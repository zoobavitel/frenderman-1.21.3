package net.zbavitel.frenderman.util;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;

import java.util.List;
import java.util.Map;

public class ServerTickEventHandler implements ServerTickEvents.EndTick {
    @Override
    public void onEndTick(MinecraftServer server) {
        for (ServerWorld world : server.getWorlds()) {
            List<LivingEntity> toReplace = MiscRegisters.getEntitiesToReplace(world);
            for (LivingEntity entity : toReplace) {
                MobEntity replacement = MiscRegisters.createReplacement((MobEntity) entity, world);
                if (replacement != null) {
                    world.spawnEntity(replacement);
                    entity.remove(Entity.RemovalReason.DISCARDED);
                }
            }
        }
        MiscRegisters.resetReplacements();
    }
}
