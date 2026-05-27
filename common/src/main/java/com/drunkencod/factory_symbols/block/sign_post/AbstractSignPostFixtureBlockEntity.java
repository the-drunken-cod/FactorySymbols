package com.drunkencod.factory_symbols.block.sign_post;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/** Base class for all sign post fixture block entities. */
public abstract class AbstractSignPostFixtureBlockEntity extends BlockEntity {

    public AbstractSignPostFixtureBlockEntity(BlockEntityType<? extends AbstractSignPostFixtureBlockEntity> type,
            BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    // #region Sync to client

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
