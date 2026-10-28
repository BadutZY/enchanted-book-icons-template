package com.enchantbookicons.forge;

import com.enchantbookicons.EnchantBookIcons;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

/** Entrypoint Forge. Hanya aktif di client; di dedicated server tidak melakukan apa pun. */
@Mod(EnchantBookIcons.MOD_ID)
public final class EnchantBookIconsForge {
    public EnchantBookIconsForge() {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
        }
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        // ItemProperties bukan thread-safe -> daftarkan di main thread.
        event.enqueueWork(EnchantBookIcons::initClient);
    }
}
