# Symbols'n'Signs Contributing Guide
## Table of Contents

- [Code of Conduct](#code-of-conduct)
- [Local Setup](#local-setup)
- [Commands](#commands)

<br>

## Code of Conduct

- When contributing AI-generated code in any way, be transparent about its usage in the PR description. If your provider supports the `AGENTS.md` or `.github/copilot-instructions.md` files, make sure you and the model follow the guidelines outlined there.
- We follow the [Contributor Covenant Code of Conduct.](https://www.contributor-covenant.org/version/2/0/code_of_conduct/) In short: just don't be a jerk?
- If you have any doubts about whether your contribution is appropriate, please reach out to the maintainers for guidance.

<br>

## Local Setup

1. [Fork the repository](https://docs.github.com/en/pull-requests/collaborating-with-pull-requests/working-with-forks/fork-a-repo) and [clone it](https://docs.github.com/en/repositories/creating-and-managing-repositories/cloning-a-repository) to your local machine using Git.
2. Install Java 21 (I recommend using [Adoptium Temurin](https://adoptium.net/)).
3. Optionally install [Node.js](https://nodejs.org/) and [pnpm](https://pnpm.io/) if you want to run the Node-based tools (refer to `scripts` in `package.json`).
4. Install your preferred IDE.  
  I use and recommend [Visual Studio Code](https://code.visualstudio.com/) with the extensions defined in `.vscode/extensions.json` (you should get an automatic prompt to install them when opening the project). It isn't an IDE designed for Java though, so there can be a bit of jank.
5. Open the project in your IDE and let it index and set up the gradle workspace.  
  For me personally, it takes a few times of restarting VS Code after the first setup for gradle to work properly. Killing all Java processes and VS Code and running `./gradlew clean build --refresh-dependencies` in the terminal seems to sometimes help too.
6. Optionally, but highly recommended, set up the `source/` directory if you want to have a local reference of the decompiled Minecraft source code and resources. You can find guides to do this yourself on the Internet. You wanna end up with the Java source code, the default resource pack assets, and the default datapack data in there.
7. You should now be able to run the mod in a development environment! Now refer the commands listed in the [Commands](#commands) section below.

<br>

## Commands

| Command | Description |
| :-- | :-- |
| `./gradlew :neoforge:runClient` | Runs the NeoForge dev client using the un-authenticated `Dev` account. |
| `./gradlew :fabric:runClient` | Same as above, but uses the Fabric loader. |
| `./gradlew :neoforge:runData` | Runs the NeoForge DataGen task, which generates all JSON assets and lang files. Remember to run this after making changes to any code or data that affects the generated assets! This includes files in `common/src/main/resources/` that would need to be merged with generated resources during the build. If you're unsure, first run this, then `runClient`. |
| `./gradlew :fabric:runData` | Same as above, but for the Fabric loader. |
| `./gradlew build` | Builds the mod for both loaders, producing JAR files in `neoforge/build/libs/` and `fabric/build/libs/`. Remember to run the DataGen task first to make sure all generated assets are up to date before building! |
| `./gradlew :neoforge:build` | Same as above, but only builds the NeoForge version. |
| `./gradlew :fabric:build` | Same as above, but only builds the Fabric version. |
| `./gradlew clean (build\|:neoforge:build\|:fabric:build)` | Cleans the build directories before building. |
| `./gradlew clean --refresh-dependencies` | Cleans the build directories and refreshes all dependencies. This can help resolve weird issues with gradle, especially after modifying dependencies in `gradle.properties` or `versions/`. |
| `pnpm clean` | Deletes the generated sources in `src/generated/`. This is useful if you want to make sure that all generated files are actually up to date and not accidentally left unchanged after a code change. Note that this command often seems to fail due to permission issues, so you might have to delete the files manually or run the command with elevated permissions or a different user on Linux. |

<br>

