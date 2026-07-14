# Context

## Sign Post Fixture
An item that can be right-clicked onto a [[Sign Post]] to convert it into a special blockentity with extra behavior (e.g. [[Sign Fixture]], Button Fixture, Lamp Fixture, Redstone Emitter Fixture). Each fixture tracks which of the post's 6 faces it occupies and blocks other posts/fixtures from connecting to an occupied face.

## Sign Fixture
A [[Sign Post Fixture]] that can hold an independent sign or symbol item on each of the post's 6 faces simultaneously (unlike other fixtures, which only ever occupy a single face). Each occupied face stores its own item (with full NBT), [[Stance]], Rotation, scale, double-sided flag, and brightness flag.

## Face (of a Sign Post Fixture)
Which of the 6 directions a fixture's slot belongs to. For the Sign Fixture, the face is fixed permanently at placement time, determined by which face of the post was right-clicked with the sign/symbol item. There is no wrench mode to relocate a placed sign to a different face — the player must remove it (sneak-right-click that face) and re-place it on the desired face.

## Stance
The wrench-configurable mounting mode of a sign within its [[Face]]'s slot: flat against the post, or sticking out perpendicular (+90° or -90°). Available stance options depend on whether the face is horizontal or vertical, and on the sign's support type. Distinct from **Rotation** (the 0-7 spin of the sign within its mount) and **Face** (which side of the post it's on).

## Rotation
The wrench-configurable 0-7 spin of a sign within its slot, analogous to item frame rotation. Distinct from [[Stance]].

## Sign Support Type
**Deprecated** — slated for removal; all Stance values are planned to become valid on every face regardless of Sign Support Type. Until removed: a property of a `SignType` (`BACK`, `BOTTOM`, `VERTICAL`, `HORIZONTAL`, `ANY`) describing which edges of the sign's texture/shape are physically supportable. Every type implicitly includes `BACK` (flat mounting is always valid on every face, for every sign). Gates which [[Stance]] values are available on a given [[Sign Fixture]] face:
- `HORIZONTAL` faces (the post's N/E/S/W side faces) only allow perpendicular Stance (sticking out ±90°) for signs with `HORIZONTAL` or `ANY` support (left/right edge).
- `VERTICAL` faces (the post's UP/DOWN faces) only allow perpendicular Stance (4 cardinal directions) for signs with `VERTICAL` or `ANY` support (top/bottom edge), or `BOTTOM` support when the fixture occupies the post's UP face specifically (a `BOTTOM`-support sign can stand on top of a post but can't hang below one, since it has no top edge to hang from).
- `BACK`-only signs are flat-only everywhere.

## Bright (Sign Fixture)
A per-face, wrench-configurable boolean that renders that face's sign at full brightness, ignoring ambient/block light — purely a render-time override (like vanilla's Glow Item Frame). It does not emit actual light and has no effect on the level's light map.

## Configuration (of an `IWrenchConfigurable` block)
The subset of a block's state that the [[Configuration Clipboard]] and Ratchet Wrench can copy, paste, and configure — e.g. a Button Fixture's Active High flag and orientation, a Sign Fixture face's [[Stance]]/Rotation/Scale/Offset/double-sided mode/[[Bright (Sign Fixture)]], or a Display Panel's Rotation/Scale/Bright. Each `IWrenchConfigurable` implementor decides which of its own properties count as Configuration; it explicitly excludes transient/derived state (e.g. a Button's Pressed flag, a Lamp's Lit flag). Where an implementor's Configuration includes an item it holds (Sign Fixture face, Display Panel), copying never duplicates that item — see [[Configuration Clipboard]].

## Configuration Clipboard
An item that copies (shift-right-click) and pastes (right-click) an `IWrenchConfigurable` block's [[Configuration (of an `IWrenchConfigurable` block)]] to/from `custom_data`, keyed by the target block's registry id — one stored entry per block type, so copying one block type's Configuration doesn't overwrite an already-stored entry for a different type. For the Sign Fixture, copying reads whichever face was clicked, but the stored Configuration can be pasted onto *any* face of a target Sign Fixture (source face is not tracked). If held in the offhand while placing an `IWrenchConfigurable` block, its stored Configuration for that block type is applied automatically at placement time, taking the place of a wrench visit. Crafting one alone in a crafting grid yields a blank clipboard with no stored configurations.

Where a Configuration includes an item the block holds (a Sign Fixture face's contained sign, a Display Panel's stored item), pasting looks for an exact match (same item, same components) for the originally-copied item within the target player's main inventory and hotbar, moving it in if found and the target slot/face is currently empty. If no match is found, or the target is already occupied by a different item, the paste applies only the settings and leaves the existing item exactly as it was — it never duplicates, swaps, or discards an item.

## Display Panel
A blockentity alternative to item frames that displays one held item flat against its mounting face. Its Configuration (Rotation: 8-way in-plane spin like an Item Frame; Scale: 0.5-1.0 in 0.1 steps, cosmetic only, does not affect the block's VoxelShape; Bright: full-brightness render override, no real light emission) is independent of `FACING`/`COLOR`/`LOCKED` blockstate properties and is configurable via the Ratchet Wrench and [[Configuration Clipboard]], alongside its existing click-to-store/shift-click-to-remove item interaction.

## Format Version (of a Configuration)
A per-block integer, returned by a method each `IWrenchConfigurable` implementor defines itself, identifying the shape of the Configuration data it stores. A stored clipboard Configuration whose Format Version doesn't match the target block's current Format Version is treated the same as no stored Configuration at all (paste fails with the same message), since migration between Format Versions isn't implemented yet.
