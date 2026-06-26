package com.drunkencod.symbols_n_signs.block.sign_post;

import com.drunkencod.symbols_n_signs.signs.SignStance;
import com.mojang.math.Axis;

import net.minecraft.core.Direction;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;

/**
 * Shared transform math for a Sign Fixture face's pane(s), used identically
 * by the renderer (visual quads) and
 * {@link SignPostSignFixtureBlock#getShape} (collision shape), so the two
 * never drift apart. Pure JOML math with no client-only dependency, since the
 * collision shape is also computed server-side.
 */
public final class SignFixtureGeometry {

    /**
     * Distance the stance/rotation/scale pivot sits outward from the post's face
     * plane.
     */
    public static final float ATTACH_OFFSET = 2.5f / 16f;
    /**
     * Half the gap between the front and back pane, along the pane's local normal.
     */
    public static final float PANE_HALF_GAP = 0.01f;
    /** Unscaled (scale = 1.0) world-space edge length of a pane. */
    public static final float BASE_PANE_SIZE = 1f;

    private SignFixtureGeometry() {
    }

    /**
     * Local-to-block transform for the pivot shared by Stance, Rotation and
     * Scale: block center -> face attachment point -> stance tilt -> in-plane
     * spin. Rotation and Scale are applied on top of this in
     * {@link #buildPaneTransform}, so they share the same pivot as Stance.
     */
    public static Matrix4f buildPivotTransform(Direction face, SignFixtureFaceData data) {
        SignStance stance = data.getStance();
        int rotation = data.getRotation();
        float offset = data.getOffset();

        Matrix4f m = new Matrix4f();
        m.translate(0.5f, 0.5f, 0.5f);
        m.rotate(faceOrientation(face));
        m.translate(0f, 0f, ATTACH_OFFSET + offset);
        m.rotate(stanceRotation(face, stance));
        m.rotate(Axis.ZP.rotationDegrees(rotation * 45f));
        return m;
    }

    /**
     * Local-to-block transform for one of the two panes of a face.
     *
     * @param signSide +1 for the forward-facing pane, -1 for the backward-facing
     *                 one
     */
    public static Matrix4f buildPaneTransform(Matrix4f pivot, float scale, int signSide) {
        Matrix4f m = new Matrix4f(pivot);
        m.translate(0f, 0f, signSide * PANE_HALF_GAP);
        m.scale(scale * BASE_PANE_SIZE, scale * BASE_PANE_SIZE, 1f);
        return m;
    }

    /**
     * The 4 corners of a unit quad centered on the origin, in the pane's local XY
     * plane.
     */
    public static Vector3f[] unitQuadCorners() {
        return new Vector3f[] {
                new Vector3f(-0.5f, -0.5f, 0f),
                new Vector3f(0.5f, -0.5f, 0f),
                new Vector3f(0.5f, 0.5f, 0f),
                new Vector3f(-0.5f, 0.5f, 0f),
        };
    }

    public static Vector3f transform(Matrix4f m, Vector3f local) {
        Vector4f v = new Vector4f(local.x(), local.y(), local.z(), 1f);
        m.transform(v);
        return new Vector3f(v.x(), v.y(), v.z());
    }

    /** Rotation that points local +Z along the given face's outward normal. */
    private static Quaternionf faceOrientation(Direction face) {
        return switch (face) {
            case SOUTH -> Axis.YP.rotationDegrees(0f);
            case NORTH -> Axis.YP.rotationDegrees(180f);
            case EAST -> Axis.YP.rotationDegrees(90f);
            case WEST -> Axis.YP.rotationDegrees(270f);
            case UP -> Axis.XP.rotationDegrees(-90f);
            case DOWN -> Axis.XP.rotationDegrees(90f);
        };
    }

    /**
     * Tilt applied at the attachment point to swing the pane from flat
     * (against the post) to perpendicular (sticking out), expressed in the
     * face-local frame established by {@link #faceOrientation}.
     * <p>
     * First-pass orientation pending visual confirmation in-game; the exact
     * compass mapping of PERP_A..D may need adjusting once rendered.
     */
    private static Quaternionf stanceRotation(Direction face, SignStance stance) {
        boolean vertical = !face.getAxis().isHorizontal();
        return switch (stance) {
            case FLAT -> Axis.YP.rotationDegrees(0f);
            case PERP_A -> vertical ? Axis.XP.rotationDegrees(90f) : Axis.YP.rotationDegrees(90f);
            case PERP_B -> vertical ? Axis.XP.rotationDegrees(-90f) : Axis.YP.rotationDegrees(-90f);
            case PERP_C -> Axis.ZP.rotationDegrees(90f);
            case PERP_D -> Axis.ZP.rotationDegrees(-90f);
        };
    }
}
