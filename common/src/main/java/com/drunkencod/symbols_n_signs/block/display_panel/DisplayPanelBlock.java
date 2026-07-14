package com.drunkencod.symbols_n_signs.block.display_panel;

import com.drunkencod.symbols_n_signs.Constants;
import com.drunkencod.symbols_n_signs.item.ConfigurationClipboardItem;
import com.drunkencod.symbols_n_signs.item.ConfigurationClipboardUtil;
import com.drunkencod.symbols_n_signs.item.IWrenchConfigurable;
import com.drunkencod.symbols_n_signs.item.RatchetWrenchItem;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

import org.jetbrains.annotations.Nullable;

public class DisplayPanelBlock extends Block implements EntityBlock, IWrenchConfigurable {

    public static final String ID = "display_panel";

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);
    public static final BooleanProperty LOCKED = BooleanProperty.create("locked");

    // #region Color ↔ model-id helpers (BLACK = 0, component not strictly needed)

    public static final List<Integer> COLORS_ORDERED = List.of(
            1, // WHITE
            9, // LIGHT_GRAY
            8, // GRAY
            0, // BLACK
            13, // BROWN
            15, // RED
            2, // ORANGE
            5, // YELLOW
            6, // LIME
            14, // GREEN
            10, // CYAN
            4, // LIGHT_BLUE
            12, // BLUE
            11, // PURPLE
            3, // MAGENTA
            7); // PINK

    public static int colorToModelId(DyeColor color) {
        return switch (color) {
            case BLACK -> 0;
            case WHITE -> 1;
            case ORANGE -> 2;
            case MAGENTA -> 3;
            case LIGHT_BLUE -> 4;
            case YELLOW -> 5;
            case LIME -> 6;
            case PINK -> 7;
            case GRAY -> 8;
            case LIGHT_GRAY -> 9;
            case CYAN -> 10;
            case PURPLE -> 11;
            case BLUE -> 12;
            case BROWN -> 13;
            case GREEN -> 14;
            case RED -> 15;
        };
    }

    public static DyeColor modelIdToColor(int id) {
        return switch (id) {
            case 1 -> DyeColor.WHITE;
            case 2 -> DyeColor.ORANGE;
            case 3 -> DyeColor.MAGENTA;
            case 4 -> DyeColor.LIGHT_BLUE;
            case 5 -> DyeColor.YELLOW;
            case 6 -> DyeColor.LIME;
            case 7 -> DyeColor.PINK;
            case 8 -> DyeColor.GRAY;
            case 9 -> DyeColor.LIGHT_GRAY;
            case 10 -> DyeColor.CYAN;
            case 11 -> DyeColor.PURPLE;
            case 12 -> DyeColor.BLUE;
            case 13 -> DyeColor.BROWN;
            case 14 -> DyeColor.GREEN;
            case 15 -> DyeColor.RED;
            default -> DyeColor.BLACK;
        };
    }

    // #region Voxel shapes - 1-pixel slab on each face
    private static final VoxelShape SHAPE_NORTH = Block.box(0, 0, 15, 16, 16, 16);
    private static final VoxelShape SHAPE_SOUTH = Block.box(0, 0, 0, 16, 16, 1);
    private static final VoxelShape SHAPE_WEST = Block.box(15, 0, 0, 16, 16, 16);
    private static final VoxelShape SHAPE_EAST = Block.box(0, 0, 0, 1, 16, 16);

    public DisplayPanelBlock(Properties properties) {
        super(properties
                .pushReaction(PushReaction.BLOCK));
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(COLOR, DyeColor.BLACK)
                .setValue(LOCKED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, COLOR, LOCKED);
    }

    // #region Shape

    @Override
    public VoxelShape getOcclusionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return getShape(state, level, pos, CollisionContext.empty());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case NORTH -> SHAPE_NORTH;
            case SOUTH -> SHAPE_SOUTH;
            case WEST -> SHAPE_WEST;
            case EAST -> SHAPE_EAST;
            default -> SHAPE_NORTH;
        };
    }

    // #region Placement

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
        CustomModelData cmd = context.getItemInHand().get(DataComponents.CUSTOM_MODEL_DATA);
        int id = cmd != null ? cmd.value() : 0;
        return state.setValue(COLOR, modelIdToColor(id));
    }

    // #region Interaction

    @Override
    public ItemInteractionResult useItemOn(ItemStack heldStack, BlockState state, Level level, BlockPos pos,
            Player player, InteractionHand hand, BlockHitResult hit) {
        if (!(level.getBlockEntity(pos) instanceof DisplayPanelBlockEntity be))
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        // Let the Ratchet Wrench/Configuration Clipboard fall through to Item#useOn
        // instead of being stored as the displayed item.
        if (heldStack.getItem() instanceof RatchetWrenchItem || heldStack.getItem() instanceof ConfigurationClipboardItem)
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        if (!heldStack.isEmpty()) {
            if (isLockedPlaySound(level, player, state, pos))
                return ItemInteractionResult.FAIL;

            ItemStack toStore = heldStack.copyWithCount(1);

            // abort if already contains an item
            if (!be.getStoredItem().isEmpty())
                return ItemInteractionResult.FAIL;

            be.setStoredItem(toStore);
            playAddItemSound(level, player, pos);
            level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);

            if (!level.isClientSide()) {
                if (!player.isCreative())
                    heldStack.shrink(1);
                be.setChanged();
                level.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);
            }
            return ItemInteractionResult.SUCCESS;
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
            BlockHitResult hit) {
        if (!(level.getBlockEntity(pos) instanceof DisplayPanelBlockEntity be))
            return InteractionResult.PASS;

        if (!player.isShiftKeyDown())
            return InteractionResult.PASS;

        if (isLockedPlaySound(level, player, state, pos))
            return InteractionResult.FAIL;

        ItemStack existing = be.getStoredItem();

        if (!existing.isEmpty()) {
            be.setStoredItem(ItemStack.EMPTY);
            playRemoveItemSound(level, player, pos);
            level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);

            if (!level.isClientSide()) {
                if (!player.isCreative())
                    giveOrDrop(player, existing.copy());

                be.setChanged();
                level.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    private boolean isLockedPlaySound(Level level, Player player, BlockState state, BlockPos pos) {
        if (state.getValue(LOCKED)) {
            playLockedSound(level, player, pos);
            return true;
        }
        return false;
    }

    private static void giveOrDrop(Player player, ItemStack stack) {
        if (!player.getInventory().add(stack))
            player.drop(stack, false);
    }

    // #region sound

    private static final float SOUND_VOL = 0.75f;
    private static final float SOUND_PITCH = 1.3f;

    private void playAddItemSound(Level level, Player player, BlockPos pos) {
        level.playSound(player, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, SOUND_VOL, SOUND_PITCH);
    }

    private void playRemoveItemSound(Level level, Player player, BlockPos pos) {
        level.playSound(player, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, SOUND_VOL,
                SOUND_PITCH);
    }

    private void playLockedSound(Level level, Player player, BlockPos pos) {
        if (level.isClientSide())
            level.playSound(player, pos, SoundEvents.DISPENSER_FAIL, SoundSource.BLOCKS, SOUND_VOL, SOUND_PITCH);
    }

    // #region Pick block

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        ItemStack stack = super.getCloneItemStack(level, pos, state);
        int id = colorToModelId(state.getValue(COLOR));
        if (id != 0)
            stack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(id));
        return stack;
    }

    // #region Placement - offhand Configuration Clipboard override

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer,
            ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        ConfigurationClipboardUtil.applyOffhandConfigOnPlace(level, pos, state, placer);
    }

    // #region IWrenchConfigurable

    private static final int MODE_ROTATION = 0;
    private static final int MODE_SCALE = 1;
    private static final int MODE_BRIGHT = 2;
    private static final int MODE_LOCKED = 3;
    private static final int MODE_COUNT = 4;

    private static String getModeKey(String valueKey) {
        return Constants.MOD_ID + ".ratchet_wrench.mode.display_panel." + valueKey;
    }

    private static String getModeName(String valueKey, int modeIndex) {
        String modeName = Component.translatable(getModeKey(valueKey)).getString();
        String template = Component.translatable(getModeKey("name_template")).getString();
        return String.format(template, modeIndex + 1, MODE_COUNT, modeName);
    }

    @Override
    public int getWrenchModeCount(BlockState state, Direction clickedFace) {
        return MODE_COUNT;
    }

    @Override
    public String getWrenchModeKey(BlockState state, Direction clickedFace, int modeIndex) {
        return switch (modeIndex) {
            case MODE_ROTATION -> getModeKey("rotation");
            case MODE_SCALE -> getModeKey("scale");
            case MODE_BRIGHT -> getModeKey("bright");
            case MODE_LOCKED -> getModeKey("locked");
            default -> Constants.MOD_ID + ".ratchet_wrench.mode.unknown";
        };
    }

    @Override
    public String getWrenchModeString(BlockState state, Direction clickedFace, int modeIndex) {
        return switch (modeIndex) {
            case MODE_ROTATION -> getModeName("rotation", modeIndex);
            case MODE_SCALE -> getModeName("scale", modeIndex);
            case MODE_BRIGHT -> getModeName("bright", modeIndex);
            case MODE_LOCKED -> getModeName("locked", modeIndex);
            default -> Constants.MOD_ID + ".ratchet_wrench.mode.unknown";
        };
    }

    @Override
    public Component getCurrentModeComponent(BlockState state, Direction clickedFace, Player player) {
        ItemStack wrench = RatchetWrenchItem.getWrenchInHand(player);
        if (wrench.isEmpty())
            return Component.empty();
        ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(this);
        int mode = RatchetWrenchItem.getSelectedMode(wrench, blockId) % MODE_COUNT;
        return Component.literal(getWrenchModeString(state, clickedFace, mode));
    }

    @Override
    public InteractionResult onWrenchLeftClick(Level level, BlockPos pos, BlockState state, Direction clickedFace,
            @Nullable Vec3 hitLocation, Player player) {
        if (!level.isClientSide()) {
            ItemStack wrench = RatchetWrenchItem.getWrenchInHand(player);
            if (wrench.isEmpty())
                return InteractionResult.PASS;
            ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(this);
            int next = (RatchetWrenchItem.getSelectedMode(wrench, blockId) + (player.isShiftKeyDown() ? -1 : 1))
                    % MODE_COUNT;
            if (next <= -1)
                next = MODE_COUNT - 1;
            RatchetWrenchItem.setSelectedMode(wrench, blockId, next);
            player.displayClientMessage(Component.literal(getWrenchModeString(state, clickedFace, next)), true);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult onWrenchRightClick(Level level, BlockPos pos, BlockState state, Direction clickedFace,
            Vec3 hitLocation, Player player) {
        if (!(level.getBlockEntity(pos) instanceof DisplayPanelBlockEntity be))
            return InteractionResult.PASS;
        if (!level.isClientSide()) {
            ItemStack wrench = RatchetWrenchItem.getWrenchInHand(player);
            if (wrench.isEmpty())
                return InteractionResult.PASS;
            ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(this);
            int mode = RatchetWrenchItem.getSelectedMode(wrench, blockId);
            switch (mode) {
                case MODE_ROTATION -> {
                    int next = (be.getRotation() + 1) % DisplayPanelBlockEntity.ROTATION_COUNT;
                    be.setRotation(next);
                    player.displayClientMessage(
                            Component.translatable(getWrenchModeKey(state, clickedFace, MODE_ROTATION))
                                    .append(": " + next),
                            true);
                }
                case MODE_SCALE -> {
                    float next = Math.round((be.getScale() + DisplayPanelBlockEntity.SCALE_STEP) * 1000f) / 1000f;
                    if (next > DisplayPanelBlockEntity.MAX_SCALE + 1e-3f)
                        next = DisplayPanelBlockEntity.MIN_SCALE;
                    be.setScale(next);
                    player.displayClientMessage(
                            Component.translatable(getWrenchModeKey(state, clickedFace, MODE_SCALE))
                                    .append(": " + next),
                            true);
                }
                case MODE_BRIGHT -> {
                    boolean next = !be.isBright();
                    be.setBright(next);
                    player.displayClientMessage(
                            Component.translatable(getWrenchModeKey(state, clickedFace, MODE_BRIGHT))
                                    .append(": ")
                                    .append(Component.translatable(
                                            getWrenchModeKey(state, clickedFace, MODE_BRIGHT) + ".value." + next)),
                            true);
                }
                case MODE_LOCKED -> {
                    boolean next = !state.getValue(LOCKED);
                    level.setBlock(pos, state.setValue(LOCKED, next), Block.UPDATE_CLIENTS);
                    player.displayClientMessage(
                            Component.translatable(getWrenchModeKey(state, clickedFace, MODE_LOCKED))
                                    .append(": ")
                                    .append(Component.translatable(
                                            getWrenchModeKey(state, clickedFace, MODE_LOCKED) + ".value." + next)),
                            true);
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    // #region Configuration Clipboard

    private static final String NBT_ROTATION = "rotation";
    private static final String NBT_SCALE = "scale";
    private static final String NBT_BRIGHT = "bright";
    private static final String NBT_LOCKED = "locked";
    private static final String NBT_ITEM = "item";

    @Override
    public int getConfigFormatVersion() {
        return 1;
    }

    @Override
    public CompoundTag copyConfiguration(Level level, BlockPos pos, BlockState state, Direction clickedFace,
            @Nullable Vec3 hitLocation, Player player) {
        if (!(level.getBlockEntity(pos) instanceof DisplayPanelBlockEntity be))
            return new CompoundTag();
        CompoundTag tag = new CompoundTag();
        tag.putInt(NBT_ROTATION, be.getRotation());
        tag.putFloat(NBT_SCALE, be.getScale());
        tag.putBoolean(NBT_BRIGHT, be.isBright());
        tag.putBoolean(NBT_LOCKED, state.getValue(LOCKED));
        if (!be.getStoredItem().isEmpty())
            tag.put(NBT_ITEM, be.getStoredItem().saveOptional(level.registryAccess()));
        return tag;
    }

    @Override
    public boolean pasteConfiguration(Level level, BlockPos pos, BlockState state, Direction clickedFace,
            @Nullable Vec3 hitLocation, CompoundTag data, Player player) {
        if (!(level.getBlockEntity(pos) instanceof DisplayPanelBlockEntity be))
            return false;

        be.setRotation(data.getInt(NBT_ROTATION));
        be.setScale(data.contains(NBT_SCALE) ? data.getFloat(NBT_SCALE) : 1.0f);
        be.setBright(data.getBoolean(NBT_BRIGHT));
        if (data.contains(NBT_LOCKED))
            level.setBlock(pos, state.setValue(LOCKED, data.getBoolean(NBT_LOCKED)), Block.UPDATE_CLIENTS);

        if (be.getStoredItem().isEmpty() && data.contains(NBT_ITEM)) {
            ItemStack template = ItemStack.parseOptional(level.registryAccess(), data.getCompound(NBT_ITEM));
            ItemStack found = ConfigurationClipboardUtil.takeMatchingItem(player, template);
            if (!found.isEmpty())
                be.setStoredItem(found);
        }
        return true;
    }

    // #region BlockEntity

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DisplayPanelBlockEntity(pos, state);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock()) && !movedByPiston) {
            if (level.getBlockEntity(pos) instanceof DisplayPanelBlockEntity be) {
                ItemStack stored = be.getStoredItem();
                if (!stored.isEmpty())
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stored);
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    // #region redstone

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof DisplayPanelBlockEntity be) {
            ItemStack stack = be.getStoredItem();
            if (stack.isEmpty())
                return 0;
            int stackSize = stack.getMaxStackSize();
            if (stackSize <= 1)
                return 15;
            if (stackSize <= 8)
                return 11;
            if (stackSize <= 16)
                return 7;
            if (stackSize <= 32)
                return 3;
            if (stackSize <= 64)
                return 1;
        }
        return 1;
    }
}
