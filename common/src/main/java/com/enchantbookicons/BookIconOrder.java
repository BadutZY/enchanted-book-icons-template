package com.enchantbookicons;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Urutan enchantment untuk jalur lama (item property "enchantbookicons:icon", Minecraft 1.19 - 1.21.3).
 *
 * Nilai property = (nomor enchantment) / 100. Urutan ini HARUS sama dengan urutan "overrides"
 * di assets/minecraft/models/item/enchanted_book.json. Enchantment yang tidak ada di suatu
 * versi (misalnya Lunge di 1.20.x) tidak pernah muncul, jadi daftar yang sama aman dipakai
 * untuk semua versi.
 *
 * Kelas ini TIDAK memakai class Minecraft sama sekali.
 */
final class BookIconOrder {
    private static final List<String> ORDER = List.of(
            "protection",
            "fire_protection",
            "feather_falling",
            "blast_protection",
            "projectile_protection",
            "respiration",
            "aqua_affinity",
            "thorns",
            "depth_strider",
            "frost_walker",
            "soul_speed",
            "swift_sneak",
            "sharpness",
            "smite",
            "bane_of_arthropods",
            "knockback",
            "fire_aspect",
            "looting",
            "sweeping_edge",
            "efficiency",
            "silk_touch",
            "unbreaking",
            "fortune",
            "power",
            "punch",
            "flame",
            "infinity",
            "luck_of_the_sea",
            "lure",
            "loyalty",
            "impaling",
            "riptide",
            "channeling",
            "multishot",
            "quick_charge",
            "piercing",
            "density",
            "breach",
            "wind_burst",
            "lunge",
            "mending",
            "binding_curse",
            "vanishing_curse");

    private static final Map<String, Integer> INDEX = new HashMap<>();

    static {
        for (int i = 0; i < ORDER.size(); i++) {
            INDEX.put("minecraft:" + ORDER.get(i), i + 1);
        }
    }

    /**
     * Id lama -> id baru. Sebelum Minecraft 1.21, Sweeping Edge bernama "minecraft:sweeping".
     * Tanpa alias ini buku Sweeping Edge di 1.19 - 1.20.6 jatuh ke texture bawaan.
     */
    private static final Map<String, String> LEGACY_ALIASES = Map.of(
            "minecraft:sweeping", "minecraft:sweeping_edge");

    private BookIconOrder() {
    }

    /** Nilai item property untuk id enchantment (mis. "minecraft:sharpness"); 0 = texture bawaan. */
    static float iconValue(String enchantmentId) {
        if (enchantmentId == null || enchantmentId.isEmpty()) {
            return 0.0F;
        }
        String id = enchantmentId.indexOf(':') < 0 ? "minecraft:" + enchantmentId : enchantmentId;
        id = LEGACY_ALIASES.getOrDefault(id, id);
        Integer idx = INDEX.get(id);
        return idx == null ? 0.0F : idx / 100.0F;
    }
}
