package com.drunkencod.factory_symbols.signs;

public enum SignSupportType {
    BOTTOM("bottom"),
    VERTICAL("vertical"),
    HORIZONTAL("horizontal"),
    ANY("any"),
    CENTER_ONLY("center_only");

    private final String id;

    SignSupportType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
