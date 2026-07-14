# 0004 - Configuration Clipboard never duplicates or discards items

## Status

Accepted

## Context

A Sign Fixture face's Configuration includes the actual sign/symbol item occupying it (full `ItemStack`, including future custom NBT/components). If the clipboard's copy/paste stored and replayed that item verbatim, pasting the same clipboard onto many Sign Fixtures would recreate that item out of thin air on each one — free item duplication.

Two options were considered:
1. Store only the settings (Stance, Rotation, Scale, Offset, double-sided mode, Bright), never the item. Paste always leaves the target face's item slot exactly as it was.
2. Store the settings plus a reference to the copied item (its type + components, not the ItemStack instance itself). At paste time, search the pasting player's inventory for an exact match (`ItemStack.isSameItemSameComponents`) in their main inventory and hotbar; if found, move that stack's item into the target face (only if the face is currently empty); if not found, or the face is already occupied, fall back to settings-only.

## Decision

Option 2. The clipboard remembers which item occupied the copied face, and paste will move a matching item out of the pasting player's own inventory into an empty target face, but never fabricates, swaps out, or discards an item.

## Consequences

- Pasting a Sign Fixture Configuration can silently consume an item from the player's inventory (any exact match in main inventory or hotbar), which is easy to miss if the player wasn't expecting an item to move.
- If the target face is already occupied, or no matching item exists in the player's inventory, paste degrades gracefully to settings-only — this must not be treated as an error case in implementation, since it's the expected common outcome (e.g. pasting a Configuration whose original item was one-of-a-kind).
- This principle generalizes to any future `IWrenchConfigurable` implementor that stores an item as part of its Configuration: clipboard paste may only move items the player already owns, never create or destroy one.
