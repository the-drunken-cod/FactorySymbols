# 0001 - Generalize AbstractSignPostFixtureBlock to a multi-face model

## Status

Accepted

## Context

Every existing Sign Post Fixture (Button, Lamp, Redstone Emitter) is built around a single `FACING` blockstate property — one fixture instance occupies exactly one face of the post. The Sign Fixture (see `sign_post_sign_fixture.md`) needs to occupy up to all 6 faces of a single post simultaneously, each with independent state (contained sign item, Stance, Rotation, Scale, double-sided, Bright).

Three options were considered:
1. Keep Sign Fixture as a standalone block class, sharing only behavior (POWERED passthrough, connection-blocking, wrench-harvest) via a new interface — leaving `AbstractSignPostFixtureBlock` untouched.
2. Force-fit Sign Fixture onto `AbstractSignPostFixtureBlock` despite its single-`FACING` assumption, overriding methods to handle 6 faces.
3. Generalize `AbstractSignPostFixtureBlock` itself to track an arbitrary set of occupied faces, and migrate Button/Lamp/Redstone Emitter Fixtures onto the generalized model even though they only ever populate one face.

## Decision

Option 3. `AbstractSignPostFixtureBlock` is refactored to track occupancy per-direction (see ADR for occupancy storage below) rather than a single `FACING`. Button, Lamp, and Redstone Emitter Fixtures continue to only ever occupy one face in practice, but they now go through the same generalized occupancy/connection-blocking/POWERED-passthrough code path as the Sign Fixture.

## Consequences

- A future reader will find multi-face plumbing (occupied-face sets, per-face blockstate booleans) in fixtures that conceptually only ever use one face. This is intentional: it keeps one code path for connection-blocking and POWERED passthrough instead of two parallel implementations.
- Button/Lamp/Redstone Emitter Fixture blockstates change shape (their single `FACING` property is replaced by the generalized per-direction occupancy properties), which is a breaking save-format change for any world with these fixtures already placed — acceptable pre-1.0.0.
- Adding a future fixture type that only ever uses one face costs nothing extra; one that needs multiple faces (like Sign Fixture) is no longer a special case.
