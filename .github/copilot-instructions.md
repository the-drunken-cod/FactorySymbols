---
name: Base Instructions
description: This file contains the base instructions for the Factory Symbols Minecraft mod project, like coding conventions and guidelines.
applyTo: "**"
---

# Base Instructions

Language: Java 21  
Application Type: Minecraft mod named "Factory Symbols" that adds a bunch of symbol items to the game, without any functionality of their own.
Minecraft Version: 1.21.1  
Modding Framework: NeoForge v21.1

# Reference

The full Minecraft source code and assets are available under `source/` (if set up properly):

- `source/assets/` contains all assets in the format of a standard resource pack.
- `source/data/` contains the default datapack, like vanilla tags and more.
- `source/src/` contains the actual Java classes.

The datagenned JSONs can be found under `src/generated/` for reference, but don't edit those directly.

# Conventions

- Always try to implement a modloader-agnostic solution in `common/`. If not possible, implement a service interface and a modloader-specific implementation in `fabric/` and `neoforge/`. Deduplicate as much code as possible in the common implementation and only override the necessary parts in the modloader-specific implementations.
- Don't give up on a problem and suggest adding a `// TODO: fix` comment. Realize dead ends and think about solutions or alternatives. Interject with questions if needed and speak up when there's an objectively better path.
- Don't add comments for the sake of comments. Code should be self-explanatory and comments reserved for explanations or important notes.
- Try to use datapack JSONs before writing any Java code.
- Use 4 spaces for indentation.
- Add `//#region` indicators for logical code sections (without `#endregion`). Keep the initial region comment concise and descriptive.
- Respect the existing code style and don't use javax annotations.
- In the output, instead of including unmodified members, only show the new or modified code and make use of comments like `/* existing code */`.
