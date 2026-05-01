package com.drunkencod.factory_symbols.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
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
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

import org.jetbrains.annotations.Nullable;

public class DisplayPanelBlock extends Block implements EntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);

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

    // #region Voxel shapes — 1-pixel slab on each face
    private static final VoxelShape SHAPE_NORTH = Block.box(0, 0, 15, 16, 16, 16);
    private static final VoxelShape SHAPE_SOUTH = Block.box(0, 0, 0, 16, 16, 1);
    private static final VoxelShape SHAPE_WEST = Block.box(15, 0, 0, 16, 16, 16);
    private static final VoxelShape SHAPE_EAST = Block.box(0, 0, 0, 1, 16, 16);

    public DisplayPanelBlock(Properties properties) {
        super(properties
                .pushReaction(PushReaction.NORMAL));
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(COLOR, DyeColor.BLACK));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, COLOR);
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
        Direction clickedFace = context.getClickedFace();
        BlockState state;
        if (clickedFace.getAxis().isHorizontal())
            state = defaultBlockState().setValue(FACING, clickedFace);
        else
            state = defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
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

        if (!heldStack.isEmpty()) {
            ItemStack toStore = heldStack.copyWithCount(1);

            // abort if already contains an item
            if (!be.getStoredItem().isEmpty())
                return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

            be.setStoredItem(toStore);

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
        if (!player.isShiftKeyDown())
            return InteractionResult.PASS;
        if (!(level.getBlockEntity(pos) instanceof DisplayPanelBlockEntity be))
            return InteractionResult.PASS;
        ItemStack existing = be.getStoredItem();
        if (!existing.isEmpty()) {
            be.setStoredItem(ItemStack.EMPTY);
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

    private static void giveOrDrop(Player player, ItemStack stack) {
        if (!player.getInventory().add(stack))
            player.drop(stack, false);
    }

    // #region Pick block

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        if (level.getBlockEntity(pos) instanceof DisplayPanelBlockEntity be) {
            ItemStack stored = be.getStoredItem();
            if (!stored.isEmpty())
                return stored.copy();
        }
        ItemStack stack = super.getCloneItemStack(level, pos, state);
        int id = colorToModelId(state.getValue(COLOR));
        if (id != 0)
            stack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(id));
        return stack;
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
        return level.getBlockEntity(pos) instanceof DisplayPanelBlockEntity be && !be.getStoredItem().isEmpty()
                ? 15
                : 0;
    }

}
