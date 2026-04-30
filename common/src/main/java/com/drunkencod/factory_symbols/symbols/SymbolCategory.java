package com.drunkencod.factory_symbols.symbols;

public enum SymbolCategory {
    NUMBER("number"),
    LETTER("letter"),
    INSTRUCTION("instruction"),
    ARROW("arrow"),
    MATHEMATICAL("mathematical"), // TODO
    WARNING("warning"),
    SCIENCE("science"), // TODO
    ENVIRONMENT("environment"), // TODO
    MISC("misc"); // TODO

    private final String id;

    SymbolCategory(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
