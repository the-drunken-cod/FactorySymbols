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
    - [x] **Sign Post:** thin pole shaped block for realistic looking signs
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
    - [x] **Button Fixture:** placed on vertical sign posts
        - transforms the targeted post into a special blockentity
        - when clicked, makes the block below the post emit a redstone signal, allowing redstone dust below to be powered
    - [ ] **Redstone Fixture:** Strongly powers the block it faces when the sign post is powered, eliminating the need for using a comparator to read the post's signal, for better aesthetics and more compact redstone designs.
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

<br>

## Road Signs:
These behave differently from the symbol items, because they only come in a single material/color variant and have very unique shapes and textures inspired by European and German road signs.  
The crafting recipe also slightly differs, requiring retroreflective iron sheets.  
  
Example signs (not final):  
|  | Category | Shape | Sign |
| :-- | :-- | :-- | :-- |
|   | Hazard | Triangle | Steep Downgrade |
|   | Hazard | Triangle | Steep Upgrade |
|   | Hazard | Triangle | Uncontrolled Intersection Ahead |
|   | Hazard | Triangle | Uneven Road Surface Ahead |
|   | Hazard | Triangle | Narrow Road Ahead |
|   | Hazard | Triangle | Pedestrians Ahead |
|   | Hazard | Triangle | Railway Crossing Ahead |
|   | Hazard | Triangle | Bridge Ahead |
|   | Hazard | Triangle | Oncoming Traffic |
|   | Hazard | Triangle | Traffic Light |
|   | Hazard | Triangle | Animals Crossing |
|   | Hazard | St. Andrew's Cross | Railway Crossing |
|   | Regulatory | Triangle | Priority |
|   | Regulatory | Triangle | Merge Left |
|   | Regulatory | Triangle | Merge Right |
|   | Regulatory | Circle | Go Left |
|   | Regulatory | Circle | Go Right |
|   | Regulatory | Circle | Go Straight |
|   | Regulatory | Circle | Go Left Here (horiz. left) |
|   | Regulatory | Circle | Go Right Here (horiz. right) |
|   | Regulatory | Circle | Along Left (diag. down-left) |
|   | Regulatory | Circle | Along Right (diag. down-right) |
|   | Regulatory | Circle | Straight or Right |
|   | Regulatory | Circle | Straight or Left |
|   | Regulatory | Circle | Roundabout |
|   | Regulatory | Circle | Pedestrians Only |
|   | Regulatory | Circle | No Speed Limit |
|   | Regulatory | Circle | Speed Limit 5, 10-150 (incr. by 10) |
|   | Regulatory | Circle | No Speed Limit |
|   | Regulatory | Circle | Overtaking allowed |
|   | Regulatory | Circle | U-Turn Left |
|   | Regulatory | Circle | U-Turn Right |
|   | Regulatory | Diamond | Priority Road |
|   | Regulatory | Diamond | Priority Road End |
|   | Regulatory | Inverted Triangle | Give Way / Yield |
|   | Regulatory | Octagon | Stop |
|   | Regulatory | Rectangle | One-Way Left |
|   | Regulatory | Rectangle | One-Way Right |
|   | Regulatory | Square | Parking |
|   | Regulatory | Square | Parking Garage |
|   | Regulatory | Square | Dead End |
|   | Regulatory | Square | Curve Marker Left |
|   | Regulatory | Square | Curve Marker Right |
|   | Regulatory | Square | Pedestrian Crossing |
|   | Regulatory | Square | Highway Beginning |
|   | Regulatory | Square | Highway End |
|   | Regulatory | Square | Tunnel |
|   | Regulatory | Square | Green Arrow Left |
|   | Regulatory | Square | Green Arrow Right |
|   | Prohibition | Circle | No Entry |
|   | Prohibition | Circle | No Stopping |
|   | Prohibition | Circle | No Parking |
|   | Prohibition | Circle | No Overtaking |
|   | Prohibition | Circle | U-Turn Prohibited Left |
|   | Prohibition | Circle | U-Turn Prohibited Right |
|   | Prohibition | Circle | No Vehicles |
|   | Prohibition | Circle | No Heavy Vehicles |
|   | Prohibition | Circle | No Cars |
|   | Prohibition | Circle | No Bicycles |
|   | Prohibition | Circle | No Pedestrians |
|   | Extra | Circle | Bus/Tram/Train Stop "H" |
|   | Extra | Circle | NATO Bridge Load Classification |
|   | Extra | Inverted Triangle | Nature Reserve |
|   | Extra | Rectangle | Traffic-Calmed Zone Beginning |
|   | Extra | Rectangle | Traffic-Calmed Zone End |
|   | Extra | Square | Bus/Tram/Train Stop |
|   | Extra | Square | Gas Station |
|   | Extra | Square | Recharging Station |
|   | Extra | Square | Emergency Stopping Bay |
|   | Extra | Square | Emergency Telephone |
|   | Extra | Square | Information |
|   | Extra | Square | Motel |
|   | Extra | Square | Inn |
|   | Extra | Square | Toilet |

<br>

## Sign Post:
A thin pole-shaped cable-like block that can connect to any center-supporting blockface, allowing for realistic-looking signs.  
  
Redstone signals that are passed into any block of a connected sign post structure should propagate through the entire structure (max distance of 15 blocks), allowing for a sort of 3D redstone wiring.  
Comparators need to be used to read whether a sign post is powered or not.  
  
Buttons, Levers, Signs, Fixtures, and many more blocks can be attached to the sign post, allowing for a wide variety of uses.  
Fixtures are items that convert the sign post block into a special blockentity that can do more complex interactions and rendering.

<br>

## Sign Post Fixtures:
These are items that can be right-clicked onto sign posts to convert them into special blockentities.  
Each type of fixture has a different function and model (not stored via blockdata, but via NBT), but also some shared functionality like passing through the POWERED state and preventing other blocks from connecting to the face that the fixture is attached to.  
Each fixture block needs a loot table that makes it drop both a sign post and the fixture item.

<br>

### Ratchet Wrench:
- Item that allows for configuring sign post fixtures, as well as potentially other blocks in the future.
- Uses the same mechanisms as wrenches from other mods like Create, Mekanism, etc. so that they are interoperable.
- Interaction is the same as the Debug Stick from Vanilla (left-click to change mode, which gets displayed above the hotbar, right-click to change the selected mode's value).
    - Modes are internally enumerated, but should not show this integer to the player. Instead, translations like `factory_symbols.ratchet_wrench.mode.button_fixture.orientation` and `factory_symbols.ratchet_wrench.mode.button_fixture.orientation.value.0` should be used to allow for more descriptive values.
    - Modes should be easily defineable and extensible in the code by modifying an enum class or something.
- Two tooltip lines to explain the interactions:
    - "L-Click on Fixture: Switch Mode"
    - "R-Click on Fixture: Change Value"
- Modes:
    - Button Fixture:
        - Fixture Orientation: 0-3 for all 4 possible horizontal rotations of the button on the post
        - Button Mode: 0 (default) for stone button behavior, 1 for wooden button behavior, 2 for toggle button behavior
        - Active High: 0 for emitting a redstone signal when not powered, 1 (default) for emitting a redstone signal when powered
    - Sign Fixture:
        - Sign Orientation: 0-7 for 8 possible rotations of the contained sign
        - Sign Scale: 0-2 for scaling the contained sign's model up or down (default is 1)
        - Fixture Orientation: 0-5 for all 6 possible faces of the post to attach the fixture to
        - Double-sided: 0 (default) for only rendering the sign on the fixture's front side, 1 for also rendering a flipped version of the sign on the back side of the fixture
    - Lamp Fixture:
        - Fixture Orientation: 0-1 for the 2 possible orientations of the lamp on the post (long side facing north-south or east-west)
        - Light Level: 0-15 for the light level emitted by the lamp (default is 15)
    - Redstone Fixture:
        - Orientation: 0-5 for all 6 possible faces of the post to attach the fixture to.
        - Inverted: 0 (default) emits when high, 1 emits when low.

<br>

### Button Fixture:
- If clicked on a *horizontal* face of a sign post, the fixture renders a button model attached to the sign post that, when pressed, sends a redstone signal into the adjacent sign post by setting the POWERED blockstate and propagating it along the post, like a regular sign post does. The details of which need to be read from NBT (to respect what was configured with the wrench item).
- The blockentity should allow one button to be placed on any *horizontal* face and prevent other posts from connecting when a face is occupied by a button.
- Redstone signals should propagate through this fixture blockentity as if it was a regular post.
- Cannot be placed anywhere else besides sign posts.

<br>

### Sign Fixture:
- If clicked on a *horizontal* face of a sign post, the fixture can render a sign flat against the post, or sticking out from the post rotated by 90°, if the sign's shape is horizontally supporting.
- If clicked on a *vertical* face of a sign post, the fixture can render a sign sticking out from the post rotated by 90°, if the sign's shape is vertically supporting.
- The rotation, scale, and whether the sign is double-sided or not should be configurable with the wrench item, and needs to be read from NBT when rendering.
- The blockentity should allow signs to be placed on all valid faces and prevent other posts from connecting when a face is occupied by a fixture, meaning up to 6 signs can be attached to a single post, also meaning the blockentity will need to keep track of them individually in NBT. Each contained sign needs to be *fully* stored, including its own NBT, since signs might have custom text, colors, etc. in the future.
- Redstone signals should propagate through this fixture blockentity as if it was a regular post.
- Cannot be placed anywhere else besides sign posts.

<br>

### Lamp Fixture:
- If clicked on a vertical face of a sign post, the fixture can render a lamp model that emits light and is attached to the post.
- Once converted, the sign post will always keep two faces active, so that the lamp's model can attach to the post and render properly. E.g. if the lamp is attached so that its 2 long sides are facing north and south, then the north and south faces, as well as the bottom face of the post can no longer connect.
- Redstone signals should propagate through this fixture blockentity as if it was a regular post.
- Cannot be placed anywhere else besides sign posts.

<br>

### Redstone Fixture:
- If clicked on any face of a sign post, transforms it into a BE that renders a small model attached to the post and emits a redstone signal into the adjacent block in the direction it is facing when the post network's signal is high.
- The blockentity should allow one fixture to be placed on any face and prevent other posts from connecting when a face is occupied by a fixture.
- Block at FACING receives a strong signal, allowing dust and other redstone components to be powered across a 1 (transmissive) block gap from the sign post.
- Redstone signals should propagate through this fixture blockentity as if it was a regular post.
- Cannot be placed anywhere else besides sign posts.
