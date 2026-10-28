package com.enchantbookicons;

import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

/**
 * Minecraft 1.21.4: mengubah Enchanted Book (tepat 1 enchantment) menjadi salinan yang membawa
 * custom_model_data string = nama enchantment. Hanya dipakai untuk render; stack asli
 * (dan server) tidak disentuh.
 */
public final class BookIconCmd {
    private BookIconCmd() {
    }

    public static ItemStack apply(ItemStack stack) {
        try {
            if (stack == null || stack.isEmpty() || !stack.is(Items.ENCHANTED_BOOK)) {
                return stack;
            }
            // Hormati custom_model_data milik resource pack / mod lain.
            if (stack.get(DataComponents.CUSTOM_MODEL_DATA) != null) {
                return stack;
            }
            ItemEnchantments stored = stack.get(DataComponents.STORED_ENCHANTMENTS);
            if (stored == null || stored.size() != 1) {
                return stack;
            }
            for (Holder<Enchantment> holder : stored.keySet()) {
                var key = holder.unwrapKey();
                if (key.isEmpty() || !"minecraft".equals(key.get().location().getNamespace())) {
                    return stack;
                }
                String path = key.get().location().getPath();
                if (BookIconOrder.iconValue("minecraft:" + path) <= 0.0F) {
                    return stack;
                }
                ItemStack copy = stack.copy();
                copy.set(DataComponents.CUSTOM_MODEL_DATA,
                        new CustomModelData(List.of(), List.of(), List.of(path), List.of()));
                return copy;
            }
        } catch (Throwable t) {
            // Jangan pernah crash render hanya karena icon.
        }
        return stack;
    }
}
