package com.drunkencod.factory_symbols.config;

import com.drunkencod.factory_symbols.symbols.SymbolCategory;
import com.drunkencod.factory_symbols.symbols.SymbolMaterial;
import com.drunkencod.factory_symbols.symbols.SymbolType;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.EnumMap;
import java.util.Map;

public class NeoForgeConfigHelper implements IConfigHelper {

    // #region Common (startup) config

    public static final CommonConfig COMMON;
    private static final ModConfigSpec COMMON_SPEC;

    static {
        Pair<CommonConfig, ModConfigSpec> specPair = new ModConfigSpec.Builder()
                .configure(CommonConfig::new);
        COMMON = specPair.getLeft();
        COMMON_SPEC = specPair.getRight();
    }

    // #region Server config

    public static final ServerConfig SERVER;
    private static final ModConfigSpec SERVER_SPEC;

    static {
        Pair<ServerConfig, ModConfigSpec> specPair = new ModConfigSpec.Builder()
                .configure(ServerConfig::new);
        SERVER = specPair.getLeft();
        SERVER_SPEC = specPair.getRight();
    }

    // #region Client config

    public static final ClientConfig CLIENT;
    private static final ModConfigSpec CLIENT_SPEC;

    static {
        Pair<ClientConfig, ModConfigSpec> specPair = new ModConfigSpec.Builder()
                .configure(ClientConfig::new);
        CLIENT = specPair.getLeft();
        CLIENT_SPEC = specPair.getRight();
    }

    // #region Registration — call from FactorySymbolsMod constructor

    public void register(ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, COMMON_SPEC);
        modContainer.registerConfig(ModConfig.Type.SERVER, SERVER_SPEC);
        modContainer.registerConfig(ModConfig.Type.CLIENT, CLIENT_SPEC);
    }

    // #region IConfigHelper implementation

    @Override
    public boolean getExampleStartupBool() {
        return COMMON.exampleStartupBool.get();
    }

    @Override
    public boolean getExampleServerBool() {
        return SERVER.exampleServerBool.get();
    }

    @Override
    public boolean getExampleClientBool() {
        return CLIENT.exampleClientBool.get();
    }

    @Override
    public boolean isMaterialEnabled(SymbolMaterial material) {
        return COMMON.materialEnabled.get(material).get();
    }

    @Override
    public boolean isCategoryEnabled(SymbolCategory category) {
        return COMMON.categoryEnabled.get(category).get();
    }

    @Override
    public boolean isSymbolEnabled(SymbolType symbol) {
        return COMMON.symbolEnabled.get(symbol).get();
    }

    // #region Inner config classes

    public static class CommonConfig {
        public final ModConfigSpec.BooleanValue exampleStartupBool;

        public final Map<SymbolMaterial, ModConfigSpec.BooleanValue> materialEnabled = new EnumMap<>(
                SymbolMaterial.class);
        public final Map<SymbolCategory, ModConfigSpec.BooleanValue> categoryEnabled = new EnumMap<>(
                SymbolCategory.class);
        public final Map<SymbolType, ModConfigSpec.BooleanValue> symbolEnabled = new EnumMap<>(SymbolType.class);

        CommonConfig(ModConfigSpec.Builder builder) {
            exampleStartupBool = builder
                    .comment("Example common (startup) config boolean")
                    .define("exampleStartupBool", false);

            builder.push("materials");
            for (SymbolMaterial mat : SymbolMaterial.values()) {
                builder.push(mat.getPrefix());
                materialEnabled.put(mat, builder
                        .comment("Enable items and stonecutter recipes for the " + mat.getPrefix() + " material")
                        .define("enabled", true));
                builder.pop();
            }
            builder.pop();

            builder.push("categories");
            for (SymbolCategory cat : SymbolCategory.values()) {
                builder.push(cat.getId());
                categoryEnabled.put(cat, builder
                        .comment("Enable stonecutter recipes for all symbols in the " + cat.getId() + " category")
                        .define("enabled", true));
                builder.pop();
            }
            builder.pop();

            builder.push("symbols");
            for (SymbolType sym : SymbolType.values()) {
                builder.push(sym.name().toLowerCase());
                symbolEnabled.put(sym, builder
                        .comment("Enable the stonecutter recipe for symbol \"" + sym.getId() + "\"")
                        .define("enabled", true));
                builder.pop();
            }
            builder.pop();
        }
    }

    public static class ServerConfig {
        public final ModConfigSpec.BooleanValue exampleServerBool;

        ServerConfig(ModConfigSpec.Builder builder) {
            exampleServerBool = builder
                    .comment("Example server config boolean")
                    .define("exampleServerBool", false);
        }
    }

    public static class ClientConfig {
        public final ModConfigSpec.BooleanValue exampleClientBool;

        ClientConfig(ModConfigSpec.Builder builder) {
            exampleClientBool = builder
                    .comment("Example client config boolean")
                    .define("exampleClientBool", false);
        }
    }
}
