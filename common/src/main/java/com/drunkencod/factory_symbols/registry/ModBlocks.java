package com.drunkencod.factory_symbols.registry;

import com.drunkencod.factory_symbols.block.display_panel.DisplayPanelBlock;
import com.drunkencod.factory_symbols.block.display_panel.DisplayPanelBlockEntity;
import com.drunkencod.factory_symbols.block.display_panel.DisplayPanelItem;
import com.drunkencod.factory_symbols.block.sign_post.SignPostBlock;
import com.drunkencod.factory_symbols.block.sign_post.SignPostButtonFixtureBlock;
import com.drunkencod.factory_symbols.block.sign_post.SignPostButtonFixtureBlockEntity;
import com.drunkencod.factory_symbols.block.sign_post.SignPostLampFixtureBlock;
import com.drunkencod.factory_symbols.block.sign_post.SignPostLampFixtureBlockEntity;
import com.drunkencod.factory_symbols.platform.Services;
import com.drunkencod.factory_symbols.util.TooltipUtil;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;

import java.util.function.Supplier;

/**
 * Central block registry.
 */
public class ModBlocks {

    // #region Display Panel
    public static final Supplier<DisplayPanelBlock> DISPLAY_PANEL = Services.REGISTRY.registerBlock(
            "display_panel",
            () -> new DisplayPanelBlock(BlockBehaviour.Properties.of()
                    .strength(0.85f, 3.0f)
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

    // #region Sign Post
    public static final Supplier<SignPostBlock> SIGN_POST = Services.REGISTRY.registerBlock(
            "sign_post",
            () -> new SignPostBlock(BlockBehaviour.Properties.of()
                    .strength(0.85f, 2.0f)
                    .sound(SoundType.COPPER_GRATE)
                    .pushReaction(PushReaction.NORMAL)
                    .requiresCorrectToolForDrops()
                    .noOcclusion()));

    public static final Supplier<Item> SIGN_POST_ITEM = Services.REGISTRY.registerItem(
            "sign_post",
            () -> new BlockItem(SIGN_POST.get(), new Item.Properties()
                    .component(DataComponents.LORE,
                            TooltipUtil.getTooltip(
                                    "block.factory_symbols.sign_post.tooltip"))));

    public static void register() {
        // Triggers static field initialization
    }

    // #region Sign Post — Button Fixture
    public static final Supplier<SignPostButtonFixtureBlock> SIGN_POST_BUTTON_FIXTURE = Services.REGISTRY
            .registerBlock("sign_post_button_fixture",
                    () -> new SignPostButtonFixtureBlock(BlockBehaviour.Properties.of()
                            .strength(0.85f, 2.0f)
                            .sound(SoundType.COPPER_GRATE)
                            .pushReaction(PushReaction.NORMAL)
                            .requiresCorrectToolForDrops()
                            .noOcclusion()));

    public static final Supplier<Item> SIGN_POST_BUTTON_FIXTURE_ITEM = Services.REGISTRY.registerItem(
            "sign_post_button_fixture",
            () -> new BlockItem(SIGN_POST_BUTTON_FIXTURE.get(), new Item.Properties()
                    .component(DataComponents.LORE,
                            TooltipUtil.getTooltip("block.factory_symbols.sign_post_button_fixture.tooltip"))));

    public static final Supplier<BlockEntityType<SignPostButtonFixtureBlockEntity>> SIGN_POST_BUTTON_FIXTURE_BE_TYPE = Services.REGISTRY
            .registerBlockEntityType(
                    "sign_post_button_fixture", ModBlocks::makeButtonFixtureBEType);

    private static BlockEntityType<SignPostButtonFixtureBlockEntity> makeButtonFixtureBEType() {
        return BlockEntityType.Builder.of(SignPostButtonFixtureBlockEntity::new,
                SIGN_POST_BUTTON_FIXTURE.get()).build(null);
    }

    // #region Sign Post — Lamp Fixture
    public static final Supplier<SignPostLampFixtureBlock> SIGN_POST_LAMP_FIXTURE = Services.REGISTRY
            .registerBlock("sign_post_lamp_fixture",
                    () -> new SignPostLampFixtureBlock(BlockBehaviour.Properties.of()
                            .strength(0.85f, 2.0f)
                            .sound(SoundType.COPPER_GRATE)
                            .pushReaction(PushReaction.NORMAL)
                            .requiresCorrectToolForDrops()
                            .noOcclusion()
                            .lightLevel(state -> state.getValue(SignPostLampFixtureBlock.LIT) ? 15 : 0)));

    public static final Supplier<Item> SIGN_POST_LAMP_FIXTURE_ITEM = Services.REGISTRY.registerItem(
            "sign_post_lamp_fixture",
            () -> new BlockItem(SIGN_POST_LAMP_FIXTURE.get(), new Item.Properties()
                    .component(DataComponents.LORE,
                            TooltipUtil.getTooltip("block.factory_symbols.sign_post_lamp_fixture.tooltip"))));

    public static final Supplier<BlockEntityType<SignPostLampFixtureBlockEntity>> SIGN_POST_LAMP_FIXTURE_BE_TYPE = Services.REGISTRY
            .registerBlockEntityType("sign_post_lamp_fixture", ModBlocks::makeLampFixtureBEType);

    private static BlockEntityType<SignPostLampFixtureBlockEntity> makeLampFixtureBEType() {
        return BlockEntityType.Builder.of(SignPostLampFixtureBlockEntity::new,
                SIGN_POST_LAMP_FIXTURE.get()).build(null);
    }
}
