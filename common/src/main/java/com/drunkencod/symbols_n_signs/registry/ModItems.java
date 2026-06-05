package com.drunkencod.symbols_n_signs.registry;

import com.drunkencod.symbols_n_signs.block.display_panel.DisplayPanelBlock;
import com.drunkencod.symbols_n_signs.item.RatchetWrenchItem;
import com.drunkencod.symbols_n_signs.item.RetroreflectiveSheetItem;
import com.drunkencod.symbols_n_signs.item.SignItem;
import com.drunkencod.symbols_n_signs.item.SymbolItem;
import com.drunkencod.symbols_n_signs.platform.Services;
import com.drunkencod.symbols_n_signs.signs.SignType;
import com.drunkencod.symbols_n_signs.symbols.SymbolCategory;
import com.drunkencod.symbols_n_signs.symbols.SymbolMaterial;
import com.drunkencod.symbols_n_signs.symbols.SymbolType;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomModelData;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

public class ModItems {

    // #region Symbol items — one item per material × symbol
    public static final Map<SymbolMaterial, Map<SymbolType, Supplier<Item>>> SYMBOLS = new EnumMap<>(
            SymbolMaterial.class);

    // #region Sign items
    public static final Map<SignType, Supplier<Item>> SIGN = new EnumMap<>(SignType.class);

    // #region R.R. Sheet
    public static Supplier<Item> RETROREFLECTIVE_SHEET;

    // #region Ratchet Wrench
    public static Supplier<Item> RATCHET_WRENCH;

    public static void register() {
        // register symbol items from enums:
        for (SymbolMaterial mat : SymbolMaterial.values()) {
            Map<SymbolType, Supplier<Item>> symMap = new EnumMap<>(SymbolType.class);
            for (SymbolType sym : SymbolType.values()) {
                Supplier<Item> item = Services.REGISTRY.registerItem(
                        sym.getId() + "_" + mat.getPrefix(),
                        () -> new SymbolItem(mat, sym, new Item.Properties()));
                symMap.put(sym, item);
            }
            SYMBOLS.put(mat, symMap);
        }

        // retroreflective iron sheet:
        RETROREFLECTIVE_SHEET = Services.REGISTRY.registerItem(
                RetroreflectiveSheetItem.ID,
                () -> new RetroreflectiveSheetItem(new Item.Properties()));

        // ratchet wrench:
        RATCHET_WRENCH = Services.REGISTRY.registerItem(
                RatchetWrenchItem.ID,
                () -> new RatchetWrenchItem(new Item.Properties()));

        // register sign items from enums:
        for (SignType sym : SignType.values()) {
            Supplier<Item> item = Services.REGISTRY.registerItem(
                    "sign_" + sym.getId(),
                    () -> new SignItem(sym, new Item.Properties()));
            SIGN.put(sym, item);
        }
    }

    public static ItemStack getSymbolStack(SymbolMaterial mat, SymbolType sym) {
        return SYMBOLS.get(mat).get(sym).get().getDefaultInstance();
    }

    public static ItemStack getSignStack(SignType sym) {
        return SIGN.get(sym).get().getDefaultInstance();
    }

    // #region Creative tabs
    public static void populateBlocksTab(CreativeModeTab.Output output) {
        output.accept(RATCHET_WRENCH.get().getDefaultInstance());
        output.accept(ModBlocks.SIGN_POST_ITEM.get().getDefaultInstance());
        output.accept(ModBlocks.SIGN_POST_BUTTON_FIXTURE_ITEM.get().getDefaultInstance());
        output.accept(ModBlocks.SIGN_POST_LAMP_FIXTURE_ITEM.get().getDefaultInstance());
        output.accept(ModBlocks.SIGN_POST_REDSTONE_EMITTER_FIXTURE_ITEM.get().getDefaultInstance());

        for (int i : DisplayPanelBlock.COLORS_ORDERED) {
            ItemStack itm = ModBlocks.DISPLAY_PANEL_ITEM.get().getDefaultInstance();
            itm.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(i));
            output.accept(itm);
        }
    }

    public static void populateSymbolsTab(CreativeModeTab.Output output) {
        for (SymbolMaterial mat : SymbolMaterial.values())
            for (SymbolCategory cat : SymbolCategory.values())
                for (SymbolType sym : SymbolType.values())
                    if (sym.getCategory() == cat)
                        output.accept(getSymbolStack(mat, sym));
    }

    public static void populateSignsTab(CreativeModeTab.Output output) {
        output.accept(RATCHET_WRENCH.get().getDefaultInstance());
        output.accept(RETROREFLECTIVE_SHEET.get().getDefaultInstance());

        for (SignType sym : SignType.values())
            output.accept(getSignStack(sym));
    }
}
