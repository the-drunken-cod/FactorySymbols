package com.drunkencod.symbols_n_signs.signs;

public enum SignCategory {
    HAZARD("hazard"),
    REGULATORY("regulatory"),
    PROHIBITION("prohibition"),
    EXTRA("extra");

    private final String id;

    SignCategory(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
