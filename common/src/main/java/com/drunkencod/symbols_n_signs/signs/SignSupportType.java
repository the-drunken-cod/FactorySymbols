package com.drunkencod.symbols_n_signs.signs;

public enum SignSupportType {
    /** Sign texture can only be supported at the bottom edge and back. */
    BOTTOM("bottom"),
    /** Sign texture can only be supported at the top or bottom edge and back. */
    VERTICAL("vertical"),
    /** Sign texture can only be supported at the left or right edge and back. */
    HORIZONTAL("horizontal"),
    /** Sign texture can be supported at the top, bottom, left, right and back. */
    ANY("any"),
    /** Sign texture can only be supported at the back. */
    BACK("back");

    private final String id;

    SignSupportType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
