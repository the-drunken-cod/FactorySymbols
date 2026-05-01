package com.drunkencod.factory_symbols.registry;

import com.drunkencod.factory_symbols.block.DisplayPanelBlock;
import com.drunkencod.factory_symbols.block.DisplayPanelBlockEntity;
import com.drunkencod.factory_symbols.block.DisplayPanelItem;
import com.drunkencod.factory_symbols.platform.Services;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Supplier;

/**
 * Central block registry.
 */
public class ModBlocks {

    // #region Display Panel
    public static final Supplier<DisplayPanelBlock> DISPLAY_PANEL = Services.REGISTRY.registerBlock(
            "display_panel",
            () -> new DisplayPanelBlock(BlockBehaviour.Properties.of()
                    .strength(1.0f, 3.0f)
                    .sound(SoundType.METAL)
                    .noOcclusion()));

    public static final Supplier<Item> DISPLAY_PANEL_ITEM = Services.REGISTRY.registerItem(
            "display_panel",
            () -> new DisplayPanelItem(DISPLAY_PANEL.get(), new Item.Properties()));

    public static final Supplier<BlockEntityType<DisplayPanelBlockEntity>> DISPLAY_PANEL_BE_TYPE = Services.REGISTRY
            .registerBlockEntityType("display_panel", ModBlocks::makeDisplayPanelBEType);

    private static BlockEntityType<DisplayPanelBlockEntity> makeDisplayPanelBEType() {
        return BlockEntityType.Builder.of(DisplayPanelBlockEntity::new, DISPLAY_PANEL.get()).build(null);
    }

    public static void register() {
        // Triggers static field initialization
    }
}
