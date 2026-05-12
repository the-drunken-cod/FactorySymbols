package com.drunkencod.factory_symbols.block.sign_post;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.codecs.RecordCodecBuilder.Mu;

public class LampFixtureBlock extends AbstractSignPostFixtureBlock {
    public static final MapCodec<LampFixtureBlock> CODEC = RecordCodecBuilder
            .mapCodec(instance -> instance.group(LampFixtureBlock.propertiesCodec())
                    .apply((Applicative<Mu<LampFixtureBlock>, ?>) instance, LampFixtureBlock::new));

    public LampFixtureBlock(Properties properties) {
        super(properties);
    }

    public MapCodec<LampFixtureBlock> codec() {
        return CODEC;
    }
}
