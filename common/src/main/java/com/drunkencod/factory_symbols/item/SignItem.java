package com.drunkencod.factory_symbols.item;

import com.drunkencod.factory_symbols.Constants;
import com.drunkencod.factory_symbols.signs.SignType;
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
                "factory_symbols.sign_item_name_template",
                Component.translatable("sign." + Constants.MOD_ID + "." + signType.getId()));
    }
}
