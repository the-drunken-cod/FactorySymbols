# `symbols_n_signs:sign_post_sign_fixture`
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
