package net.zbavitel.frenderman.entity.client;

import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.model.EndermanEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.util.Identifier;
import net.zbavitel.frenderman.entity.custom.FrendermanEntity;

public class FrendermanRenderer
        extends MobEntityRenderer<FrendermanEntity,
        EndermanEntityModel<FrendermanEntity>> {

    /** Vanilla Enderman diffuse texture (no asset in your mod). */
    private static final Identifier VANILLA_ENDERMAN =
            Identifier.ofVanilla("textures/entity/enderman/enderman.png");

    public FrendermanRenderer(Context ctx) {
        super(ctx,
                new EndermanEntityModel<>(ctx.getPart(EntityModelLayers.ENDERMAN)),
                0.5f);                     // shadow radius
    }

    /* ► Yarn 1.20.5+ uses getTexture()
       ► Yarn ≤ 1.20.1 uses getTextureLocation()
       Keep the one that matches your mappings and delete the other. */
    @Override
    public Identifier getTexture(FrendermanEntity entity) {
        return VANILLA_ENDERMAN;
    }
    // If your IDE complains the method doesn’t exist, swap to:
    // @Override
    // public Identifier getTextureLocation(FrendermanEntity entity) {
    //     return VANILLA_ENDERMAN;
    // }
}
