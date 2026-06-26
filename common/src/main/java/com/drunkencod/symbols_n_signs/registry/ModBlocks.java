package com.drunkencod.symbols_n_signs.registry;

import com.drunkencod.symbols_n_signs.block.display_panel.DisplayPanelBlock;
import com.drunkencod.symbols_n_signs.block.display_panel.DisplayPanelBlockEntity;
import com.drunkencod.symbols_n_signs.block.display_panel.DisplayPanelItem;
import com.drunkencod.symbols_n_signs.block.sign_post.SignPostBlock;
import com.drunkencod.symbols_n_signs.block.sign_post.SignPostButtonFixtureBlock;
import com.drunkencod.symbols_n_signs.block.sign_post.SignPostButtonFixtureBlockEntity;
import com.drunkencod.symbols_n_signs.block.sign_post.SignPostLampFixtureBlock;
import com.drunkencod.symbols_n_signs.block.sign_post.SignPostLampFixtureBlockEntity;
import com.drunkencod.symbols_n_signs.block.sign_post.SignPostRedstoneEmitterFixtureBlock;
import com.drunkencod.symbols_n_signs.block.sign_post.SignPostRedstoneEmitterFixtureBlockEntity;
import com.drunkencod.symbols_n_signs.block.sign_post.SignPostSignFixtureBlock;
import com.drunkencod.symbols_n_signs.block.sign_post.SignPostSignFixtureBlockEntity;
import com.drunkencod.symbols_n_signs.platform.Services;
import com.drunkencod.symbols_n_signs.util.TooltipUtil;

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

        public static void register() {
                // Triggers static field initialization
        }

        // #region Display Panel
        public static final Supplier<DisplayPanelBlock> DISPLAY_PANEL = Services.REGISTRY.registerBlock(
                        DisplayPanelBlock.ID,
                        () -> new DisplayPanelBlock(BlockBehaviour.Properties.of()
                                        .strength(0.85f, 3.0f)
                                        .sound(SoundType.METAL)
                                        .noOcclusion()));

        public static final Supplier<Item> DISPLAY_PANEL_ITEM = Services.REGISTRY.registerItem(
                        DisplayPanelBlock.ID,
                        () -> new DisplayPanelItem(DISPLAY_PANEL.get(), new Item.Properties()));

        public static final Supplier<BlockEntityType<DisplayPanelBlockEntity>> DISPLAY_PANEL_BE_TYPE = Services.REGISTRY
                        .registerBlockEntityType(DisplayPanelBlock.ID, ModBlocks::makeDisplayPanelBEType);

        private static BlockEntityType<DisplayPanelBlockEntity> makeDisplayPanelBEType() {
                return BlockEntityType.Builder.of(DisplayPanelBlockEntity::new, DISPLAY_PANEL.get()).build(null);
        }

        // #region Sign Post
        public static final Supplier<SignPostBlock> SIGN_POST = Services.REGISTRY.registerBlock(
                        SignPostBlock.ID,
                        () -> new SignPostBlock(BlockBehaviour.Properties.of()
                                        .strength(0.85f, 2.0f)
                                        .sound(SoundType.COPPER_GRATE)
                                        .pushReaction(PushReaction.NORMAL)
                                        .requiresCorrectToolForDrops()
                                        .noOcclusion()));

        public static final Supplier<Item> SIGN_POST_ITEM = Services.REGISTRY.registerItem(
                        SignPostBlock.ID,
                        () -> new BlockItem(SIGN_POST.get(), new Item.Properties()
                                        .component(DataComponents.LORE,
                                                        TooltipUtil.getTooltip(
                                                                        "block.symbols_n_signs.sign_post.tooltip"))));

        // #region Sign Post - Sign Fixture
        public static final Supplier<SignPostSignFixtureBlock> SIGN_POST_SIGN_FIXTURE = Services.REGISTRY
                        .registerBlock(SignPostSignFixtureBlock.ID,
                                        () -> new SignPostSignFixtureBlock(BlockBehaviour.Properties.of()
                                                        .strength(0.85f, 2.0f)
                                                        .sound(SoundType.COPPER_GRATE)
                                                        .pushReaction(PushReaction.NORMAL)
                                                        .requiresCorrectToolForDrops()
                                                        .noOcclusion()));

        public static final Supplier<Item> SIGN_POST_SIGN_FIXTURE_ITEM = Services.REGISTRY.registerItem(
                        SignPostSignFixtureBlock.ID,
                        () -> new BlockItem(SIGN_POST_SIGN_FIXTURE.get(), new Item.Properties()
                                        .component(DataComponents.LORE,
                                                        TooltipUtil.getTooltip(
                                                                        "block.symbols_n_signs.sign_post_sign_fixture.tooltip"))));

        public static final Supplier<BlockEntityType<SignPostSignFixtureBlockEntity>> SIGN_POST_SIGN_FIXTURE_BE_TYPE = Services.REGISTRY
                        .registerBlockEntityType(SignPostSignFixtureBlock.ID, ModBlocks::makeSignFixtureBEType);

        private static BlockEntityType<SignPostSignFixtureBlockEntity> makeSignFixtureBEType() {
                return BlockEntityType.Builder.of(SignPostSignFixtureBlockEntity::new,
                                SIGN_POST_SIGN_FIXTURE.get()).build(null);
        }

        // #region Sign Post - Button Fixture
        public static final Supplier<SignPostButtonFixtureBlock> SIGN_POST_BUTTON_FIXTURE = Services.REGISTRY
                        .registerBlock(SignPostButtonFixtureBlock.ID,
                                        () -> new SignPostButtonFixtureBlock(BlockBehaviour.Properties.of()
                                                        .strength(0.85f, 2.0f)
                                                        .sound(SoundType.COPPER_GRATE)
                                                        .pushReaction(PushReaction.NORMAL)
                                                        .requiresCorrectToolForDrops()
                                                        .noOcclusion()));

        public static final Supplier<Item> SIGN_POST_BUTTON_FIXTURE_ITEM = Services.REGISTRY.registerItem(
                        SignPostButtonFixtureBlock.ID,
                        () -> new BlockItem(SIGN_POST_BUTTON_FIXTURE.get(), new Item.Properties()
                                        .component(DataComponents.LORE,
                                                        TooltipUtil.getTooltip(
                                                                        "block.symbols_n_signs.sign_post_button_fixture.tooltip"))));

        public static final Supplier<BlockEntityType<SignPostButtonFixtureBlockEntity>> SIGN_POST_BUTTON_FIXTURE_BE_TYPE = Services.REGISTRY
                        .registerBlockEntityType(
                                        SignPostButtonFixtureBlock.ID, ModBlocks::makeButtonFixtureBEType);

        private static BlockEntityType<SignPostButtonFixtureBlockEntity> makeButtonFixtureBEType() {
                return BlockEntityType.Builder.of(SignPostButtonFixtureBlockEntity::new,
                                SIGN_POST_BUTTON_FIXTURE.get()).build(null);
        }

        // #region Sign Post - Lamp Fixture
        public static final Supplier<SignPostLampFixtureBlock> SIGN_POST_LAMP_FIXTURE = Services.REGISTRY
                        .registerBlock(SignPostLampFixtureBlock.ID,
                                        () -> new SignPostLampFixtureBlock(BlockBehaviour.Properties.of()
                                                        .strength(0.85f, 2.0f)
                                                        .sound(SoundType.COPPER_GRATE)
                                                        .pushReaction(PushReaction.NORMAL)
                                                        .requiresCorrectToolForDrops()
                                                        .noOcclusion()
                                                        .lightLevel(state -> state.getValue(
                                                                        SignPostLampFixtureBlock.LIT) ? 15 : 0)));

        public static final Supplier<Item> SIGN_POST_LAMP_FIXTURE_ITEM = Services.REGISTRY.registerItem(
                        SignPostLampFixtureBlock.ID,
                        () -> new BlockItem(SIGN_POST_LAMP_FIXTURE.get(), new Item.Properties()
                                        .component(DataComponents.LORE,
                                                        TooltipUtil.getTooltip(
                                                                        "block.symbols_n_signs.sign_post_lamp_fixture.tooltip"))));

        public static final Supplier<BlockEntityType<SignPostLampFixtureBlockEntity>> SIGN_POST_LAMP_FIXTURE_BE_TYPE = Services.REGISTRY
                        .registerBlockEntityType(SignPostLampFixtureBlock.ID, ModBlocks::makeLampFixtureBEType);

        private static BlockEntityType<SignPostLampFixtureBlockEntity> makeLampFixtureBEType() {
                return BlockEntityType.Builder.of(SignPostLampFixtureBlockEntity::new,
                                SIGN_POST_LAMP_FIXTURE.get()).build(null);
        }

        // #region Sign Post - Redstone Emitter Fixture
        public static final Supplier<SignPostRedstoneEmitterFixtureBlock> SIGN_POST_REDSTONE_EMITTER_FIXTURE = Services.REGISTRY
                        .registerBlock(SignPostRedstoneEmitterFixtureBlock.ID,
                                        () -> new SignPostRedstoneEmitterFixtureBlock(BlockBehaviour.Properties.of()
                                                        .strength(0.85f, 2.0f)
                                                        .sound(SoundType.COPPER_GRATE)
                                                        .pushReaction(PushReaction.NORMAL)
                                                        .requiresCorrectToolForDrops()
                                                        .noOcclusion()
                                                        .lightLevel(state -> state.getValue(
                                                                        SignPostRedstoneEmitterFixtureBlock.LIT)
                                                                                        ? 4
                                                                                        : 0)));

        public static final Supplier<Item> SIGN_POST_REDSTONE_EMITTER_FIXTURE_ITEM = Services.REGISTRY.registerItem(
                        SignPostRedstoneEmitterFixtureBlock.ID,
                        () -> new BlockItem(SIGN_POST_REDSTONE_EMITTER_FIXTURE.get(), new Item.Properties()
                                        .component(DataComponents.LORE,
                                                        TooltipUtil.getTooltip(
                                                                        "block.symbols_n_signs.sign_post_redstone_emitter_fixture.tooltip"))));

        public static final Supplier<BlockEntityType<SignPostRedstoneEmitterFixtureBlockEntity>> SIGN_POST_REDSTONE_EMITTER_FIXTURE_BE_TYPE = Services.REGISTRY
                        .<SignPostRedstoneEmitterFixtureBlockEntity>registerBlockEntityType(
                                        SignPostRedstoneEmitterFixtureBlock.ID,
                                        ModBlocks::makeRedstoneEmitterFixtureBEType);

        private static BlockEntityType<SignPostRedstoneEmitterFixtureBlockEntity> makeRedstoneEmitterFixtureBEType() {
                return BlockEntityType.Builder.of(SignPostRedstoneEmitterFixtureBlockEntity::new,
                                SIGN_POST_REDSTONE_EMITTER_FIXTURE.get()).build(null);
        }
}
