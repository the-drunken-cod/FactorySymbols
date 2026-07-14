## Roadmap (partially outdated info):
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
    - change from `symbols_n_signs:symbol_<material>` to `symbols_n_signs:<category>_<symbol>_<material>`
    - allows for item checks that only check the registry ID, like Create Redstone Link frequencies
    - makes it so there's no need for datafixing when adding or modifying symbols
4. [x] **Sign Post & Fixture:**
    - [x] **Sign Post:** thin pole shaped block for realistic looking signs
        - allows for IRL road-sign-like structures
        - can connect to any center-supporting top or bottom blockface
        - can horizontally branch off by connecting to adjacent sign post blocks
        - behaves similar to a chorus plant stalk for self-connection purposes
    - [x] **Sign Fixture:** blockentity that can attach to any blockface that is center-supporting
        - can hold a symbol item and display it on a flat surface facing toward the camera when placing, like a road sign
        - can contain any item and display it in the same way
        - very small fixture model that grips onto the top or bottom center of the displayed item
        - 16 color variants saved in blockstate & item NBT
        - if attached to a vertical sign post, transforms the block into a special blockentity that allows the sign to render much closer to the post
    - [x] **Lamp Fixture:** block that attaches to any blockface and emits light
        - can be dyed in 16 colors
        - special model when placed on center-supporting faces, like sign posts
    - [x] **Button Fixture:** placed on vertical sign posts
        - transforms the targeted post into a special blockentity
        - when clicked, makes the block below the post emit a redstone signal, allowing redstone dust below to be powered
    - [x] **Redstone Fixture:** Strongly powers the block it faces when the sign post is powered, eliminating the need for using a comparator to read the post's signal, for better aesthetics and more compact redstone designs.
5. [x] **Rename to Symbols'n'Signs**
6. [ ] **More Symbols:**
    - [ ] more emoji and unicode symbols
    - [ ] signs (one material per symbol with single-file, non-layered textures)
        - [x] road signs
        - [ ] GHS hazard signs
        - [ ] NFPA hazard signs
7. [ ] **Final Polish 🇵🇱:**
    - Lamp Fixture:
        - [ ] More lamp models: bigger lamp, connected lamp, caged lamp and modern (LED) lamp.
        - [ ] Make lamp place and remove a `minecraft:light` block above the first non-air block in the column below it.
    - Ratchet Wrench:
        - [ ] Sneak-right-click to remove fixtures from sign posts and signs from sign fixtures, instead of breaking the entire block.
    - Redstone Emitter Fixture:
        - [ ] Make emitters power sign posts to act as a signal relay. (But make sure to investigate infinite loop potential and performance, maybe add a server config toggle?)
8. [ ] **v1.0.0 Release**
9. [ ] **Configuration Clipboard:**
    - [ ] Item that can copy and paste fixture configurations for faster configuration.
    - [ ] If held in the offhand while placing fixtures, auto-applies the settings while placing.
10. [ ] **Future Features:**
    - wide variety of road markings that all interconnect.
        - white/yellow/gray colors.
        - zebra stripes (placed like minecart tracks).
        - single/double solid/double dashed/double solid & dashed (L & R).
        - solid/dashed/dashed (short).
    - traffic light fixture
        - remote controlled by wireless controller block?
        - dumb system (player needs to figure out phases) or smart system (GUI with phase editor)?
        - computercraft integration?
    - make display panel items rotatable like item frames (maybe via the wrench or something?).
    - make display panel placeable on the ceiling and floor.
    - make lamp fixture place a light above the first solid block in the column below where it's placed.
    - more symbol materials (e.g. netherite, quartz, prismarine, etc.).
    - user-defined symbols: allows modpack creators to add their own symbols.

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
The symbol items have registry IDs in the format `symbols_n_signs:<material>_<name>_symbol` and the item tags `#symbols_n_signs:symbols` and `#symbols_n_signs:symbols/<material>`.  
  
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
These behave differently from the symbol items, because they only come in a single material/color variant and have very unique shapes and textures inspired by European and German StVO road signs.  
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
    - Modes are internally enumerated, but should not show this integer to the player. Instead, translations like `symbols_n_signs.ratchet_wrench.mode.button_fixture.orientation` and `symbols_n_signs.ratchet_wrench.mode.button_fixture.orientation.value.0` should be used to allow for more descriptive values.
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
Blockentity that allows placing up to 6 signs on each face, that render adjacent to the sign post fixture's face.  
Each of the 6 faces can hold independent sign items, and can be individually configured via the Ratchet Wrench (probably needs adjusting in IWrenchConfigurable, to support per-face configuration, as well as full-block configuration).  
  
- When right-clicked on a sign post, transforms it into the fixture blockentity. It renders a duplicate set of segments with a thicker apothem, which are textured like metal ring clamps wrapping around the post, which the signs attach to.
- If a sign item (`#symbols_n_signs:signs`) is clicked on a *horizontal* face of a sign post, the fixture can render a sign flat against the post, or sticking out from the post rotated by 90°, if the sign's shape is horizontally supporting (configured with wrench).
- If a sign item (`#symbols_n_signs:signs`) is clicked on a *vertical* face of a sign post, the fixture can render a sign sticking out from the post rotated by 90°, if the sign's shape is vertically supporting (configured with wrench).
- The rotation, scale, and whether the sign is double-sided or not should all be configurable via the Ratchet Wrench. For this to work, this info needs to be stored in and read from NBT when rendering.
- The blockentity should allow signs to be placed on all valid faces and prevent other posts from connecting when a face is occupied by a fixture, meaning up to 6 signs can be attached to a single post, also meaning the blockentity will need to keep track of them individually in NBT, per each face. Each contained sign needs to be *fully* stored, including its own NBT, since signs might have custom text, colors, etc. in the future.
- Faces that contain a sign item should not connect to other sign posts and fixtures. If a sign is added, connectivity needs to be recalculated at least for that face.
- Redstone signals should propagate through this fixture blockentity as if it was a regular post.
- Cannot be placed anywhere else besides sign posts.
- VoxelShape needs to adjust based on NBT.
- Loot table drops `sign_post` + the fixture item (2 pools, same convention as other fixtures). Each occupied face's contained sign item (with full NBT) is dropped individually via code on block removal, since loot tables can't enumerate a dynamic per-face item set.
- A face's slot is fixed permanently at placement (determined by which face was right-clicked with the sign item). There is no wrench mode to relocate a placed sign to a different face; to move one, sneak-right-click it off (see below) and re-place it on the desired face.
- Sneak-right-clicking an occupied face with an empty hand removes only that face's sign (clearing its NBT slot and returning the item to the player), without affecting the other faces or the post itself.
- If all 6 faces are emptied, the block remains a `sign_post_sign_fixture` blockentity (does not auto-revert to a plain `sign_post`). Reverting to a plain post requires sneak-right-clicking the whole fixture with the Ratchet Wrench, same as removing any other fixture.
- "Bright" is a render-only override (like vanilla's Glow Item Frame): the face's sign renders at full brightness ignoring ambient/block light, but emits no actual light and has no effect on the level's light map.
- The Ratchet Wrench targets whichever face is literally under the player's crosshair when left/right-clicking (`IWrenchConfigurable` methods receive the clicked `Direction`); each face's selected mode and values are tracked independently.
- Stance availability is gated by the sign's `SignSupportType` (`BACK`, `BOTTOM`, `VERTICAL`, `HORIZONTAL`, `ANY`); `BACK` is implicitly included in every type, so flat Stance is always valid everywhere:
  - On a *horizontal* face (post's N/E/S/W sides): perpendicular Stance (±90°) requires `HORIZONTAL` or `ANY` support (left/right edge).
  - On a *vertical* face (post's UP/DOWN sides): perpendicular Stance (0°/90°/180°/-90°) requires `VERTICAL` or `ANY` support (top/bottom edge), or `BOTTOM` support specifically when the fixture occupies the post's UP face (a `BOTTOM`-support sign can stand on top of a post but can't hang below one).
  - All scale values (0.5-2.5) are valid in every Stance; no cross-validation between Scale and Stance.
- Ratchet Wrench Modes:
  - Stance [if horizontal]: flat, perpendicular (90°), perpendicular (-90°)
  - Stance [if vertical]: flat, perpendicular (0°), perpendicular (90°), perpendicular (180°), perpendicular (-90°)
  - Rotation (pivot point at center of orientation face): 0-7 (like item frames)
  - Scale (pivot point at center of orientation face): 0.5, 0.75, 1.0, 1.25, 1.5, 1.75, 2.0, 2.25, 2.5
  - Double-sided (render the backing texture as a bg layer): true, false
  - Bright (render at full brightness): true, false
- Example NBT:
  ```jsonc
  {
    "symbols_n_signs:signs": {
      "up": {
        "item": {
          "id": "symbols_n_signs:regulatory_yield_sign",
          "Count": 1,
          "components": {
            // ...
          }
        },
        "stance": 2,
        "rotation": 0,
        "scale": 2.25,
        "double_sided": true,
        "bright": true
      },
      "down": {
        "item": null,
        "stance": 0,
        "rotation": 7,
        "scale": 0.75,
        "double_sided": false,
        "bright": false
      },
      // ...
    }
  }
  ```

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

<br><br><br><br>

## Misc:

### Powershell Functions:
In order to use these, run `echo $PROFILE` in Powershell, then create and/or open that file and paste the following code snippets in it. Also refer to [the Powershell docs.](https://learn.microsoft.com/en-us/powershell/module/microsoft.powershell.core/about/about_profiles?view=powershell-7.6)  
  
- `neo` command to run datagen with the default mods in `neoforge/run/mods/` disabled, then reenable them and run the client:  
    
    ```ps1
    function neo {
        $ModsFolder = 'neoforge/run/mods'
        $ModsFolderRenamed = 'neoforge/run/mods_disabled_by_neocmd__'
        $FolderRenamed = 0
    
        if (Test-Path -Path $ModsFolder) {
            Move-Item -Path $ModsFolder -Destination $ModsFolderRenamed
            $FolderRenamed = 1
        }
    
        ./gradlew :neoforge:runData $args
    
        $RunDataExitCode = $LASTEXITCODE
    
        if ($FolderRenamed -eq 1) {
            if (Test-Path -Path $ModsFolder) {
                Remove-Item -Path $ModsFolder -Recurse -Force
            }
            Move-Item -Path $ModsFolderRenamed -Destination $ModsFolder -Force
        }
    
        if ($RunDataExitCode -eq 0) {
            ./gradlew :neoforge:runClient $args
        }
    }
    ```
- `neoc` and `neod` as shortcut aliases for `:neoforge:runData` and `:neoforge:runClient`, with support for drilling arguments:
    ```ps1
    function neoc {
        ./gradlew :neoforge:runClient $args
    }
    function neod {
        ./gradlew :neoforge:runData $args
    }
    ```
