package com.drunkencod.factory_symbols;

import com.drunkencod.factory_symbols.datagen.FabricItemModelProvider;
import com.drunkencod.factory_symbols.datagen.FabricItemTagsProvider;
import com.drunkencod.factory_symbols.datagen.FabricSymbolRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class FactorySymbolsDataGen implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();
        pack.addProvider(FabricItemModelProvider::new);
        pack.addProvider(FabricSymbolRecipeProvider::new);
        pack.addProvider(FabricItemTagsProvider::new);
    }
}
