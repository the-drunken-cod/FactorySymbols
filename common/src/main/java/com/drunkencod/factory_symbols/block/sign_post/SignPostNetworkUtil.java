package com.drunkencod.factory_symbols.block.sign_post;

import com.drunkencod.factory_symbols.Constants;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Shared connection and shape logic for sign post blocks and fixture blocks.
 */
public final class SignPostNetworkUtil {

    private SignPostNetworkUtil() {
    }

    // #region Tags

    public static final TagKey<Item> TOOLS_WRENCH = TagKey.create(Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath("c", "tools/wrench"));

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

    /** All sign post network blocks: sign_post + sign_post_fixtures */
    public static final TagKey<Block> TAG_SIGN_POST_BLOCKS = TagKey.create(Registries.BLOCK,
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "sign_post_blocks"));

    // #region Shapes

    /** Thickness of sign post segments in pixels (matches SignPostBlock.APOTHEM) */
    public static final float APOTHEM = 2f / 16f;

    private static final float MIN = 0.5f - APOTHEM;
    private static final float MAX = 0.5f + APOTHEM;

    private static final VoxelShape CENTER_SHAPE = Block.box(MIN * 16, MIN * 16, MIN * 16, MAX * 16, MAX * 16,
            MAX * 16);
    private static final VoxelShape ARM_NORTH = Block.box(MIN * 16, MIN * 16, 0, MAX * 16, MAX * 16, MIN * 16);
    private static final VoxelShape ARM_SOUTH = Block.box(MIN * 16, MIN * 16, MAX * 16, MAX * 16, MAX * 16, 16);
    private static final VoxelShape ARM_WEST = Block.box(0, MIN * 16, MIN * 16, MIN * 16, MAX * 16, MAX * 16);
    private static final VoxelShape ARM_EAST = Block.box(MAX * 16, MIN * 16, MIN * 16, 16, MAX * 16, MAX * 16);
    private static final VoxelShape ARM_DOWN = Block.box(MIN * 16, 0, MIN * 16, MAX * 16, MIN * 16, MAX * 16);
    private static final VoxelShape ARM_UP = Block.box(MIN * 16, MAX * 16, MIN * 16, MAX * 16, 16, MAX * 16);

    /**
     * Builds the combined VoxelShape from the center cube and whichever arm
     * segments are enabled by the directional connection properties.
     */
    public static VoxelShape buildShape(BlockState state, Map<Direction, BooleanProperty> props) {
        VoxelShape shape = CENTER_SHAPE;
        if (state.getValue(props.get(Direction.NORTH)))
            shape = Shapes.or(shape, ARM_NORTH);
        if (state.getValue(props.get(Direction.SOUTH)))
            shape = Shapes.or(shape, ARM_SOUTH);
        if (state.getValue(props.get(Direction.WEST)))
            shape = Shapes.or(shape, ARM_WEST);
        if (state.getValue(props.get(Direction.EAST)))
            shape = Shapes.or(shape, ARM_EAST);
        if (state.getValue(props.get(Direction.DOWN)))
            shape = Shapes.or(shape, ARM_DOWN);
        if (state.getValue(props.get(Direction.UP)))
            shape = Shapes.or(shape, ARM_UP);
        return shape;
    }

    // #region Connection logic

    /**
     * Returns true when the neighbor at (pos + direction) should cause a connection
     * in that direction from the sign post or fixture at pos.
     *
     * @param direction direction from the sign post / fixture to the neighbor
     */
    public static boolean shouldConnectTo(LevelAccessor level, BlockState neighborState, BlockPos neighborPos,
            Direction direction) {
        // Fixtures block connections on their protruding face; no neighbor should
        // connect into that face from the opposite side.
        if (neighborState.getBlock() instanceof AbstractSignPostFixtureBlock fixture
                && direction == fixture.getFixtureDirection(neighborState).getOpposite())
            return false;
        return neighborState.is(TAG_SIGN_POST_BLOCKS)
                || isCenterSupporting(level, neighborState, neighborPos, direction.getOpposite())
                || isAttachedToFace(neighborState, direction);
    }

    /**
     * Returns true when a neighboring block is face-attached pointing outward in
     * directionFromPost (e.g. a button on the sign post's east face has
     * FACING=EAST).
     *
     * @param directionFromPost direction from the sign post / fixture toward the
     *                          neighbor
     */
    public static boolean isAttachedToFace(BlockState neighbor, Direction directionFromPost) {
        // wall-attached blocks (signs, buttons, levers)
        if (neighbor.hasProperty(BlockStateProperties.HORIZONTAL_FACING)
                && neighbor.getValue(BlockStateProperties.HORIZONTAL_FACING) == directionFromPost)
            return true;
        // floor/ceiling attached blocks
        if (neighbor.hasProperty(BlockStateProperties.ATTACH_FACE)) {
            AttachFace face = neighbor.getValue(BlockStateProperties.ATTACH_FACE);
            if ((directionFromPost == Direction.UP && face == AttachFace.FLOOR)
                    || (directionFromPost == Direction.DOWN && face == AttachFace.CEILING))
                return true;
        }
        // 6-directional facing blocks (end rods, lightning rods)
        if (neighbor.hasProperty(BlockStateProperties.FACING)
                && neighbor.getValue(BlockStateProperties.FACING) == directionFromPost)
            return true;
        // axis-oriented blocks (chains)
        if (neighbor.hasProperty(BlockStateProperties.AXIS)
                && neighbor.getValue(BlockStateProperties.AXIS) == directionFromPost.getAxis())
            return true;
        // hanging blocks (lanterns hanging below the post)
        if (directionFromPost == Direction.DOWN
                && neighbor.hasProperty(BlockStateProperties.HANGING)
                && neighbor.getValue(BlockStateProperties.HANGING))
            return true;
        // floor-mounted rotatable blocks (standing banners, signs, skulls)
        if (directionFromPost == Direction.UP
                && neighbor.hasProperty(BlockStateProperties.ROTATION_16))
            return true;
        // explicitly tagged connectable blocks
        if (neighbor.is(TAG_CONNECTS_TO))
            return true;
        if (directionFromPost == Direction.UP && neighbor.is(TAG_CONNECTS_TO_BOTTOM_FACE))
            return true;
        if (directionFromPost == Direction.DOWN && neighbor.is(TAG_CONNECTS_TO_TOP_FACE))
            return true;
        if (directionFromPost.getAxis().isHorizontal() && neighbor.is(TAG_CONNECTS_TO_SIDES))
            return true;
        return false;
    }

    public static boolean isCenterSupporting(LevelAccessor level, BlockState state, BlockPos pos, Direction face) {
        if (state.isAir())
            return false;
        if (state.is(TAG_DOES_NOT_CONNECT_TO))
            return false;
        if (state.is(TAG_CONNECTS_TO))
            return true;
        return state.isFaceSturdy(level, pos, face, SupportType.CENTER);
    }

    // #region State helpers

    /**
     * Returns the PROPERTY_BY_DIRECTION map from PipeBlock (N/E/S/W/U/D →
     * BooleanProperty). Avoids duplicating the map definition outside PipeBlock.
     */
    public static Map<Direction, BooleanProperty> getDirectionProperties() {
        return PipeBlock.PROPERTY_BY_DIRECTION;
    }

    // #region Power relay utilities

    /**
     * Returns true when the sign post network block at pos has a direct external
     * redstone signal (from non-network neighbors). For button fixtures, also
     * accounts for ACTIVE_HIGH and returns true when the button's own pressed state
     * warrants power output.
     */
    public static boolean hasDirectPower(Level level, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof SignPostButtonFixtureBlock) {
            boolean pressed = state.getValue(SignPostButtonFixtureBlock.PRESSED);
            boolean activeHigh = state.getValue(SignPostButtonFixtureBlock.ACTIVE_HIGH);
            return activeHigh ? pressed : !pressed;
        }
        for (Direction dir : Direction.values()) {
            BlockPos neighborPos = pos.relative(dir);
            if (level.getBlockState(neighborPos).is(TAG_SIGN_POST_BLOCKS))
                continue;
            if (level.getSignal(neighborPos, dir) > 0)
                return true;
        }
        return false;
    }

    /**
     * BFS through the connected sign post network to find any node with a direct
     * external power source. Returns true if any such node is found within maxDepth
     * hops from start.
     */
    public static boolean isNetworkDirectlyPowered(Level level, BlockPos start, int maxDepth) {
        Map<BlockPos, Integer> depthMap = new HashMap<>();
        ArrayDeque<BlockPos> queue = new ArrayDeque<>();
        depthMap.put(start, 0);
        queue.add(start);

        while (!queue.isEmpty()) {
            BlockPos current = queue.poll();
            int depth = depthMap.get(current);
            BlockState currentState = level.getBlockState(current);
            if (!currentState.is(TAG_SIGN_POST_BLOCKS))
                continue;

            if (hasDirectPower(level, current, currentState))
                return true;

            if (depth >= maxDepth)
                continue;

            for (Map.Entry<Direction, BooleanProperty> entry : PipeBlock.PROPERTY_BY_DIRECTION.entrySet()) {
                if (!currentState.getValue(entry.getValue()))
                    continue;
                BlockPos neighbor = current.relative(entry.getKey());
                if (depthMap.containsKey(neighbor))
                    continue;
                BlockState neighborState = level.getBlockState(neighbor);
                if (!neighborState.is(TAG_SIGN_POST_BLOCKS))
                    continue;
                depthMap.put(neighbor, depth + 1);
                queue.add(neighbor);
            }
        }
        return false;
    }

    /**
     * BFS through the connected sign post network setting each node's POWERED
     * property to the given value. Uses UPDATE_CLIENTS during traversal to avoid
     * cascading neighborChanged callbacks; after all nodes are updated, calls
     * updateNeighborsAt for each changed position so comparators and other redstone
     * observers are notified exactly once per node.
     */
    public static void propagatePower(Level level, BlockPos source, boolean powered, int maxDepth) {
        Map<BlockPos, Integer> depthMap = new HashMap<>();
        ArrayDeque<BlockPos> queue = new ArrayDeque<>();
        List<BlockPos> changed = new ArrayList<>();
        depthMap.put(source, 0);
        queue.add(source);

        while (!queue.isEmpty()) {
            BlockPos current = queue.poll();
            int depth = depthMap.get(current);
            BlockState currentState = level.getBlockState(current);
            if (!currentState.is(TAG_SIGN_POST_BLOCKS))
                continue;

            if (currentState.hasProperty(BlockStateProperties.POWERED)
                    && currentState.getValue(BlockStateProperties.POWERED) != powered) {
                BlockState newState = currentState.setValue(BlockStateProperties.POWERED, powered);
                if (newState.getBlock() instanceof SignPostLampFixtureBlock) {
                    newState = newState.setValue(SignPostLampFixtureBlock.LIT,
                            SignPostLampFixtureBlock.computeLit(newState, powered));
                }
                level.setBlock(current, newState, Block.UPDATE_CLIENTS);
                changed.add(current.immutable());
            }

            if (depth >= maxDepth)
                continue;

            for (Map.Entry<Direction, BooleanProperty> entry : PipeBlock.PROPERTY_BY_DIRECTION.entrySet()) {
                if (!currentState.getValue(entry.getValue()))
                    continue;
                BlockPos neighbor = current.relative(entry.getKey());
                if (depthMap.containsKey(neighbor))
                    continue;
                BlockState neighborState = level.getBlockState(neighbor);
                if (!neighborState.is(TAG_SIGN_POST_BLOCKS))
                    continue;
                depthMap.put(neighbor, depth + 1);
                queue.add(neighbor);
            }
        }

        for (BlockPos pos : changed) {
            BlockState state = level.getBlockState(pos);
            level.updateNeighborsAt(pos, state.getBlock());
        }
    }

    // #region Wrench harvest

    /**
     * Handles sneak-right-click harvest for sign post blocks. Any item in
     * {@code #c:tools/wrench} while the player is sneaking will break the block,
     * play the break sound + particles and item-pickup sound, and deliver the
     * block's loot-table drops directly into the player's inventory. Overflow items
     * (when the inventory is full) are dropped from the player's eye position.
     * In creative mode the block is still broken but no items are given.
     */
    public static ItemInteractionResult harvestWithWrench(ItemStack stack, BlockState state, Level level,
            BlockPos pos, Player player) {
        if (!player.isShiftKeyDown() || !stack.is(TOOLS_WRENCH))
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        if (!level.isClientSide()) {
            BlockEntity be = level.getBlockEntity(pos);
            if (!player.isCreative()) {
                List<ItemStack> drops = Block.getDrops(state, (ServerLevel) level, pos, be, player, stack);
                for (ItemStack drop : drops) {
                    player.addItem(drop);
                    if (!drop.isEmpty())
                        player.drop(drop, false);
                }
            }
            level.destroyBlock(pos, false);
            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2f,
                    0.5f + level.random.nextFloat() * 0.4f);
        }
        return ItemInteractionResult.sidedSuccess(level.isClientSide());
    }
}
