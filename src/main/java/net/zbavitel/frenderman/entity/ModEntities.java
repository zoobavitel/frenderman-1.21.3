package net.zbavitel.frenderman.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.entity.mob.EndermanEntity;
import net.zbavitel.frenderman.Frenderman1211;

public class ModEntities {

    // Define the custom TamedEndermanEntity
    public static final EntityType<TamedEndermanEntity> TAMED_ENDERMAN = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Frenderman1211.MOD_ID, "tamed_enderman"),
            FabricEntityTypeBuilder.createMob()
                    .entityFactory(TamedEndermanEntity::new) // Links to your custom entity class
                    .defaultAttributes(EndermanEntity::createEndermanAttributes) // Inherits Enderman attributes
                    .build()
    );

    // Method to register all entities
    public static void registerEntities() {
        Frenderman1211.LOGGER.info("Registering custom entities...");
    }
}
