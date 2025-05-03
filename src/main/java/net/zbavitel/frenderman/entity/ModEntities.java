package net.zbavitel.frenderman.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.zbavitel.frenderman.Frenderman;
import net.zbavitel.frenderman.entity.custom.FrendermanEntity;   // <‑‑ correct import!

public final class ModEntities {

    public static final EntityType<FrendermanEntity> FRENDERMAN = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of(Frenderman.MOD_ID, "frenderman"),
            EntityType.Builder.create(FrendermanEntity::new, SpawnGroup.CREATURE)
                    .dimensions(0.6f, 2.9f).build());

    public static void registerModEntities() {
        Frenderman.LOGGER.info("Registering Mod Entities for " + Frenderman.MOD_ID);

        FabricDefaultAttributeRegistry.register(
                FRENDERMAN,
                EndermanEntity.createEndermanAttributes()
                        .add(EntityAttributes.GENERIC_ATTACK_DAMAGE) // optional but safe
        );
    }
}