package net.zbavitel.frenderman;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.EndermanEntityRenderer; // Import for vanilla Enderman renderer
import net.zbavitel.frenderman.entity.ModEntities;

public class frendermanClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Register the renderer for TamedEndermanEntity
        EntityRendererRegistry.register(ModEntities.TAMED_ENDERMAN, EndermanEntityRenderer::new);
    }
}
