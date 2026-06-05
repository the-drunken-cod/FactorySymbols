package com.drunkencod.symbols_n_signs.symbols;

public enum SymbolShape {
    // #region Entries
    SQUARE("square"),
    TRIANGLE("triangle"),
    AMOGUS("amogus");

    private final String id;

    SymbolShape(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
