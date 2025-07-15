package net.zbavitel.frenderman.entity.custom;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.village.Merchant;
import net.minecraft.village.TradeOffers;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.zbavitel.frenderman.common.item.ModItems;
import net.zbavitel.frenderman.common.trades.TradeFactoryRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class FrendermanEntity extends EndermanEntity implements Merchant {

    private static final TrackedData<Boolean> TAMED = DataTracker.registerData(FrendermanEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Optional<UUID>> OWNER_UUID = DataTracker.registerData(FrendermanEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
    private static final TrackedData<Integer> ROLE = DataTracker.registerData(FrendermanEntity.class, TrackedDataHandlerRegistry.INTEGER);

    private PlayerEntity customer;
    private TradeOfferList tradeOffers;

    public enum EndermanRole {
        EXPLORER,
        ENCHANTER,
        MERCHANT,
        MINER
    }

    public FrendermanEntity(EntityType<? extends EndermanEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(TAMED, false);
        builder.add(OWNER_UUID, Optional.empty());
        builder.add(ROLE, -1);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("Tamed", this.isTamed());
        if (this.getOwnerUuid() != null) {
            nbt.putUuid("Owner", this.getOwnerUuid());
        }
        if (this.getRole() != null) {
            nbt.putInt("Role", this.getRole().ordinal());
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setTamed(nbt.getBoolean("Tamed"));
        if (nbt.containsUuid("Owner")) {
            this.setOwnerUuid(nbt.getUuid("Owner"));
        }
        if (nbt.contains("Role")) {
            int roleId = nbt.getInt("Role");
            if (roleId >= 0 && roleId < EndermanRole.values().length) {
                this.setRole(EndermanRole.values()[roleId]);
            }
        }
    }

    public boolean isTamed() {
        return this.dataTracker.get(TAMED);
    }

    public void setTamed(boolean tamed) {
        this.dataTracker.set(TAMED, tamed);
    }

    @Nullable
    public UUID getOwnerUuid() {
        return this.dataTracker.get(OWNER_UUID).orElse(null);
    }

    public void setOwnerUuid(@Nullable UUID uuid) {
        this.dataTracker.set(OWNER_UUID, Optional.ofNullable(uuid));
    }

    @Nullable
    public LivingEntity getOwner() {
        try {
            UUID uuid = this.getOwnerUuid();
            return uuid == null ? null : this.getWorld().getPlayerByUuid(uuid);
        } catch (IllegalArgumentException var2) {
            return null;
        }
    }

    public EndermanRole getRole() {
        int roleId = this.dataTracker.get(ROLE);
        return roleId == -1 ? null : EndermanRole.values()[roleId];
    }

    public void setRole(EndermanRole role) {
        this.dataTracker.set(ROLE, role.ordinal());
    }

    @Override
    protected void initGoals() {
        if (!this.isTamed()) {
            super.initGoals();
        } else {
            this.goalSelector.add(0, new SwimGoal(this));
            this.goalSelector.add(1, new MeleeAttackGoal(this, 1.2, true));
            this.goalSelector.add(2, new FollowEntityGoal(this, 1.0, 10.0f, 2.0f));
            this.goalSelector.add(3, new WanderAroundFarGoal(this, 1.0));
            this.goalSelector.add(4, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
            this.goalSelector.add(5, new LookAroundGoal(this));
        }
    }

    class FollowEntityGoal extends Goal {
        private final FrendermanEntity entity;
        private LivingEntity target;
        private final double speed;
        private final float maxDistance;
        private final float minDistance;
        private int updateCountdownTicks;

        public FollowEntityGoal(FrendermanEntity entity, double speed, float maxDistance, float minDistance) {
            this.entity = entity;
            this.speed = speed;
            this.maxDistance = maxDistance;
            this.minDistance = minDistance;
        }

        @Override
        public boolean canStart() {
            LivingEntity owner = entity.getOwner();
            return owner != null && !owner.isSpectator() && entity.squaredDistanceTo(owner) >= (minDistance * minDistance);
        }

        @Override
        public boolean shouldContinue() {
            return target != null && !target.isSpectator() && entity.squaredDistanceTo(target) > (minDistance * minDistance);
        }

        @Override
        public void start() {
            updateCountdownTicks = 0;
        }

        @Override
        public void stop() {
            target = null;
            entity.getNavigation().stop();
        }

        @Override
        public void tick() {
            if (target == null) return;
            if (--updateCountdownTicks <= 0) {
                updateCountdownTicks = 10;
                entity.getNavigation().startMovingTo(target, speed);
            }
        }
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);

        if (!this.isTamed() && itemStack.getItem() == ModItems.COPPER_COIN) {
            if (!player.getAbilities().creativeMode) itemStack.decrement(1);
            this.setTamed(true);
            this.setOwnerUuid(player.getUuid());
            this.setRole(EndermanRole.values()[random.nextInt(EndermanRole.values().length)]);
            this.setTarget(null);
            this.getNavigation().stop();
            this.getWorld().sendEntityStatus(this, (byte) 7);
            return ActionResult.SUCCESS;
        }

        if (this.isTamed() && player.getUuid().equals(this.getOwnerUuid())) {
            if (itemStack.isIn(net.minecraft.registry.tag.ItemTags.FLOWERS) && this.getHealth() < this.getMaxHealth()) {
                this.heal(4.0F);
                if (!player.getAbilities().creativeMode) itemStack.decrement(1);
                this.getWorld().sendEntityStatus(this, (byte) 7);
                return ActionResult.SUCCESS;
            }

            if (!player.getWorld().isClient &&
                    !itemStack.isOf(ModItems.COPPER_COIN) &&
                    !itemStack.isIn(net.minecraft.registry.tag.ItemTags.FLOWERS)) {
                this.setCustomer(player);
                this.sendOffers(player, this.getDisplayName(), 1);
                return ActionResult.SUCCESS;
            }
        }

        return super.interactMob(player, hand);
    }

    @Override
    public void tickMovement() {
        if (this.getWorld().isClient) {
            for (int i = 0; i < 2; i++) {
                this.getWorld().addParticle(
                        ParticleTypes.PORTAL,
                        this.getParticleX(0.5),
                        this.getRandomBodyY() - 0.25,
                        this.getParticleZ(0.5),
                        (this.random.nextDouble() - 0.5) * 2.0,
                        -this.random.nextDouble(),
                        (this.random.nextDouble() - 0.5) * 2.0
                );
            }
        }

        this.jumping = false;

        if (!this.getWorld().isClient) {
            this.tickAngerLogic((ServerWorld) this.getWorld(), true);
            if (this.isTamed()) {
                LivingEntity owner = this.getOwner();
                if (owner != null && owner.squaredDistanceTo(this) > 100) {
                    this.teleportToOwner(owner.getX(), owner.getY(), owner.getZ());
                }
            }
        }

        super.tickMovement();
    }

    protected boolean teleportToOwner(double x, double y, double z) {
        BlockPos.Mutable mutable = new BlockPos.Mutable(x, y, z);
        while (mutable.getY() > this.getWorld().getBottomY() && !this.getWorld().getBlockState(mutable).blocksMovement()) {
            mutable.move(Direction.DOWN);
        }

        BlockState blockState = this.getWorld().getBlockState(mutable);
        if (blockState.blocksMovement() && !blockState.getFluidState().isIn(FluidTags.WATER)) {
            Vec3d oldPos = this.getPos();
            boolean success = this.teleport(x, y, z, true);
            if (success) {
                this.getWorld().emitGameEvent(GameEvent.TELEPORT, oldPos, GameEvent.Emitter.of(this));
                if (!this.isSilent()) {
                    this.getWorld().playSound(null, this.prevX, this.prevY, this.prevZ, SoundEvents.ENTITY_ENDERMAN_TELEPORT, this.getSoundCategory(), 1.0F, 1.0F);
                    this.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
                }
            }
            return success;
        }
        return false;
    }

    // === Merchant Trading ===
    @Override
    public TradeOfferList getOffers() {
        if (this.tradeOffers == null) {
            initTradeOffers();
        }
        return this.tradeOffers;
    }

    @Override
    public void setOffersFromServer(TradeOfferList offers) {
        
    }

    private void initTradeOffers() {
        this.tradeOffers = new TradeOfferList();
        EndermanRole role = getRole();
        if (role == null) return;

        TradeOffers.Factory[] factories = TradeFactoryRegistry.getFactoriesForRole(role.name());
        for (TradeOffers.Factory factory : factories) {
            TradeOffer offer = factory.create(this, this.random);
            if (offer != null) {
                this.tradeOffers.add(offer);
            }
        }
    }

    @Override
    public void setCustomer(@Nullable PlayerEntity customer) {
        this.customer = customer;
    }

    @Override
    public PlayerEntity getCustomer() {
        return this.customer;
    }

    @Override
    public void trade(TradeOffer offer) {
        offer.use();
    }

    @Override
    public void onSellingItem(ItemStack stack) {}

    @Override
    public int getExperience() {
        return 0;
    }

    @Override
    public void setExperienceFromServer(int experience) {

    }


    @Override
    public boolean isLeveledMerchant() {
        return false;
    }

    @Override
    public SoundEvent getYesSound() {
        return SoundEvents.ENTITY_ENDERMAN_AMBIENT;
    }

    @Override
    public boolean isClient() {
        return false;
    }
}
