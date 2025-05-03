package net.zbavitel.frenderman.mixin;

import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.zbavitel.frenderman.util.MiscRegisters;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin {

    @Inject(method = "createMobAttributes", at = @At("RETURN"), cancellable = true)
    private static void addAttackDamage(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        DefaultAttributeContainer.Builder builder = cir.getReturnValue();
        builder.add(EntityAttributes.GENERIC_ATTACK_DAMAGE);
        cir.setReturnValue(builder);
    }

    @Inject(method = "initialize", at = @At("RETURN"))
    private void replaceEnderman(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, CallbackInfoReturnable<EntityData> cir) {
        MobEntity entity = (MobEntity)(Object)this;

        if (entity.getType() == EntityType.ENDERMAN &&
                MiscRegisters.replaceWith.containsKey(EntityType.ENDERMAN) &&
                MiscRegisters.shouldReplace(EntityType.ENDERMAN, entity.getRandom())) {
            MiscRegisters.addEntityToReplace((LivingEntity) entity);
        }
    }
}