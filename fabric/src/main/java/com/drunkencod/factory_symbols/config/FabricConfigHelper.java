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

import java.util.Map;

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
            case IRON -> mats.iron;
            case GOLD -> mats.gold;
            case REDSTONE -> mats.redstone;
            case COAL -> mats.coal;
            case EMERALD -> mats.emerald;
            case LAPIS -> mats.lapis;
        };
    }

    @Override
    public boolean isCategoryEnabled(SymbolCategory category) {
        CategoriesSection cats = AutoConfig.getConfigHolder(CommonConfigData.class).getConfig().categories;
        return switch (category) {
            case NUMBER -> cats.numbers;
            case LETTER -> cats.letters;
            case INSTRUCTION -> cats.instruction;
            case ARROW -> cats.arrow;
            case MATHEMATICAL -> cats.mathematical;
            case WARNING -> cats.warning;
            case SCIENCE -> cats.science;
            case ENVIRONMENT -> cats.environment;
            case MISC -> cats.misc;
        };
    }

    @Override
    public boolean isSymbolEnabled(SymbolType symbol) {
        Map<String, Boolean> syms = AutoConfig.getConfigHolder(CommonConfigData.class).getConfig().symbols;
        return syms.getOrDefault(symbol.getId(), true);
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
        public Map<String, Boolean> symbols = defaultSymbols();

        private static Map<String, Boolean> defaultSymbols() {
            Map<String, Boolean> map = new java.util.LinkedHashMap<>();
            for (SymbolType sym : SymbolType.values()) {
                map.put(sym.getId(), true);
            }
            return map;
        }
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
        public boolean iron = true;
        public boolean gold = true;
        public boolean redstone = true;
        public boolean coal = true;
        public boolean emerald = true;
        public boolean lapis = true;
    }

    public static class CategoriesSection {
        public boolean numbers = true;
        public boolean letters = true;
        public boolean instruction = true;
        public boolean arrow = true;
        public boolean mathematical = true;
        public boolean warning = true;
        public boolean science = true;
        public boolean environment = true;
        public boolean misc = true;
    }
}
