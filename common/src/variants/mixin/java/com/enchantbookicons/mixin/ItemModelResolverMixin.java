package com.enchantbookicons.mixin;

import com.enchantbookicons.BookIconCmd;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * Minecraft 1.21.4. Semua jalur render item (inventory, tangan, dropped item, item frame)
 * lewat ItemModelResolver. Argumen ItemStack diganti dengan salinan yang membawa
 * custom_model_data. require = 0: kalau nama method berubah, mixin dilewati (icon bawaan),
 * game tidak crash.
 */
@Mixin(ItemModelResolver.class)
public abstract class ItemModelResolverMixin {
    @ModifyVariable(
            method = {"updateForTopItem", "updateForLiving", "updateForNonLiving"},
            at = @At("HEAD"),
            argsOnly = true,
            require = 0)
    private ItemStack enchantbookicons$withIconData(ItemStack stack) {
        return BookIconCmd.apply(stack);
    }
}
