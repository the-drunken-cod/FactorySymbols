package com.drunkencod.symbols_n_signs.item;

import com.drunkencod.symbols_n_signs.Constants;
import com.drunkencod.symbols_n_signs.signs.SignType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class SignItem extends Item {

    private final SignType signType;

    public SignItem(SignType signType, Properties properties) {
        super(properties);
        this.signType = signType;
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable(
                "symbols_n_signs.sign_item_name_template",
                Component.translatable("sign." + Constants.MOD_ID + "." + signType.getId()));
    }
}
