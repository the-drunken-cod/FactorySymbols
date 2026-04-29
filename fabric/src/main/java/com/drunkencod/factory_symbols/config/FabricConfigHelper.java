package com.drunkencod.factory_symbols.config;

import com.drunkencod.factory_symbols.Constants;
import com.drunkencod.factory_symbols.symbols.SymbolCategory;
import com.drunkencod.factory_symbols.symbols.SymbolMaterial;
import com.drunkencod.factory_symbols.symbols.SymbolType;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

public class FabricConfigHelper implements IConfigHelper {

    // #region Registration — call from FactorySymbolsMod during mod initialisation

    public void register() {
        AutoConfig.register(CommonConfigData.class, GsonConfigSerializer::new);
        AutoConfig.register(ServerConfigData.class, GsonConfigSerializer::new);
        AutoConfig.register(ClientConfigData.class, GsonConfigSerializer::new);
    }

    // #region IConfigHelper implementation

    @Override
    public boolean getExampleStartupBool() {
        return AutoConfig.getConfigHolder(CommonConfigData.class).getConfig().exampleStartupBool;
    }

    @Override
    public boolean getExampleServerBool() {
        return AutoConfig.getConfigHolder(ServerConfigData.class).getConfig().exampleServerBool;
    }

    @Override
    public boolean getExampleClientBool() {
        return AutoConfig.getConfigHolder(ClientConfigData.class).getConfig().exampleClientBool;
    }

    @Override
    public boolean isMaterialEnabled(SymbolMaterial material) {
        MaterialsSection mats = AutoConfig.getConfigHolder(CommonConfigData.class).getConfig().materials;
        return switch (material) {
            case PAPER -> mats.paper;
            case IRON -> mats.iron;
            case GOLD -> mats.gold;
            case COPPER -> mats.copper;
            case REDSTONE -> mats.redstone;
            case COAL -> mats.coal;
            case EMERALD -> mats.emerald;
            case DIAMOND -> mats.diamond;
            case AMETHYST -> mats.amethyst;
        };
    }

    @Override
    public boolean isCategoryEnabled(SymbolCategory category) {
        CategoriesSection cats = AutoConfig.getConfigHolder(CommonConfigData.class).getConfig().categories;
        return switch (category) {
            case NUMBERS -> cats.numbers;
            case LETTERS -> cats.letters;
            case INSTRUCTIVE -> cats.instructive;
            case MATHEMATICAL -> cats.mathematical;
            case WARNING -> cats.warning;
            case SCIENCE -> cats.science;
            case ENVIRONMENT -> cats.environment;
            case MISC -> cats.misc;
        };
    }

    @Override
    public boolean isSymbolEnabled(SymbolType symbol) {
        SymbolsSection syms = AutoConfig.getConfigHolder(CommonConfigData.class).getConfig().symbols;
        return switch (symbol) {
            case NUM_0 -> syms.num_0;
            case NUM_1 -> syms.num_1;
            case NUM_2 -> syms.num_2;
            case NUM_3 -> syms.num_3;
            case NUM_4 -> syms.num_4;
            case NUM_5 -> syms.num_5;
            case NUM_6 -> syms.num_6;
            case NUM_7 -> syms.num_7;
            case NUM_8 -> syms.num_8;
            case NUM_9 -> syms.num_9;
            case LETTER_A -> syms.letter_a;
            case LETTER_B -> syms.letter_b;
            case LETTER_C -> syms.letter_c;
            case LETTER_D -> syms.letter_d;
            case LETTER_E -> syms.letter_e;
            case LETTER_F -> syms.letter_f;
            case LETTER_G -> syms.letter_g;
            case LETTER_H -> syms.letter_h;
            case LETTER_I -> syms.letter_i;
            case LETTER_J -> syms.letter_j;
            case LETTER_K -> syms.letter_k;
            case LETTER_L -> syms.letter_l;
            case LETTER_M -> syms.letter_m;
            case LETTER_N -> syms.letter_n;
            case LETTER_O -> syms.letter_o;
            case LETTER_P -> syms.letter_p;
            case LETTER_Q -> syms.letter_q;
            case LETTER_R -> syms.letter_r;
            case LETTER_S -> syms.letter_s;
            case LETTER_T -> syms.letter_t;
            case LETTER_U -> syms.letter_u;
            case LETTER_V -> syms.letter_v;
            case LETTER_W -> syms.letter_w;
            case LETTER_X -> syms.letter_x;
            case LETTER_Y -> syms.letter_y;
            case LETTER_Z -> syms.letter_z;
        };
    }

    // #region Config data classes

    @Config(name = Constants.MOD_ID + "_common")
    public static class CommonConfigData implements ConfigData {
        public boolean exampleStartupBool = false;

        @ConfigEntry.Gui.CollapsibleObject
        public MaterialsSection materials = new MaterialsSection();

        @ConfigEntry.Gui.CollapsibleObject
        public CategoriesSection categories = new CategoriesSection();

        @ConfigEntry.Gui.CollapsibleObject
        public SymbolsSection symbols = new SymbolsSection();
    }

    @Config(name = Constants.MOD_ID + "_server")
    public static class ServerConfigData implements ConfigData {
        @ConfigEntry.Gui.Tooltip
        public boolean exampleServerBool = false;
    }

    @Config(name = Constants.MOD_ID + "_client")
    public static class ClientConfigData implements ConfigData {
        @ConfigEntry.Gui.Tooltip
        public boolean exampleClientBool = false;
    }

    public static class MaterialsSection {
        public boolean paper = true;
        public boolean iron = true;
        public boolean gold = true;
        public boolean copper = true;
        public boolean redstone = true;
        public boolean coal = true;
        public boolean emerald = true;
        public boolean diamond = true;
        public boolean amethyst = true;
    }

    public static class CategoriesSection {
        public boolean numbers = true;
        public boolean letters = true;
        public boolean instructive = true;
        public boolean mathematical = true;
        public boolean warning = true;
        public boolean science = true;
        public boolean environment = true;
        public boolean misc = true;
    }

    public static class SymbolsSection {
        public boolean num_0 = true;
        public boolean num_1 = true;
        public boolean num_2 = true;
        public boolean num_3 = true;
        public boolean num_4 = true;
        public boolean num_5 = true;
        public boolean num_6 = true;
        public boolean num_7 = true;
        public boolean num_8 = true;
        public boolean num_9 = true;
        public boolean letter_a = true;
        public boolean letter_b = true;
        public boolean letter_c = true;
        public boolean letter_d = true;
        public boolean letter_e = true;
        public boolean letter_f = true;
        public boolean letter_g = true;
        public boolean letter_h = true;
        public boolean letter_i = true;
        public boolean letter_j = true;
        public boolean letter_k = true;
        public boolean letter_l = true;
        public boolean letter_m = true;
        public boolean letter_n = true;
        public boolean letter_o = true;
        public boolean letter_p = true;
        public boolean letter_q = true;
        public boolean letter_r = true;
        public boolean letter_s = true;
        public boolean letter_t = true;
        public boolean letter_u = true;
        public boolean letter_v = true;
        public boolean letter_w = true;
        public boolean letter_x = true;
        public boolean letter_y = true;
        public boolean letter_z = true;
    }
}
