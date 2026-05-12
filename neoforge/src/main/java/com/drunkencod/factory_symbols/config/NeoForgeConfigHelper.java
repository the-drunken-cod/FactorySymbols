package com.drunkencod.factory_symbols.config;

import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

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
    public int signPostRelayMaxDepth() {
        return SERVER.signPostRelayMaxDepth.get();
    }

    @Override
    public boolean displayPanelShiftRenderedItem() {
        return CLIENT.displayPanelShiftRenderedItem.get();
    }

    // #region Inner config classes

    public static class CommonConfig {
        // public final ModConfigSpec.BooleanValue exampleStartupBool;

        CommonConfig(ModConfigSpec.Builder builder) {
            // exampleStartupBool = builder
            // .comment("Example common (startup) config boolean")
            // .define("exampleStartupBool", false);

        }
    }

    public static class ServerConfig {
        public final ModConfigSpec.IntValue signPostRelayMaxDepth;

        ServerConfig(ModConfigSpec.Builder builder) {
            signPostRelayMaxDepth = builder
                    .comment("Maximum number of sign post hops a block-update relay signal propagates")
                    .defineInRange("signPostRelayMaxDepth", 15, 1, 64);
        }
    }

    public static class ClientConfig {
        public final ModConfigSpec.BooleanValue displayPanelShiftRenderedItem;

        ClientConfig(ModConfigSpec.Builder builder) {
            displayPanelShiftRenderedItem = builder
                    .comment("Whether to slightly shift the rendered item on the display panel to prevent z-fighting")
                    .define("displayPanelShiftRenderedItem", false);
        }
    }
}
