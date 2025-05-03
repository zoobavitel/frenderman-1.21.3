package net.zbavitel.frenderman.entity.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.zbavitel.frenderman.entity.ModEntities;

public class frendermanClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.FRENDERMAN, FrendermanRenderer::new);
    }
}
