package com.drunkencod.symbols_n_signs.block.sign_post;

import com.drunkencod.symbols_n_signs.registry.ModBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class SignPostButtonFixtureBlockEntity extends AbstractSignPostFixtureBlockEntity {

    public SignPostButtonFixtureBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.SIGN_POST_BUTTON_FIXTURE_BE_TYPE.get(), pos, state);
    }
}
