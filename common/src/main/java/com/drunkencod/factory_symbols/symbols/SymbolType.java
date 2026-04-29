package com.drunkencod.factory_symbols.symbols;

public enum SymbolType {
    // #region Numbers
    NUM_0("0", SymbolCategory.NUMBERS),
    NUM_1("1", SymbolCategory.NUMBERS),
    NUM_2("2", SymbolCategory.NUMBERS),
    NUM_3("3", SymbolCategory.NUMBERS),
    NUM_4("4", SymbolCategory.NUMBERS),
    NUM_5("5", SymbolCategory.NUMBERS),
    NUM_6("6", SymbolCategory.NUMBERS),
    NUM_7("7", SymbolCategory.NUMBERS),
    NUM_8("8", SymbolCategory.NUMBERS),
    NUM_9("9", SymbolCategory.NUMBERS),
    // #region Letters
    LETTER_A("letter_a", SymbolCategory.LETTERS),
    LETTER_B("letter_b", SymbolCategory.LETTERS),
    LETTER_C("letter_c", SymbolCategory.LETTERS),
    LETTER_D("letter_d", SymbolCategory.LETTERS),
    LETTER_E("letter_e", SymbolCategory.LETTERS),
    LETTER_F("letter_f", SymbolCategory.LETTERS),
    LETTER_G("letter_g", SymbolCategory.LETTERS),
    LETTER_H("letter_h", SymbolCategory.LETTERS),
    LETTER_I("letter_i", SymbolCategory.LETTERS),
    LETTER_J("letter_j", SymbolCategory.LETTERS),
    LETTER_K("letter_k", SymbolCategory.LETTERS),
    LETTER_L("letter_l", SymbolCategory.LETTERS),
    LETTER_M("letter_m", SymbolCategory.LETTERS),
    LETTER_N("letter_n", SymbolCategory.LETTERS),
    LETTER_O("letter_o", SymbolCategory.LETTERS),
    LETTER_P("letter_p", SymbolCategory.LETTERS),
    LETTER_Q("letter_q", SymbolCategory.LETTERS),
    LETTER_R("letter_r", SymbolCategory.LETTERS),
    LETTER_S("letter_s", SymbolCategory.LETTERS),
    LETTER_T("letter_t", SymbolCategory.LETTERS),
    LETTER_U("letter_u", SymbolCategory.LETTERS),
    LETTER_V("letter_v", SymbolCategory.LETTERS),
    LETTER_W("letter_w", SymbolCategory.LETTERS),
    LETTER_X("letter_x", SymbolCategory.LETTERS),
    LETTER_Y("letter_y", SymbolCategory.LETTERS),
    LETTER_Z("letter_z", SymbolCategory.LETTERS);

    private final String id;
    private final SymbolCategory category;

    SymbolType(String id, SymbolCategory category) {
        this.id = id;
        this.category = category;
    }

    /** Registry path segment, e.g. {@code "0"} or {@code "letter_a"}. */
    public String getId() {
        return id;
    }

    public SymbolCategory getCategory() {
        return category;
    }
}
