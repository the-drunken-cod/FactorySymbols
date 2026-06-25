# AGENTS.md

## Non-discoverable commands

| Task | Command |
|---|---|
| Build both loaders | `./gradlew build` |
| Run NeoForge client | `./gradlew :neoforge:runClient` |
| Run Fabric client | `./gradlew :fabric:runClient` |
| Run NeoForge DataGen | `./gradlew :neoforge:runData` |
| Run Fabric DataGen | `./gradlew :fabric:runData` |
| Delete generated sources | `pnpm clean` (has tendency to fail due to not having permission) |

Node tools (`tools/`) use **pnpm**, run via the root `package.json` scripts. Do not run `npm` or `yarn` in those directories.

## Landmines / critical constraints

**DataGen JSONs are excluded from the repo** — `neoforge/src/generated/` and `fabric/src/generated/` are git-ignored until v1.0.0. Never hand-edit files in `src/generated/`; run DataGen to regenerate them. Read them only after running DataGen.

**Version-specific properties live in `versions/<mc_version>.properties`**, not in `gradle.properties` directly. When targeting a new MC version, create a new file there — do not hardcode versions in `gradle.properties`.

**NeoForge lang file is fully generated** — it lives in `neoforge/src/generated/resources` and is produced by `NeoForgeLanguageProvider` during DataGen. The build excludes the duplicate from `commonResources` via `DuplicatesStrategy.EXCLUDE`. If lang keys are missing for NeoForge, run DataGen rather than adding a static file.

**`source/` is a local reference copy** of decompiled Minecraft assets, data and source — it is not part of the build, is in .gitignore, is never distributed under any circumstance, and must be set up manually by the local developer. If `source/` is absent, don't panic; it's an optional reference for development close to the source.

**Run directories** (`neoforge/run/`, `fabric/run/`) are dev-environment artifacts for testing, not shipping artifacts.

## Architecture conventions (non-obvious)

- **When modifying or adding code:** Prefix single-line changes with `//<AI/>` and encapsulate multi-line changes with `//<AI>` and `//</AI>` to make them easily identifiable for human review. **All GenAI code must be clearly marked to ensure transparency and comply with project guidelines.** When submitting a PR as an agent, add a comment to the PR description stating something like `As per the project guidelines, this PR contains GenAI-generated code that was fully reviewed and tested by a human collaborator.`
- **Always prefer `common/`** for new logic. Use a service interface + platform implementations in `fabric/` and `neoforge/` only when the common module genuinely cannot access the API needed.
- **Custom model integer lookup:** `custom_model_data` integer is used for some blocks or items, like the Display Panel. The integer directly maps to the ordinal of the enum value of the model.
- **Sound effects must be triggered on both client and server** to play for all players — this is not enforced by any lint rule.
- **DataGen-first:** prefer datapack JSONs (tags, recipes, loot tables) and blockstates (when rendering models) over Java code wherever possible.
- Use `// #region ${Short Descriptive Title}` markers (no `#endregion`) to section Java code. Try to stay below or equal to 39 characters, or an ellipse will be shown in the center of the title.
- 4-space indentation throughout; no javax annotations unless creating a nullable value is unavoidable.
- Add imports instead of fully qualifying classes, even if they are only used once.
- Use JetBrains annotations over javax ones, but use them only when absolutely necessary.

## Branch/version convention

One branch per game version (e.g. `1.21.1`, `1.21.4`) branching from `develop`. `1.21.1` is the current main release branch. Do not merge feature work directly to `1.21.1`.
