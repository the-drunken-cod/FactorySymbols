package com.drunkencod.symbols_n_signs.datagen;

import com.drunkencod.symbols_n_signs.Constants;
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
                .resolve("../../../../common/src/main/resources/assets/symbols_n_signs/lang/en_us.json")
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

        // #region Pass-through runtime keys (tab name, tag names, item name template,
        // material/symbol names)
        // Skip only comment keys; all other keys are needed at runtime.
        for (Map.Entry<String, JsonElement> entry : lang.entrySet()) {
            String key = entry.getKey();
            if (key.contains("__comment__")) {
                continue;
            }
            add(key, entry.getValue().getAsString());
        }
    }

}
