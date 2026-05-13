package com.drunkencod.factory_symbols.block.sign_post;

import com.drunkencod.factory_symbols.Constants;
import com.drunkencod.factory_symbols.platform.Services;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;

public class SignPostBlock extends PipeBlock implements SimpleWaterloggedBlock {

    /** Thickness of the sign post's segments in pixels */
    public static final float APOTHEM = 2f / 16f;

    public static final TagKey<Block> TAG_DOES_NOT_CONNECT_TO = TagKey.create(Registries.BLOCK,
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "sign_post_does_not_connect_to"));

    public static final TagKey<Block> TAG_CONNECTS_TO = TagKey.create(Registries.BLOCK,
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "sign_post_connects_to"));

    public static final TagKey<Block> TAG_CONNECTS_TO_BOTTOM_FACE = TagKey.create(Registries.BLOCK,
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "sign_post_connects_to_bottom"));

    public static final TagKey<Block> TAG_CONNECTS_TO_TOP_FACE = TagKey.create(Registries.BLOCK,
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "sign_post_connects_to_top"));

    public static final TagKey<Block> TAG_CONNECTS_TO_SIDES = TagKey.create(Registries.BLOCK,
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "sign_post_connects_to_sides"));

    public static final MapCodec<SignPostBlock> CODEC = SignPostBlock.simpleCodec(SignPostBlock::new);

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public SignPostBlock(Properties properties) {
        super(APOTHEM, properties.forceSolidOn());
        this.registerDefaultState(this.stateDefinition.any().setValue(NORTH, false).setValue(EAST, false)
                .setValue(SOUTH, false).setValue(WEST, false).setValue(UP, false).setValue(DOWN, false)
                .setValue(WATERLOGGED, false).setValue(POWERED, false));
    }

    /**
     * sign post is fully face-sturdy so buttons, levers, etc. can attach to any
     * face
     */
    @Override
    public VoxelShape getBlockSupportShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return Shapes.block();
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN, WATERLOGGED, POWERED);
    }

    @Override
    protected BlockState updateShape(BlockState ownState, Direction direction, BlockState neighborState,
            LevelAccessor level, BlockPos ownPos, BlockPos neighborPos) {
        if (!ownState.canSurvive(level, ownPos))
            level.scheduleTick(ownPos, this, 1);

        if (ownState.getValue(WATERLOGGED))
            level.scheduleTick(ownPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));

        // Connection Logic:
        // 1. If the block in the given direction is a sign post, connect to it
        // 2. If the block's face in the given direction is center-supporting, connect
        // to it
        // 3. If the block is a face-attached block (sign, button, lever, ...) pointing
        // away from us, connect to it
        // 4. Otherwise, disconnect from that direction

        boolean shouldConnect = neighborState.is(this)
                || isCenterSupporting(level, neighborState, neighborPos, direction.getOpposite())
                || isAttachedToFace(neighborState, direction);

        return ownState.setValue(PROPERTY_BY_DIRECTION.get(direction), shouldConnect);
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        return blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        LevelAccessor level = ctx.getLevel();
        boolean waterlogged = level.getFluidState(ctx.getClickedPos()).getType() == Fluids.WATER;
        return getStateWithConnections(level, ctx.getClickedPos(),
                this.defaultBlockState())
                .setValue(WATERLOGGED, waterlogged);
    }

    public static BlockState getStateWithConnections(LevelAccessor level, BlockPos blockPos,
            BlockState blockState) {
        BlockState belowState = level.getBlockState(blockPos.below());
        BlockState aboveState = level.getBlockState(blockPos.above());
        BlockState northState = level.getBlockState(blockPos.north());
        BlockState eastState = level.getBlockState(blockPos.east());
        BlockState southState = level.getBlockState(blockPos.south());
        BlockState westState = level.getBlockState(blockPos.west());
        Block block = blockState.getBlock();
        return blockState
                .trySetValue(DOWN,
                        belowState.is(block) || isCenterSupporting(level, belowState, blockPos, Direction.UP)
                                || isAttachedToFace(belowState, Direction.UP))
                .trySetValue(UP,
                        aboveState.is(block) || isCenterSupporting(level, aboveState, blockPos, Direction.DOWN)
                                || isAttachedToFace(aboveState, Direction.DOWN))
                .trySetValue(NORTH,
                        northState.is(block) || isCenterSupporting(level, northState, blockPos, Direction.SOUTH)
                                || isAttachedToFace(northState, Direction.SOUTH))
                .trySetValue(EAST,
                        eastState.is(block) || isCenterSupporting(level, eastState, blockPos, Direction.WEST)
                                || isAttachedToFace(eastState, Direction.WEST))
                .trySetValue(SOUTH,
                        southState.is(block) || isCenterSupporting(level, southState, blockPos, Direction.NORTH)
                                || isAttachedToFace(southState, Direction.NORTH))
                .trySetValue(WEST,
                        westState.is(block) || isCenterSupporting(level, westState, blockPos, Direction.EAST)
                                || isAttachedToFace(westState, Direction.EAST));
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock,
            BlockPos neighborPos, boolean movedByPiston) {
        if (!state.canSurvive(level, pos))
            level.scheduleTick(pos, this, 1);

        updateShape(state, Direction.DOWN, level.getBlockState(pos.below()), level, pos, pos.below());
        updateShape(state, Direction.UP, level.getBlockState(pos.above()), level, pos, pos.above());
        updateShape(state, Direction.NORTH, level.getBlockState(pos.north()), level, pos, pos.north());
        updateShape(state, Direction.EAST, level.getBlockState(pos.east()), level, pos, pos.east());
        updateShape(state, Direction.SOUTH, level.getBlockState(pos.south()), level, pos, pos.south());
        updateShape(state, Direction.WEST, level.getBlockState(pos.west()), level, pos, pos.west());

        // Relay: propagate power state change to connected sign posts.
        // Only react to non-sign-post neighbors to avoid feedback from our own BFS.
        // Read live state from the world instead of using the passed-in state to avoid
        // acting on a stale snapshot when updates are deferred by the neighbor updater.
        if (!level.isClientSide() && neighborBlock != this && !level.getBlockState(neighborPos).is(this)) {
            boolean shouldBePowered = isDirectlyPowered(level, pos);
            BlockState liveState = level.getBlockState(pos);
            // When powering down, verify no other post in the network still has a direct
            // signal — secondary updates from comparators/dust reading our powered state
            // would otherwise incorrectly collapse the network.
            if (liveState.is(this) && liveState.getValue(POWERED) != shouldBePowered)
                if (shouldBePowered || !isNetworkDirectlyPowered(level, pos))
                    propagatePower(level, pos, shouldBePowered);
        }
    }

    /**
     * Returns true when this position receives any direct redstone signal from its
     * non-sign-post neighbors (dust, lever, button, repeater, comparator, observer,
     * etc.). Sign posts don't override getSignal, so they contribute 0 and don't
     * create feedback through this check.
     */
    private static boolean isDirectlyPowered(Level level, BlockPos pos) {
        return level.getBestNeighborSignal(pos) > 0;
    }

    /**
     * BFS through the connected network to find any post that has a direct signal.
     */
    private boolean isNetworkDirectlyPowered(Level level, BlockPos start) {
        int maxDepth = Services.CONFIG.signPostRelayMaxDepth();
        Map<BlockPos, Integer> depthMap = new HashMap<>();
        ArrayDeque<BlockPos> queue = new ArrayDeque<>();
        depthMap.put(start, 0);
        queue.add(start);

        while (!queue.isEmpty()) {
            BlockPos current = queue.poll();
            int depth = depthMap.get(current);

            if (isDirectlyPowered(level, current))
                return true;

            if (depth >= maxDepth)
                continue;

            BlockState currentState = level.getBlockState(current);
            if (!currentState.is(this))
                continue;

            for (Map.Entry<Direction, BooleanProperty> entry : PROPERTY_BY_DIRECTION.entrySet()) {
                if (!currentState.getValue(entry.getValue()))
                    continue;
                BlockPos neighbor = current.relative(entry.getKey());
                if (depthMap.containsKey(neighbor))
                    continue;
                if (!level.getBlockState(neighbor).is(this))
                    continue;
                depthMap.put(neighbor, depth + 1);
                queue.add(neighbor);
            }
        }
        return false;
    }

    /**
     * BFS through connected sign posts, setting each to the given powered state.
     * Uses a depth map (shortest-path distance from source) so that every post
     * reachable within MAX_DEPTH is correctly included even in branching networks.
     */
    private void propagatePower(Level level, BlockPos source, boolean powered) {
        int maxDepth = Services.CONFIG.signPostRelayMaxDepth();

        Map<BlockPos, Integer> depthMap = new HashMap<>();
        ArrayDeque<BlockPos> queue = new ArrayDeque<>();
        depthMap.put(source, 0);
        queue.add(source);

        while (!queue.isEmpty()) {
            BlockPos current = queue.poll();
            int depth = depthMap.get(current);

            BlockState currentState = level.getBlockState(current);
            if (!currentState.is(this))
                continue;

            if (currentState.getValue(POWERED) != powered)
                level.setBlock(current, currentState.setValue(POWERED, powered), Block.UPDATE_ALL);

            if (depth >= maxDepth)
                continue;

            for (Map.Entry<Direction, BooleanProperty> entry : PROPERTY_BY_DIRECTION.entrySet()) {
                if (!currentState.getValue(entry.getValue()))
                    continue;
                BlockPos neighbor = current.relative(entry.getKey());
                if (depthMap.containsKey(neighbor))
                    continue;
                BlockState neighborState = level.getBlockState(neighbor);
                if (!neighborState.is(this))
                    continue;
                depthMap.put(neighbor, depth + 1);
                queue.add(neighbor);
            }
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return state.getValue(POWERED) ? 15 : 0;
    }

    /**
     * Returns true when a neighboring block is face-attached to this sign post in
     * the given direction
     * (e.g. a wall sign, button, or lever whose backing face points toward the
     * post)
     */
    private static boolean isAttachedToFace(BlockState neighbor, Direction directionFromPost) {
        // wall-attached blocks (signs, buttons, levers facing a direction)
        if (neighbor.hasProperty(BlockStateProperties.HORIZONTAL_FACING)
                && neighbor.getValue(BlockStateProperties.HORIZONTAL_FACING) == directionFromPost)
            return true;
        // floor/ceiling attached blocks (lever, button on floor/ceiling)
        if (neighbor.hasProperty(BlockStateProperties.ATTACH_FACE)) {
            AttachFace face = neighbor.getValue(BlockStateProperties.ATTACH_FACE);
            if ((directionFromPost == Direction.UP && face == AttachFace.FLOOR)
                    || (directionFromPost == Direction.DOWN && face == AttachFace.CEILING))
                return true;
        }
        // 6-directional facing blocks (end rods, lightning rods) whose tip points away
        // from the post
        if (neighbor.hasProperty(BlockStateProperties.FACING)
                && neighbor.getValue(BlockStateProperties.FACING) == directionFromPost)
            return true;
        // axis-oriented blocks (chains) — connect along the chain's axis
        if (neighbor.hasProperty(BlockStateProperties.AXIS)
                && neighbor.getValue(BlockStateProperties.AXIS) == directionFromPost.getAxis())
            return true;
        // hanging blocks (lanterns) that hang from the post above them
        if (directionFromPost == Direction.DOWN
                && neighbor.hasProperty(BlockStateProperties.HANGING)
                && neighbor.getValue(BlockStateProperties.HANGING))
            return true;
        // floor-mounted rotatable blocks (standing banners, signs, skulls on floor)
        if (directionFromPost == Direction.UP
                && neighbor.hasProperty(BlockStateProperties.ROTATION_16))
            return true;
        // explicitly tagged connectable blocks (pressure plates, ...)
        if (neighbor.is(TAG_CONNECTS_TO))
            return true;
        // blocks that only connect on their bottom face (rails, ...)
        if (directionFromPost == Direction.UP && neighbor.is(TAG_CONNECTS_TO_BOTTOM_FACE))
            return true;
        // blocks that only connect on their top face (weeping vines, cave vines, ...)
        if (directionFromPost == Direction.DOWN && neighbor.is(TAG_CONNECTS_TO_TOP_FACE))
            return true;
        // blocks that only connect on horizontal sides (cauldron, ...)
        if (directionFromPost.getAxis().isHorizontal() && neighbor.is(TAG_CONNECTS_TO_SIDES))
            return true;
        return false;
    }

    private static boolean isCenterSupporting(LevelAccessor level, BlockState state, BlockPos pos, Direction face) {
        // ignore air
        if (state.isAir())
            return false;
        // ignore everything in does_not_connect tag
        if (state.is(TAG_DOES_NOT_CONNECT_TO))
            return false;
        // force explicitly tagged center-supporting blocks through
        if (state.is(TAG_CONNECTS_TO))
            return true;
        return state.isFaceSturdy(level, pos, face, SupportType.CENTER);
    }

    // fix other posts occluding light propagation, causing "color banding":
    @Override
    protected boolean propagatesSkylightDown(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return !blockState.getValue(WATERLOGGED);
    }

    @Override
    protected boolean isPathfindable(BlockState blockState, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    protected MapCodec<? extends PipeBlock> codec() {
        return CODEC;
    }
}
