package com.drunkencod.factory_symbols.config;

import com.drunkencod.factory_symbols.Constants;
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
    public int signPostRelayMaxDepth() {
        return AutoConfig.getConfigHolder(ServerConfigData.class).getConfig().signPostRelayMaxDepth;
    }

    @Override
    public boolean displayPanelShiftRenderedItem() {
        return AutoConfig.getConfigHolder(ClientConfigData.class).getConfig().displayPanelShiftRenderedItem;
    }

    // #region Config data classes

    @Config(name = Constants.MOD_ID + "_common")
    public static class CommonConfigData implements ConfigData {
        // public boolean exampleStartupBool = false;
    }

    @Config(name = Constants.MOD_ID + "_server")
    public static class ServerConfigData implements ConfigData {
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 64)
        public int signPostRelayMaxDepth = 15;
    }

    @Config(name = Constants.MOD_ID + "_client")
    public static class ClientConfigData implements ConfigData {
        @ConfigEntry.Gui.Tooltip
        public boolean displayPanelShiftRenderedItem = true;
    }

}
