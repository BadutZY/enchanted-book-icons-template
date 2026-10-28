package com.enchantbookicons;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.item.Item;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Items;

/**
 * Varian "nbt": Minecraft 1.19 - 1.20.4.
 *
 * Di versi ini enchantment buku disimpan sebagai NBT ("StoredEnchantments"), bukan data
 * component, jadi dibaca lewat EnchantedBookItem.getEnchantments().
 */
final class LegacyBookIcons {
    private LegacyBookIcons() {
    }

    static void register() {
        registerItemProperty(
                Items.ENCHANTED_BOOK,
                new ResourceLocation(EnchantBookIcons.MOD_ID, "icon"),
                (stack, level, entity, seed) -> {
                    ListTag stored = EnchantedBookItem.getEnchantments(stack);
                    // Buku kosong atau berisi lebih dari 1 enchantment -> texture bawaan.
                    if (stored.size() != 1) {
                        return 0.0F;
                    }
                    return BookIconOrder.iconValue(stored.getCompound(0).getString("id"));
                });
    }

    /**
     * ItemProperties.register(Item, ResourceLocation, ClampedItemPropertyFunction) bersifat private
     * di vanilla (Forge/NeoForge membukanya lewat patch, Fabric lewat Fabric API). Supaya satu kode
     * jalan di semua loader tanpa Fabric API, method dicari lewat TIPE parameter (bukan nama, karena
     * nama di Fabric runtime berbeda) lalu dipanggil dengan reflection.
     */
    private static void registerItemProperty(Item item, ResourceLocation id, ClampedItemPropertyFunction fn) {
        try {
            for (Method m : ItemProperties.class.getDeclaredMethods()) {
                Class<?>[] p = m.getParameterTypes();
                if (Modifier.isStatic(m.getModifiers()) && p.length == 3
                        && p[0] == Item.class && p[1] == ResourceLocation.class
                        && p[2].isAssignableFrom(ClampedItemPropertyFunction.class)) {
                    m.setAccessible(true);
                    m.invoke(null, item, id, fn);
                    return;
                }
            }
            throw new IllegalStateException("ItemProperties.register(Item, ResourceLocation, fn) tidak ditemukan");
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Gagal mendaftarkan item property", e);
        }
    }
}
