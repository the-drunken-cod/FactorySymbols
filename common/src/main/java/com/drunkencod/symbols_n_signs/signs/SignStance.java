package com.drunkencod.symbols_n_signs.signs;

import net.minecraft.core.Direction;

import java.util.ArrayList;
import java.util.List;

/**
 * How a sign is mounted within its Sign Fixture face slot: flat against the
 * post, or sticking out perpendicular to it. Distinct from Rotation (the 0-7
 * spin of the sign within its mount).
 * <p>
 * On a horizontal post face (N/E/S/W side), only {@link #FLAT}, {@link #PERP_A}
 * and {@link #PERP_B} are ever valid (the two "wing" directions). On a vertical
 * post face (UP/DOWN), all 5 values are potentially valid (4 cardinal
 * perpendicular directions). See {@link #getAvailableStances} for the exact
 * gating rules, resolved per SignSupportType in
 * {@code docs/todo/sign_post_sign_fixture.md}.
 */
public enum SignStance {
    FLAT,
    PERP_A,
    PERP_B,
    PERP_C,
    PERP_D;

    /**
     * Returns the Stance values valid for a sign with the given support type, on
     * the given face of a Sign Fixture.
     * <p>
     * Every type implicitly includes BACK support, so FLAT is always valid
     * everywhere. HORIZONTAL/ANY unlock the 2 horizontal-face perpendicular
     * stances; VERTICAL/ANY unlock all 4 vertical-face perpendicular stances;
     * BOTTOM unlocks them only when the fixture occupies the post's UP face
     * specifically (a BOTTOM-support sign can stand on top of a post but can't
     * hang below one).
     */
    public static List<SignStance> getAvailableStances(SignSupportType supportType, Direction face) {
        List<SignStance> stances = new ArrayList<>();
        stances.add(FLAT);

        boolean horizontalFace = face.getAxis().isHorizontal();
        if (horizontalFace) {
            if (supportType == SignSupportType.HORIZONTAL || supportType == SignSupportType.ANY) {
                stances.add(PERP_A);
                stances.add(PERP_B);
            }
        } else {
            boolean unlocksVertical = supportType == SignSupportType.VERTICAL || supportType == SignSupportType.ANY
                    || (supportType == SignSupportType.BOTTOM && face == Direction.UP);
            if (unlocksVertical) {
                stances.add(PERP_A);
                stances.add(PERP_B);
                stances.add(PERP_C);
                stances.add(PERP_D);
            }
        }
        return stances;
    }
}
