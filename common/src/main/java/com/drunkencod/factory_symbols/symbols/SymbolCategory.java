package com.drunkencod.factory_symbols.symbols;

public enum SymbolCategory {
    NUMBER("number"),
    LETTER("letter"),
    INSTRUCTION("instruction"),
    ARROW("arrow"),
    MATHEMATICAL("mathematical"), // TODO
    WARNING("warning", SymbolShape.TRIANGLE),
    SCIENCE("science"), // TODO
    ENVIRONMENT("environment"), // TODO
    MISC("misc"); // TODO

    private final String id;
    private final SymbolShape shape;

    SymbolCategory(String id) {
        this(id, SymbolShape.SQUARE);
    }

    SymbolCategory(String id, SymbolShape shape) {
        this.id = id;
        this.shape = shape;
    }

    public String getId() {
        return id;
    }

    public SymbolShape getShape() {
        return shape;
    }
}
