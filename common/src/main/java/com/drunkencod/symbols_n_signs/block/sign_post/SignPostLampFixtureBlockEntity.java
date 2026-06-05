package com.drunkencod.symbols_n_signs.block.sign_post;

import com.drunkencod.symbols_n_signs.registry.ModBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class SignPostLampFixtureBlockEntity extends AbstractSignPostFixtureBlockEntity {

    public SignPostLampFixtureBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.SIGN_POST_LAMP_FIXTURE_BE_TYPE.get(), pos, state);
    }
}
