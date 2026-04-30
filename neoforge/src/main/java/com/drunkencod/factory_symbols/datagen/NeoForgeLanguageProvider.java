package com.drunkencod.factory_symbols.datagen;

import com.drunkencod.factory_symbols.Constants;
import com.drunkencod.factory_symbols.symbols.SymbolMaterial;
import com.drunkencod.factory_symbols.symbols.SymbolType;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class NeoForgeLanguageProvider extends LanguageProvider {

    // #region Source lang file — resolved at datagen time relative to the output
    // folder
    private final Path sourceLangFile;

    public NeoForgeLanguageProvider(PackOutput output) {
        super(output, Constants.MOD_ID, "en_us");
        // output folder is neoforge/src/generated/resources/; resolve relative to
        // project root
        this.sourceLangFile = output.getOutputFolder()
                .resolve("../../../../common/src/main/resources/assets/factory_symbols/lang/en_us.json")
                .normalize();
    }

    @Override
    protected void addTranslations() {
        JsonObject lang;
        try {
            lang = new Gson().fromJson(Files.readString(sourceLangFile), JsonObject.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read source lang file: " + sourceLangFile, e);
        }

        // #region Pass-through runtime keys (tab name, tag names)
        // Skip datagen-source-only keys: the template, comments, and the
        // material/symbol part keys.
        for (Map.Entry<String, JsonElement> entry : lang.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith("_") || key.startsWith("factory_symbols.item_name_template")
                    || key.startsWith("material.") || key.startsWith("symbol.")) {
                continue;
            }
            add(key, entry.getValue().getAsString());
        }

        // #region Generated item keys
        String template = requireKey(lang, "factory_symbols.item_name_template");

        for (SymbolMaterial mat : SymbolMaterial.values()) {
            String materialKey = "material." + Constants.MOD_ID + "." + mat.getPrefix();
            String materialName = requireKey(lang, materialKey);

            for (SymbolType sym : SymbolType.values()) {
                String symbolKey = "symbol." + Constants.MOD_ID + "." + sym.getId();
                String symbolName = requireKey(lang, symbolKey);
                String itemName = template
                        .replace("${material_name}", materialName)
                        .replace("${symbol_name}", symbolName);
                add("item." + Constants.MOD_ID + ".symbol_" + mat.getPrefix() + "_" + sym.getId(), itemName);
            }
        }
    }

    private String requireKey(JsonObject lang, String key) {
        JsonElement element = lang.get(key);
        if (element == null)
            throw new IllegalStateException("Missing translation key '" + key + "' in " + sourceLangFile);
        return element.getAsString();
    }
}
