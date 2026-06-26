package com.drunkencod.symbols_n_signs.block.sign_post;

import com.drunkencod.symbols_n_signs.signs.SignStance;
import com.mojang.math.Axis;

import net.minecraft.core.Direction;

import org.joml.Matrix4f;
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
    public static final float ATTACH_OFFSET = -(5.25f / 16f);
    /**
     * Half the gap between the front and back pane, along the pane's local normal.
     */
    public static final float PANE_HALF_GAP = 0.01f;
    /** Unscaled (scale = 1.0) world-space edge length of a pane. */
    public static final float BASE_PANE_SIZE = 1f;

    private static final Vector3f WORLD_UP = new Vector3f(0f, 1f, 0f);
    private static final Vector3f WORLD_NORTH = new Vector3f(0f, 0f, -1f);

    private SignFixtureGeometry() {
    }

    /**
     * Local-to-block transform for the pivot shared by Stance, Rotation and
     * Scale. The pane's orientation is built directly from explicit
     * normal/up/right vectors (rather than chained single-axis Euler
     * rotations), so perpendicular Stances are unambiguous regardless of
     * which post face they're mounted on.
     */
    public static Matrix4f buildPivotTransform(Direction face, SignFixtureFaceData data) {
        SignStance stance = data.getStance();
        int rotation = data.getRotation();
        float offset = data.getOffset();

        Vector3f faceNormal = toVector(face);
        Vector3f normal = resolveNormal(face, stance);
        Vector3f up = resolveUp(face, stance);
        Vector3f right = new Vector3f(up).cross(normal).normalize();
        Vector3f trueUp = new Vector3f(normal).cross(right).normalize();

        if (face == Direction.UP || face == Direction.DOWN)
            offset -= 1.85f / 16f;

        float along = ATTACH_OFFSET + offset;
        if (stance != SignStance.FLAT)
            // One of the pane's two in-plane axes always ends up parallel to the
            // original face normal once swung perpendicular, so pushing the pivot
            // out by half the (scaled) pane size moves its near edge to the
            // attachment point instead of burying half the pane in the post.
            along += data.getScale() * BASE_PANE_SIZE / 2f;

        // +0.5f first reaches the face's outer surface from the block center;
        // `along` is then the (small) clearance past that surface.
        Vector3f pivot = new Vector3f(0.5f, 0.5f, 0.5f).add(new Vector3f(faceNormal).mul(0.5f + along));

        Matrix4f m = new Matrix4f();
        m.translate(pivot);
        m.setColumn(0, new Vector4f(right, 0f));
        m.setColumn(1, new Vector4f(trueUp, 0f));
        m.setColumn(2, new Vector4f(normal, 0f));
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

    private static Vector3f toVector(Direction dir) {
        return new Vector3f(dir.getStepX(), dir.getStepY(), dir.getStepZ());
    }

    /**
     * World-space direction the pane's face points for the given mounting face
     * and Stance.
     * <p>
     * Horizontal faces (N/E/S/W): perpendicular Stances swing the pane to
     * point along the wall, to one of the two "wing" directions either side of
     * the mounting face.
     * <p>
     * Vertical faces (UP/DOWN): perpendicular Stances swing the pane to point
     * toward one of the 4 horizontal cardinal directions.
     */
    private static Vector3f resolveNormal(Direction face, SignStance stance) {
        if (stance == SignStance.FLAT)
            return toVector(face);
        boolean vertical = !face.getAxis().isHorizontal();
        if (!vertical) {
            Vector3f flatRight = new Vector3f(WORLD_UP).cross(toVector(face)).normalize();
            return stance == SignStance.PERP_A ? flatRight : flatRight.negate();
        }
        return switch (stance) {
            case PERP_A -> new Vector3f(WORLD_NORTH);
            case PERP_B -> new Vector3f(WORLD_NORTH).negate();
            case PERP_C -> new Vector3f(1f, 0f, 0f);
            case PERP_D -> new Vector3f(-1f, 0f, 0f);
            default -> toVector(face);
        };
    }

    /**
     * World-space reference for the pane's in-plane "up" axis. Always world-up
     * except when lying flat against a vertical (UP/DOWN) face, where
     * world-up would be parallel to the normal and therefore useless as a
     * basis vector.
     */
    private static Vector3f resolveUp(Direction face, SignStance stance) {
        boolean vertical = !face.getAxis().isHorizontal();
        if (stance == SignStance.FLAT && vertical)
            return new Vector3f(WORLD_NORTH);
        return new Vector3f(WORLD_UP);
    }
}
