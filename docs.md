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
  
| Material | Color | Yield | Prefix |
| :-- | :-- | :-- | :-- |
| Paper | White | 2 | `paper` |
| Iron | Gray | 8 | `iron` |
| Gold | Yellow | 8 | `gold` |
| Copper | Orange | 4 | `copper` |
| Redstone | Red | 8 | `redstone` |
| Coal | Black | 4 | `coal` |
| Emerald | Lime Green | 16 | `emerald` |
| Diamond | Light Blue | 16 | `diamond` |
| Amethyst | Magenta | 8 | `amethyst` |

<br>


## Symbols:
Crafted from the symbol templates in a stonecutter, yielding 1 symbol item per craft.  
The symbol items can be uncrafted back into the symbol template in a crafting grid, yielding 1 template per craft.  
The symbol items have a uniform texture design with the symbol in the center and the material's color as a square background, allowing for good contrast and recognizability even at small sizes.  
The symbol items have registry IDs in the format `factory_symbols:symbol_<material>_<symbol_name>`.
  
| Category | Emoji | Symbol |
| :-- | :-- | :-- |
| Numbers | 0️⃣ 1️⃣ 2️⃣ 3️⃣ 4️⃣ 5️⃣ 6️⃣ 7️⃣ 8️⃣ 9️⃣ | `0-9` |
| Letters | 🇦 🇧 🇨 🇩 🇪 🇫 🇬 ... | `A-Z` |
| Instructive | ✔️ ❌ ⛔ 🛑 ⬆️ ... | checkmark, cross, forbidden, stop, 8-directional arrows, advanced arrows (double-ended, bi-directional, etc.) |
| Mathematical | π ∞ ➕ ➖ ✖️ ➗ ... | pi, infinity, plus, minus, multiplication, division, greater than, less than, equal to, not equal to, modulo |
| Warning | ⚠️ 🔥 ⚡ ☣️ ... | general warning, fire hazard, electric hazard, biohazard, radiation hazard, explosive hazard, laser hazard |
| Science | ⚛️ 🧬 ☢️ ... | atom, DNA, radioactive, low temp, med temp, high temp |
| Environment | 🏠 🌳 💧 ❄️ ☀️ ... | house, tree, water drop, snowflake, sun, moon, lightning bolt |
| Misc | ❤️ ⭐ 🎵 💀 🙂 ... | heart, star, music note, skull, happy face, sad face, neutral face |

<br>


## Item Models and Textures:
For each material type, there needs to be a base item model with the material's texture as the background.  
For each symbol, there needs to be a datagenned item model that uses the corresponding material's base item model as the parent and applies the symbol texture as an overlay. For contrast to work there needs to be a black and white version of each symbol texture.
