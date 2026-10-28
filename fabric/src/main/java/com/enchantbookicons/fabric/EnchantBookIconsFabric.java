package com.enchantbookicons.fabric;

import com.enchantbookicons.EnchantBookIcons;
import net.fabricmc.api.ClientModInitializer;

/** Entrypoint Fabric (client). Tidak butuh Fabric API. */
public final class EnchantBookIconsFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EnchantBookIcons.initClient();
    }
}
