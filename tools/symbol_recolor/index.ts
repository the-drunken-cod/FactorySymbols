import { styleText } from "node:util";
import fs from "node:fs/promises";
import path from "node:path";
import prompt from "prompts";
import sharp from "sharp";

// Color remapping tool for factory symbols:
// 1. Prompt for base path
// 2. Recursively collect all .png files ending in _black or _white
// 3. Find files missing their counterpart (_black without _white, or vice versa)
// 4. Collect all unique colors across every source image that needs a counterpart
// 5. Bulk-prompt ONCE for all colors, then generate every missing file in one pass

async function collectPngFiles(dir: string): Promise<string[]> {
  const entries = await fs.readdir(dir, { withFileTypes: true });
  const files: string[] = [];
  for (const entry of entries) {
    const full = path.join(dir, entry.name);
    if(entry.isDirectory())
      files.push(...(await collectPngFiles(full)));
    else if(entry.isFile() && entry.name.toLowerCase().endsWith(".png"))
      files.push(full);
  }
  return files;
}

/** Returns a map of uppercase hex color → pixel count, skipping fully transparent pixels. */
async function extractColorCounts(filePath: string): Promise<Map<string, number>> {
  const { data, info } = await sharp(filePath)
    .ensureAlpha()
    .raw()
    .toBuffer({ resolveWithObject: true });

  const counts = new Map<string, number>();
  const ch = info.channels; // 4 (RGBA after ensureAlpha)

  for(let i = 0; i < data.length; i += ch) {
    if(data[i + 3] === 0)
      continue; // skip fully transparent
    const hex =
      "#" +
      data[i].toString(16).padStart(2, "0") +
      data[i + 1].toString(16).padStart(2, "0") +
      data[i + 2].toString(16).padStart(2, "0");
    const key = hex.toUpperCase();
    counts.set(key, (counts.get(key) ?? 0) + 1);
  }
  return counts;
}

/** Recolors an image using the provided map and writes it to outputPath. */
async function recolorImage(
  inputPath: string,
  outputPath: string,
  colorMap: Map<string, { r: number; g: number; b: number }>
): Promise<void> {
  const { data, info } = await sharp(inputPath)
    .ensureAlpha()
    .raw()
    .toBuffer({ resolveWithObject: true });

  const ch = info.channels;
  for (let i = 0; i < data.length; i += ch) {
    if(data[i + 3] === 0)
      continue;
    const key = (
      "#" +
      data[i].toString(16).padStart(2, "0") +
      data[i + 1].toString(16).padStart(2, "0") +
      data[i + 2].toString(16).padStart(2, "0")
    ).toUpperCase();
    const target = colorMap.get(key);
    if(target) {
      data[i] = target.r;
      data[i + 1] = target.g;
      data[i + 2] = target.b;
    }
  }

  await sharp(data, {
    raw: { width: info.width, height: info.height, channels: info.channels },
  })
    .png()
    .toFile(outputPath);
}

function parseHex(hex: string): { r: number; g: number; b: number } | null {
  const clean = hex.replace(/^#/, "");
  if(!/^[0-9a-fA-F]{6}$/.test(clean))
    return null;
  return {
    r: parseInt(clean.slice(0, 2), 16),
    g: parseInt(clean.slice(2, 4), 16),
    b: parseInt(clean.slice(4, 6), 16),
  };
}

async function main() {
  const { basePath } = await prompt({
    type: "text",
    name: "basePath",
    message: "Enter the base directory path:",
  });

  if(!basePath) {
    console.log(styleText("red", "No path provided, exiting."));
    return;
  }

  const allFiles = await collectPngFiles(basePath as string);

  // Find every file missing its _black or _white counterpart
  type MissingPair = { existing: string; missing: string };
  const missingPairs: MissingPair[] = [];
  const fileSet = new Set(allFiles.map((f) => f.toLowerCase()));

  for(const file of allFiles) {
    const dir = path.dirname(file);
    const ext = path.extname(file);
    const stem = path.basename(file, ext);
    const stemLower = stem.toLowerCase();

    if(stemLower.endsWith("_black")) {
      const counterpart = path.join(dir, stem.slice(0, -"_black".length) + "_white" + ext);
      if(!fileSet.has(counterpart.toLowerCase()))
        missingPairs.push({ existing: file, missing: counterpart });
    } else if(stemLower.endsWith("_white")) {
      const counterpart = path.join(dir, stem.slice(0, -"_white".length) + "_black" + ext);
      if(!fileSet.has(counterpart.toLowerCase()))
        missingPairs.push({ existing: file, missing: counterpart });
    }
  }

  if(missingPairs.length === 0) {
    console.log(styleText("green", "All files already have counterparts. Nothing to do."));
    return;
  }

  console.log(styleText("cyan", `Found ${missingPairs.length} file(s) missing a counterpart.`));
  console.log(styleText("cyan", "Scanning colors across all source images..."));

  // --- Bulk color collection across ALL source images ---
  const globalColorCounts = new Map<string, number>();
  for(const pair of missingPairs) {
    const counts = await extractColorCounts(pair.existing);
    for(const [hex, count] of counts)
      globalColorCounts.set(hex, (globalColorCounts.get(hex) ?? 0) + count);
  }

  const significantColors = [...globalColorCounts.entries()]
    .sort(([, a], [, b]) => b - a) // most-frequent first
    .map(([hex]) => hex);

  console.log(
    styleText("cyan", `Found ${significantColors.length} color(s) across all images.`)
  );
  console.log("Enter a replacement hex (e.g. #FFFFFF) for each, or leave blank to keep it.\n");

  // --- Single bulk prompt for ALL colors at once ---
  const colorAnswers = await prompt(
    significantColors.map((hex) => ({
      type: "text" as const,
      name: hex,
      message: `Replace ${styleText("yellow", hex)} with:`,
      validate: (v: string) =>
        v === "" || /^#?[0-9a-fA-F]{6}$/.test(v) ? true : "Enter a valid 6-digit hex or leave blank",
    }))
  );

  const colorMap = new Map<string, { r: number; g: number; b: number }>();
  for(const [hex, value] of Object.entries(colorAnswers)) {
    if(typeof value === "string" && value.trim() !== "") {
      const parsed = parseHex(value.trim());
      if(parsed)
        colorMap.set(hex, parsed);
    }
  }

  if(colorMap.size === 0)
    console.log(styleText("yellow", "No replacements specified — copying files unchanged."));

  // --- Generate all missing counterparts ---
  for(const pair of missingPairs) {
    const rel = path.relative(basePath as string, pair.missing);
    process.stdout.write(`  Creating ${styleText("green", rel)}... `);
    if(colorMap.size > 0)
      await recolorImage(pair.existing, pair.missing, colorMap);
    else
      await fs.copyFile(pair.existing, pair.missing);
    process.stdout.write(styleText("green", "done\n"));
  }

  console.log(styleText("green", `\nDone! Created ${missingPairs.length} file(s).`));
}

main().catch((err: unknown) => {
  console.error(styleText("red", String(err)));
  process.exit(1);
});
