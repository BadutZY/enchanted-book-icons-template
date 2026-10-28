package com.enchantbookicons;

/**
 * Varian "mixin": Minecraft 1.21.4 saja.
 *
 * 1.21.4 sudah menghapus "overrides" + ItemProperties, tetapi item model definition belum punya
 * select property untuk data component (baru ada di 1.21.5). Jadi tidak ada yang perlu
 * didaftarkan di sini: icon dipilih oleh ItemModelResolverMixin yang menaruh nama enchantment
 * ke custom_model_data (string index 0) sebelum item dirender, lalu
 * assets/minecraft/items/enchanted_book.json (varian ini) memilih model dari string itu.
 */
final class LegacyBookIcons {
    private LegacyBookIcons() {
    }

    static void register() {
        // Sengaja kosong.
    }
}
