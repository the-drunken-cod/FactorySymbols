package com.drunkencod.factory_symbols.datagen;

import com.drunkencod.factory_symbols.Constants;
import com.drunkencod.factory_symbols.symbols.SymbolMaterial;
import com.drunkencod.factory_symbols.symbols.SymbolType;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class NeoForgeItemModelProvider extends ItemModelProvider {

    public NeoForgeItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (SymbolMaterial mat : SymbolMaterial.values()) {
            String prefix = mat.getPrefix();

            // #region Base background model for this material
            withExistingParent("base_" + prefix, "minecraft:item/generated")
                    .texture("layer0", modLoc("item/bg_" + prefix));

            // #region Template item model
            withExistingParent("template_" + prefix, "minecraft:item/generated")
                    .texture("layer0", modLoc("item/template_" + prefix));

            // #region Symbol item models — two layers: background + symbol foreground
            String fgSuffix = mat.isLightForeground() ? "_white" : "_black";
            for (SymbolType sym : SymbolType.values()) {
                withExistingParent("symbol_" + prefix + "_" + sym.getId(), modLoc("item/base_" + prefix))
                        .texture("layer1", modLoc("item/symbols/" + sym.getId() + fgSuffix));
            }
        }
    }
}
