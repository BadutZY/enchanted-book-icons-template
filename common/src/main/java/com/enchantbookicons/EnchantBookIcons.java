package com.enchantbookicons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Titik masuk bersama untuk semua loader (Fabric, Forge, NeoForge).
 *
 * Kode Java hanya dipakai di Minecraft 1.19 - 1.21.3, karena di versi itu model item memakai
 * "overrides" dan nilainya harus disuplai lewat item property. Mulai 1.21.5 semuanya murni data
 * (assets/minecraft/items/enchanted_book.json), jadi profil build-nya tidak berisi kode sama sekali.
 */
public final class EnchantBookIcons {
    public static final String MOD_ID = "enchantbookicons";
    private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private EnchantBookIcons() {
    }

    /** Dipanggil oleh tiap loader pada tahap setup client. */
    public static void initClient() {
        try {
            LegacyBookIcons.register();
        } catch (Throwable t) {
            // Jangan sampai game crash hanya karena icon gagal didaftarkan.
            LOGGER.error("Gagal mendaftarkan icon Enchanted Book", t);
        }
    }
}
