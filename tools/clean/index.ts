import { unlink, stat } from "node:fs/promises";
import { join, resolve } from "node:path";

const deletePaths = [
  "neoforge/src/generated/",
  "fabric/src/generated/",
];

const rootDir = resolve(import.meta.dirname, "../../");

async function main() {
  for(const delPath of deletePaths) {
    try {
      const path = join(rootDir, delPath);
      const st = await stat(path);
      if(st.isFile() || st.isDirectory()) {
        await unlink(path);
        console.log(`Deleted path '${path}'`);
      }
      else
        console.warn(`Path '${path}' does not exist, skipping.`);
    }
    catch(err) {
      console.error(`Error deleting '${delPath}':`, err);
      throw err;
    }
  }
}

main().catch(() => {
  process.exit(1);
});
