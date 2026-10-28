package com.enchantbookicons;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.item.Item;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

/**
 * Varian "component_factory": Minecraft 1.21 - 1.21.3.
 * ResourceLocation dibuat lewat ResourceLocation.fromNamespaceAndPath.
 *
 * Enchantment buku dibaca dari data component STORED_ENCHANTMENTS. Jalur ini TIDAK dipakai
 * mulai 1.21.4 (ItemProperties / "overrides" dihapus Mojang).
 */
final class LegacyBookIcons {
    private LegacyBookIcons() {
    }

    static void register() {
        registerItemProperty(
                Items.ENCHANTED_BOOK,
                ResourceLocation.fromNamespaceAndPath(EnchantBookIcons.MOD_ID, "icon"),
                (stack, level, entity, seed) -> {
                    ItemEnchantments stored = stack.get(DataComponents.STORED_ENCHANTMENTS);
                    // Buku kosong atau berisi lebih dari 1 enchantment -> texture bawaan.
                    if (stored == null || stored.size() != 1) {
                        return 0.0F;
                    }
                    for (Holder<Enchantment> holder : stored.keySet()) {
                        var key = holder.unwrapKey();
                        if (key.isEmpty()) {
                            return 0.0F;
                        }
                        return BookIconOrder.iconValue(key.get().location().toString());
                    }
                    return 0.0F;
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
