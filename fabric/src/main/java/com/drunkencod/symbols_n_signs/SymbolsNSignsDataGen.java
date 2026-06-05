package com.drunkencod.symbols_n_signs;

import com.drunkencod.symbols_n_signs.datagen.FabricItemModelProvider;
import com.drunkencod.symbols_n_signs.datagen.FabricItemTagsProvider;
import com.drunkencod.symbols_n_signs.datagen.FabricSymbolRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class SymbolsNSignsDataGen implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();
        pack.addProvider(FabricItemModelProvider::new);
        pack.addProvider(FabricSymbolRecipeProvider::new);
        pack.addProvider(FabricItemTagsProvider::new);
    }
}
