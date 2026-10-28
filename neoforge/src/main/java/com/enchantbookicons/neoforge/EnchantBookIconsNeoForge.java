package com.enchantbookicons.neoforge;

import com.enchantbookicons.EnchantBookIcons;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;

/** Entrypoint NeoForge. Hanya aktif di client; di dedicated server tidak melakukan apa pun. */
@Mod(EnchantBookIcons.MOD_ID)
public final class EnchantBookIconsNeoForge {
    public EnchantBookIconsNeoForge(IEventBus modEventBus) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            modEventBus.addListener(this::onClientSetup);
        }
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        // ItemProperties bukan thread-safe -> daftarkan di main thread.
        event.enqueueWork(EnchantBookIcons::initClient);
    }
}
