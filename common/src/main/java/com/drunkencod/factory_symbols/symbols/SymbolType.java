package com.drunkencod.factory_symbols.symbols;

public enum SymbolType {
    // #region Numbers
    NUM_0("number_0", SymbolCategory.NUMBER),
    NUM_1("number_1", SymbolCategory.NUMBER),
    NUM_2("number_2", SymbolCategory.NUMBER),
    NUM_3("number_3", SymbolCategory.NUMBER),
    NUM_4("number_4", SymbolCategory.NUMBER),
    NUM_5("number_5", SymbolCategory.NUMBER),
    NUM_6("number_6", SymbolCategory.NUMBER),
    NUM_7("number_7", SymbolCategory.NUMBER),
    NUM_8("number_8", SymbolCategory.NUMBER),
    NUM_9("number_9", SymbolCategory.NUMBER),
    // #region Letters
    LETTER_A("letter_a", SymbolCategory.LETTER),
    LETTER_B("letter_b", SymbolCategory.LETTER),
    LETTER_C("letter_c", SymbolCategory.LETTER),
    LETTER_D("letter_d", SymbolCategory.LETTER),
    LETTER_E("letter_e", SymbolCategory.LETTER),
    LETTER_F("letter_f", SymbolCategory.LETTER),
    LETTER_G("letter_g", SymbolCategory.LETTER),
    LETTER_H("letter_h", SymbolCategory.LETTER),
    LETTER_I("letter_i", SymbolCategory.LETTER),
    LETTER_J("letter_j", SymbolCategory.LETTER),
    LETTER_K("letter_k", SymbolCategory.LETTER),
    LETTER_L("letter_l", SymbolCategory.LETTER),
    LETTER_M("letter_m", SymbolCategory.LETTER),
    LETTER_N("letter_n", SymbolCategory.LETTER),
    LETTER_O("letter_o", SymbolCategory.LETTER),
    LETTER_P("letter_p", SymbolCategory.LETTER),
    LETTER_Q("letter_q", SymbolCategory.LETTER),
    LETTER_R("letter_r", SymbolCategory.LETTER),
    LETTER_S("letter_s", SymbolCategory.LETTER),
    LETTER_T("letter_t", SymbolCategory.LETTER),
    LETTER_U("letter_u", SymbolCategory.LETTER),
    LETTER_V("letter_v", SymbolCategory.LETTER),
    LETTER_W("letter_w", SymbolCategory.LETTER),
    LETTER_X("letter_x", SymbolCategory.LETTER),
    LETTER_Y("letter_y", SymbolCategory.LETTER),
    LETTER_Z("letter_z", SymbolCategory.LETTER),
    // #region instructions
    INSTRUCTION_INFO("instruction_info", SymbolCategory.INSTRUCTION),
    INSTRUCTION_CHECK("instruction_check", SymbolCategory.INSTRUCTION),
    INSTRUCTION_CROSS("instruction_cross", SymbolCategory.INSTRUCTION),
    INSTRUCTION_FORBIDDEN("instruction_forbidden", SymbolCategory.INSTRUCTION),
    INSTRUCTION_STOP("instruction_stop", SymbolCategory.INSTRUCTION),
    // #region arrows
    ARROW_UP("arrow_up", SymbolCategory.ARROW),
    ARROW_DOWN("arrow_down", SymbolCategory.ARROW),
    ARROW_LEFT("arrow_left", SymbolCategory.ARROW),
    ARROW_RIGHT("arrow_right", SymbolCategory.ARROW),
    ARROW_UP_LEFT("arrow_up_left", SymbolCategory.ARROW),
    ARROW_UP_RIGHT("arrow_up_right", SymbolCategory.ARROW),
    ARROW_DOWN_LEFT("arrow_down_left", SymbolCategory.ARROW),
    ARROW_DOWN_RIGHT("arrow_down_right", SymbolCategory.ARROW),
    ARROW_LEFT_RIGHT("arrow_left_right", SymbolCategory.ARROW),
    ARROW_UP_DOWN("arrow_up_down", SymbolCategory.ARROW),
    ARROW_CYCLE("arrow_cycle", SymbolCategory.ARROW),
    ARROW_INWARD("arrow_inward", SymbolCategory.ARROW),
    ARROW_OUTWARD("arrow_outward", SymbolCategory.ARROW);

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
