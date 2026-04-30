package com.drunkencod.factory_symbols.symbols;

public enum SymbolShape {
    // #region Entries
    SQUARE("square"),
    TRIANGLE("triangle");

    private final String id;

    SymbolShape(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
