# 0003 - Sign Fixture clipboard configuration is copied per-face but pasted to any face

## Status

Accepted

## Context

The Sign Fixture holds up to 6 independent per-face configurations (see ADR 0001). The Configuration Clipboard needs to decide what "copy" and "paste" mean for a block with more than one configurable unit.

Three options were considered:
1. Whole-block copy: shift-right-clicking any face snapshots all 6 faces at once into a single clipboard entry; pasting overwrites all 6 faces on the target.
2. Per-face copy and paste: the clicked face's configuration is stored under a composite key (e.g. `blockId:faceName`), and paste only ever applies to the same face it was copied from.
3. Per-face copy, any-face paste: the clicked face's configuration is stored under the plain `blockId` key (one entry per block type, same as every other fixture), but the source face is not remembered — paste can target any face of any Sign Fixture.

## Decision

Option 3. Copying reads only the clicked face's Configuration (never all 6 at once, avoiding accidental clobbering of unrelated faces), but the clipboard does not track which face it came from, so it can be pasted onto whichever face the player targets next, on the same fixture or a different one.

## Consequences

- The clipboard's storage model stays uniform across every `IWrenchConfigurable` implementor (`custom_data` keyed purely by block registry id) — the Sign Fixture needs no composite key, unlike option 2.
- A player who copies a face's Configuration and expects it to only ever apply back to "the same kind of face" (e.g. a horizontal-only Stance) can paste it onto an incompatible face. Paste applies the copied Stance unvalidated, even where it wouldn't currently be reachable via the Ratchet Wrench — acceptable since Sign Support Type gating (`CONTEXT.md`) is slated for removal, at which point every Stance will be valid on every face anyway.
- Only one Sign Fixture Configuration can be held in the clipboard at a time (copying a new face overwrites the previously stored one), same as every other block type.
