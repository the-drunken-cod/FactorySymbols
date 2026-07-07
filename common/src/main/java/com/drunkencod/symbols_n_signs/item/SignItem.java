package com.drunkencod.symbols_n_signs.item;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.drunkencod.symbols_n_signs.Constants;
import com.drunkencod.symbols_n_signs.signs.SignType;
import com.drunkencod.symbols_n_signs.util.TooltipUtil;

import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class SignItem extends Item implements IExpandableTooltip {

    private final SignType signType;

    public SignItem(SignType signType, Properties properties) {
        super(properties);
        this.signType = signType;
    }

    public static @Nullable SignType getSignTypeFromItemStack(ItemStack stack) {
        if (stack.getItem() instanceof SignItem item)
            return item.getSignType();
        return null;
    }

    public SignType getSignType() {
        return signType;
    }

    @Override
    public List<Component> getExpandedTooltip(ItemStack stack) {
        SignType signType = getSignTypeFromItemStack(stack);
        if (signType == null)
            return List.of(Component.literal("Congrats, you managed to break my code!"));
        String signTooltipId = "sign." + Constants.MOD_ID + "." + signType.getId() + ".tooltip";
        if (Language.getInstance().has(signTooltipId))
            return List.of(TooltipUtil.tooltipLine(signTooltipId));
        return List.of();
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable(
                Constants.MOD_ID + ".sign_item_name_template",
                Component.translatable("sign." + Constants.MOD_ID + "." + signType.getId()));
    }
}
