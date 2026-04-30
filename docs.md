<!--
IDEA:
- **Factory Symbols** [1]
    - Placeholder items with basic meanings associated with their name and texture that can be used with other mods like Create to have more specific wireless redstone channels
    - Inspired by Factorio
    - Items:
        - Cheaply crafted in a stonecutter using a blank signal template
        - Signal items can be uncrafted back to the blank template in a crafting grid
        - Symbol Sets:
            - Basic: [1]
                - Numbers: 0-9
                - Letters: A-Z
                - Instructive: checkmark, cross, forbidden, stop, 8-directional arrows, advanced arrows (double-ended, bi-directional, etc.)
            - Advanced: [2]
                - Mathematical: pi, infinity, plus, minus, multiplication, division, greater than, less than, equal to, not equal to
                - Warning: general warning, fire hazard, electric hazard, biohazard, radiation hazard, explosive hazard, laser hazard
                - Science: atom, DNA, radioactive, low temp, med temp, high temp
                - Environment: house, tree, water drop, snowflake, sun, moon, lightning bolt
                - Miscellaneous: heart, star, music note, skull, happy face, sad face, neutral face
    - Blocks [2]:
        - Purely visual blocks for displaying the signal textures in the world
        - Blocks:
            - Signal Block: displays the applied signal item on all faces
            - Horizontal Signal Block: displays the applied signal item on its horizontal faces
            - Signal Panel: displays the applied signal item on its front face
            - Signal Projector: projects the applied signal item in the air above it

-->

## Symbol Templates:
Base items that can be crafted into the actual symbol items.  
Can be made from different materials, yielding different background colors in the textures, and a different registry ID prefix.  
The yield when crafting one material item into templates depends on the material's abundance and value.  
Template items have registry IDs in the format `factory_symbols:template_<material>` and the item tags `#factory_symbols:templates` and `#factory_symbols:materials/<material>`.  
  
| Material | BG Color | FG Color | Yield | Prefix |
| :-- | :-- | :-- | :-- |
| Iron | Light Gray | Black | 8 | `iron` |
| Copper | Orange | Black | 8 | `copper` |
| Redstone | Red | White | 8 | `redstone` |
| Coal | Black | White | 8 | `coal` |
| Amethyst | Magenta | Black | 8 | `amethyst` |
| Lapis | Blue | White | 8 | `lapis` |
| Quartz | White | Black | 16 | `quartz` |
| Gold | Yellow | Black | 16 | `gold` |
| Emerald | Lime Green | Black | 16 | `emerald` |
| Diamond | Light Blue | Black | 16 | `diamond` |

<br>


## Symbols:
Crafted from the symbol templates in a stonecutter, yielding 1 symbol item per craft.  
The symbol items can be uncrafted back into the symbol template in a crafting grid, yielding 1 template per craft.  
The symbol items have a uniform texture design with the symbol in the center and the material's color as a square background, allowing for good contrast and recognizability even at small sizes.  
The symbol items have registry IDs in the format `factory_symbols:symbol_<material>_<symbol_name>` and the item tags `#factory_symbols:symbols`, `#factory_symbols:categories/<category>`, and `#factory_symbols:materials/<material>`.
  
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

<br>


## Item Models and Textures:
For each material type, there needs to be a base item model with the material's texture as the background.  
For each symbol, there needs to be a datagenned item model that uses the corresponding material's base item model as the parent and applies the symbol texture as an overlay. For contrast to work there needs to be a black and white version of each symbol texture.
