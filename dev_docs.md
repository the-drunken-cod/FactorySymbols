## Roadmap:
1. [x] **Symbol Items:**
    - items with a symbol texture and a material-based background color that can be crafted and uncrafted in a stonecutter
    - [x] Crafting & Uncrafting / Transmuting
    - [x] Item Models: base model for each material + overlay for each symbol
    - [x] Item Textures:
        - odd-numbered texture width & height
        - black and white textures for contrast
2. [x] **Display Panel:**
    - blockentity alternative to item frames
        + has hitbox
        + doesnt need supporting block
        - cant rotate contained items
        - cant place multiple per block
        - cant place on top or bottom block faces
    - [x] Block Model: panel with flat front face and thin borders
    - [x] Block Texture: simple texture with a border to show the edges
    - [x] Dyeable: 16 color variants saved in blockstate & item NBT
    - [x] Hopper Interaction
    - [x] Redstone Locking: prevent any interaction while powered
    - [x] Comparator Output: emits a signal strength relative to item stackability
3. [x] **Migration to namespaced ID per symbol:**
    - change from `factory_symbols:symbol_<material>` to `factory_symbols:<category>_<symbol>_<material>`
    - allows for item checks that only check the registry ID, like Create Redstone Link frequencies
    - makes it so there's no need for datafixing when adding or modifying symbols
4. [ ] **Sign Post & Fixture:**
    - [ ] **Sign Post:** thin pole shaped block for realistic looking signs
        - allows for IRL road-sign-like structures
        - can connect to any center-supporting top or bottom blockface
        - can horizontally branch off by connecting to adjacent sign post blocks
        - behaves similar to a chorus plant stalk for self-connection purposes
    - [ ] **Sign Fixture:** blockentity that can attach to any blockface that is center-supporting
        - can hold a symbol item and display it on a flat surface facing toward the camera when placing, like a road sign
        - can contain any item and display it in the same way
        - very small fixture model that grips onto the top or bottom center of the displayed item
        - 16 color variants saved in blockstate & item NBT
        - if attached to a vertical sign post, transforms the block into a special blockentity that allows the sign to render much closer to the post
    - [ ] **Lamp Fixture:** block that attaches to any blockface and emits light
        - can be dyed in 16 colors
        - special model when placed on center-supporting faces, like sign posts
    - [ ] **Button Fixture:** placed on vertical sign posts
        - transforms the targeted post into a special blockentity
        - when clicked, makes the block below the post emit a redstone signal, allowing redstone dust below to be powered
5. [ ] **More Symbols:**
    - [ ] expand by more emoji and unicode symbols
    - [ ] add road signs (one material per symbol with single-file, non-layered textures)
6. [ ] **v1.0.0 Release**
7. [ ] **Future Features:**
    - make display panel items rotatable like item frames (maybe via a wrench item or something?)
    - make display panels placeable on the ceiling and floor
    - more symbol materials (e.g. netherite, quartz, prismarine, etc.)
    - user-defined symbols: allows modpack creators to add their own symbols

<br>

## Symbol Materials:
Materials for crafting the symbol items, each with a distinct background color and a fixed yield of symbol items per craft:  
| Material | BG Color | FG Color | Yield | Prefix |
| :-- | :-- | :-- | :-- |
| Iron | Light Gray | Black | 4 | `iron` |
| Redstone | Red | White | 4 | `redstone` |
| Coal | Black | White | 4 | `coal` |
| Gold | Yellow | Black | 4 | `gold` |
| Emerald | Lime Green | Black | 4 | `emerald` |
| Lapis | Blue | Black | 4 | `diamond` |

<br>

## Symbols:
Crafted from the symbol templates in a stonecutter, yielding 1 symbol item per craft.  
The symbol items can be uncrafted back into the symbol template in a crafting grid, yielding 1 template per craft.  
The symbol items have a uniform texture design with the symbol in the center and the material's color as a square background, allowing for good contrast and recognizability even at small sizes.  
The model of the symbol item is determined by the NBT component `custom_model_data` (e.g. `0` for the "Letter A" symbol). To figure out the model/texture, refer to the position of each enum value in `common/src/main/java/com/drunkencod/factory_symbols/symbols/SymbolType.java`.  
The symbol items have registry IDs in the format `factory_symbols:symbol_<material>` and the item tags `#factory_symbols:symbols` and `#factory_symbols:symbols/<material>`.  
  
Example categories and symbols (not final):
| Category | Emoji | Symbol |
| :-- | :-- | :-- |
| Numbers | 0️⃣ 1️⃣ 2️⃣ 3️⃣ 4️⃣ 5️⃣ 6️⃣ 7️⃣ 8️⃣ 9️⃣ | `0-9` |
| Letters | 🇦 🇧 🇨 🇩 🇪 🇫 🇬 ... | `A-Z` |
| Instructive | ✔️ ❌ ⛔ 🛑 ... | checkmark, cross, forbidden, stop, info |
| Arrows | ⬆️ ⬇️ ⬅️ ➡️ ... | up, down, left, right, up-down, cycle, double-horizontal, double-vertical, inward, outward |
| Mathematical | π ∞ ➕ ➖ ✖️ ➗ ... | pi, infinity, plus, minus, multiplication, division, greater than, less than, equal to, not equal to, modulo |
| Warning | ⚠️ 🔥 ⚡ ☣️ ... | general warning, fire hazard, electric hazard, biohazard, radiation hazard, explosive hazard, laser hazard |
| Science | ⚛️ 🧬 ☢️ ... | atom, DNA, radioactive, low temp, med temp, high temp, location |
| Environment | 🏠 🌳 💧 ❄️ ☀️ ... | house, tree, water drop, snowflake, sun, moon, lightning bolt, flame, haze, spray |
| Misc | ❤️ ⭐ 🎵 💀 🙂 ... | heart, star, music note, skull, happy face, sad face, neutral face |
