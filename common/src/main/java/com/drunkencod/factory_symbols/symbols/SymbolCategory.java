package com.drunkencod.factory_symbols.symbols;

public enum SymbolCategory {
    NUMBERS("numbers"),
    LETTERS("letters"),
    INSTRUCTIVE("instructive"),
    MATHEMATICAL("mathematical"),
    WARNING("warning"),
    SCIENCE("science"),
    ENVIRONMENT("environment"),
    MISC("misc");

    private final String id;

    SymbolCategory(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
