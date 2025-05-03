package net.zbavitel.frenderman.util;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.server.world.ServerWorld;
import net.zbavitel.frenderman.entity.ModEntities;

import java.util.*;

public class MiscRegisters {
    public static final Map<EntityType<?>, EntityType<? extends MobEntity>> replaceWith = new HashMap<>();
    public static final Map<EntityType<?>, Integer> replaceChance = new HashMap<>();

    private static final Map<ServerWorld, List<LivingEntity>> entitiesToReplace = new HashMap<>();

    public static void initializeReplacements() {
        addReplacements(EntityType.ENDERMAN, ModEntities.FRENDERMAN, 100); // 100% chance
    }

    public static void addReplacements(EntityType<?> sourceType, EntityType<? extends MobEntity> replaceType, int chance) {
        //noinspection unchecked
        replaceWith.put((EntityType<? extends MobEntity>) sourceType, replaceType);
        replaceChance.put(sourceType, chance);
    }

    public static boolean shouldReplace(EntityType<?> entityType, Random random) {
        Integer chance = replaceChance.get(entityType);
        return chance != null && random.nextInt(100) < chance;
    }

    public static MobEntity createReplacement(MobEntity original, World world) {
        EntityType<? extends MobEntity> replacementType = replaceWith.get(original.getType());
        if (replacementType != null) {
            MobEntity replacement = replacementType.create(world);
            if (replacement != null) {
                replacement.refreshPositionAndAngles(
                        original.getX(), original.getY(), original.getZ(),
                        original.getYaw(), original.getPitch()
                );
                return replacement;
            }
        }
        return null;
    }

    public static void addEntityToReplace(LivingEntity entity) {
        if (!(entity.getWorld() instanceof ServerWorld serverWorld)) return;
        entitiesToReplace.computeIfAbsent(serverWorld, w -> new ArrayList<>()).add(entity);
    }

    public static List<LivingEntity> getEntitiesToReplace(ServerWorld world) {
        return entitiesToReplace.getOrDefault(world, Collections.emptyList());
    }

    public static void resetReplacements() {
        entitiesToReplace.clear();
    }
}
