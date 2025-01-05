package net.zbavitel.frenderman.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.entity.ai.goal.*;
import net.zbavitel.frenderman.item.ModItems;

import java.util.UUID;

public class TamedEndermanEntity extends EndermanEntity {
    private boolean isTamed;
    private UUID ownerUuid;
    private boolean sitting;

    public TamedEndermanEntity(EntityType<? extends EndermanEntity> entityType, World world) {
        super(entityType, world);
        this.isTamed = false;
        this.sitting = false;
    }

    // Getter for tamed status
    public boolean isTamed() {
        return isTamed;
    }

    // Setter for tamed status
    public void setTamed(boolean tamed) {
        this.isTamed = tamed;
    }

    // Getter for sitting status
    public boolean isSitting() {
        return sitting;
    }

    // Setter for sitting status
    public void setSitting(boolean sitting) {
        this.sitting = sitting;
    }

    // Getter for owner UUID
    public UUID getOwnerUuid() {
        return ownerUuid;
    }

    // Setter for owner UUID
    public void setOwner(PlayerEntity owner) {
        this.ownerUuid = owner.getUuid();
        this.setTamed(true);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        if (this.isTamed()) {
            this.goalSelector.add(1, new FollowOwnerGoal(this, 1.0, 10.0F, 2.0F)); // Follow owner
            this.targetSelector.add(2, new AttackWithOwnerGoal(this)); // Attack owner's target
            this.goalSelector.add(3, new WanderAroundFarGoal(this, 1.0)); // Wander when idle
            this.goalSelector.add(4, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F)); // Look at owner
            this.goalSelector.add(5, new SitGoal(this)); // Allow sitting
        } else {
            // Vanilla Enderman behavior for untamed Endermen
            this.goalSelector.add(1, new SwimGoal(this));
            this.goalSelector.add(2, new MeleeAttackGoal(this, 1.0, false));
            this.goalSelector.add(3, new WanderAroundFarGoal(this, 1.0, 0.0F));
            this.goalSelector.add(4, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
            this.goalSelector.add(5, new LookAroundGoal(this));
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.isTamed() && !this.isSitting() && this.ownerUuid != null) {
            PlayerEntity owner = this.getWorld().getPlayerByUuid(this.ownerUuid);
            if (owner != null && this.squaredDistanceTo(owner) > 100.0) { // Teleport if too far
                this.teleport(owner.getX(), owner.getY(), owner.getZ());
            }
        }
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        // Check if the item is a flower (you can use the minecraft:flowers tag)
        return stack.isIn(ItemTags.FLOWERS);
    }


    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);

        // Taming logic
        if (!this.isTamed() && itemStack.isOf(ModItems.COPPER_COIN)) {
            itemStack.decrement(1); // Consume the item
            if (this.random.nextInt(3) == 0) { // 1 in 3 chance of taming
                this.setOwner(player);
                player.sendMessage(Text.literal("The Enderman is now tamed!"), true);
                return ActionResult.SUCCESS;
            } else {
                player.sendMessage(Text.literal("Taming failed!"), true);
                return ActionResult.FAIL;
            }
        }

        // Healing logic
        if (this.isTamed() && itemStack.isIn(ItemTags.FLOWERS)) {
            this.heal(4.0F); // Heal 4 health points
            itemStack.decrement(1); // Consume the flower
            this.getWorld().addParticle(ParticleTypes.HEART, this.getX(), this.getY() + 1.0, this.getZ(), 0, 0, 0);
            player.sendMessage(Text.literal("The Enderman has been healed!"), true);
            return ActionResult.SUCCESS;
        }

        // Sitting toggle
        if (this.isTamed() && itemStack.isEmpty()) {
            this.setSitting(!this.isSitting());
            player.sendMessage(Text.literal(this.isSitting() ? "The Enderman is sitting." : "The Enderman is moving."), true);
            return ActionResult.SUCCESS;
        }

        return super.interactMob(player, hand);
    }
}
