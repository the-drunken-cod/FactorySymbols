package com.drunkencod.factory_symbols.symbols;

public enum SymbolCategory {
    LETTER("letter"),
    NUMBER("number"),
    INSTRUCTION("instruction"),
    ARROW("arrow"),
    SCIENCE("science"),
    MATH("math"),
    ENVIRONMENT("environment"),
    MISC("misc"), // TODO
    WARNING("warning", SymbolShape.TRIANGLE);

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
