# 0002 - IWrenchConfigurable methods receive the clicked Direction

## Status

Accepted

## Context

The Sign Fixture can hold an independent configuration (Stance, Rotation, Scale, double-sided, Bright) per occupied face. The Ratchet Wrench needs to know which face the player is configuring. `RatchetWrenchItem.useOn()` already has access to the clicked face via `UseOnContext.getClickedFace()`, but `IWrenchConfigurable`'s methods (`getWrenchModeCount`, `getWrenchModeKey`, `onWrenchLeftClick`, `onWrenchRightClick`, etc.) only received a `BlockState`, with no notion of which face was targeted.

Two options were considered:
1. Add a dedicated "select active face" wrench mode to the Sign Fixture only: mode 0 cycles through occupied faces on right-click, and all subsequent modes apply to whichever face was last selected this way, independent of where the player physically clicks afterward.
2. Extend `IWrenchConfigurable`'s methods to take the clicked `Direction` directly, so the face being configured is always the literal face under the player's crosshair. Single-face fixtures (Button, Lamp, Redstone Emitter) ignore the parameter.

## Decision

Option 2. All `IWrenchConfigurable` methods gain a `Direction clickedFace` parameter. The wrench's per-block mode storage (in the wrench item's `CUSTOM_DATA`) is keyed by `blockId:faceName` instead of just `blockId`, so each face's selected mode is tracked independently.

## Consequences

- Configuring a face is consistent with how signs are placed in the first place (you click the face you mean), and requires no extra persistent "active face" state on either the wrench or the fixture.
- Every existing `IWrenchConfigurable` implementation (Button, Lamp, Redstone Emitter Fixtures) needs updating to accept and ignore the new parameter.
- Mode-storage keys change format (`blockId` -> `blockId:faceName`), which invalidates any previously-saved selected-mode state in existing Ratchet Wrench items — acceptable pre-1.0.0.
