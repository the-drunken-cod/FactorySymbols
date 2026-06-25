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
A property of a `SignType` (`BACK`, `BOTTOM`, `VERTICAL`, `HORIZONTAL`, `ANY`) describing which edges of the sign's texture/shape are physically supportable. Every type implicitly includes `BACK` (flat mounting is always valid on every face, for every sign). Gates which [[Stance]] values are available on a given [[Sign Fixture]] face:
- `HORIZONTAL` faces (the post's N/E/S/W side faces) only allow perpendicular Stance (sticking out ±90°) for signs with `HORIZONTAL` or `ANY` support (left/right edge).
- `VERTICAL` faces (the post's UP/DOWN faces) only allow perpendicular Stance (4 cardinal directions) for signs with `VERTICAL` or `ANY` support (top/bottom edge), or `BOTTOM` support when the fixture occupies the post's UP face specifically (a `BOTTOM`-support sign can stand on top of a post but can't hang below one, since it has no top edge to hang from).
- `BACK`-only signs are flat-only everywhere.

## Bright (Sign Fixture)
A per-face, wrench-configurable boolean that renders that face's sign at full brightness, ignoring ambient/block light — purely a render-time override (like vanilla's Glow Item Frame). It does not emit actual light and has no effect on the level's light map.
