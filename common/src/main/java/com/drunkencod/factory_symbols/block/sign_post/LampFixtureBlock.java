package com.drunkencod.factory_symbols.block.sign_post;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.codecs.RecordCodecBuilder.Mu;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;

public class LampFixtureBlock extends AbstractSignPostFixtureBlock {
    public static final MapCodec<LampFixtureBlock> CODEC = RecordCodecBuilder
            .mapCodec(instance -> instance.group(LampFixtureBlock.propertiesCodec())
                    .apply((Applicative<Mu<LampFixtureBlock>, ?>) instance, LampFixtureBlock::new));

    public LampFixtureBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Direction getFixtureDirection(BlockState state) {
        // Lamp hangs from ceiling (FACE=CEILING) → fixture direction is UP (the
        // mounting face)
        return state.getValue(FACE) == AttachFace.CEILING ? Direction.UP
                : state.getValue(FACE) == AttachFace.FLOOR ? Direction.DOWN
                        : state.getValue(FACING);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        // TODO: return a LampFixtureBlockEntity once it is created
        return null;
    }

    public MapCodec<LampFixtureBlock> codec() {
        return CODEC;
    }
}
