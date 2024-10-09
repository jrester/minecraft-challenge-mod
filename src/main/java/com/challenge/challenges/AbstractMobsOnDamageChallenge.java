package com.challenge.challenges;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.entity.SpawnReason;

public abstract class AbstractMobsOnDamageChallenge extends BaseChallenge {
    protected abstract EntityType getMob(LivingEntity victim, PlayerEntity player);

    @Override
    public void registerEventHandlers() {
        ServerLivingEntityEvents.AFTER_DAMAGE.register((entity, source, baseDamageTaken, damageTaken, blocked) -> {
            if(!this.isActive()) return;
            if (entity.isPlayer()) return;
            if(source.getAttacker() == null) return;
            if (!source.getAttacker().isPlayer()) return;
            // Do not change ender dragon in the end
            ServerWorld world = (ServerWorld)entity.getWorld();
            if(entity.getType().equals(EntityType.ENDER_DRAGON) && world.getRegistryKey().equals(World.END)) return;

            // If we got this far, this means that
            // 1. The entity is alive and was not killed by the damage (this is implied by the event definition)
            // 2. The entity can be safely replaced

            PlayerEntity player = (PlayerEntity)source.getAttacker();
            EntityType replacementType = this.getMob(entity, player);
            BlockPos entityPos = entity.getBlockPos();
            LivingEntity replacement = (LivingEntity)replacementType.create(world, null, entityPos, SpawnReason.TRIGGERED, false, false);
            world.spawnEntity(replacement); 
            // This is the After Damage event, which means that the damage was already applied to the entity.
            // Therefore, health of the replacement is set to the entity health, and will be automatically set to the max health of the replacement
            // if the new health exceeds the replacements max health
            replacement.setHealth(entity.getHealth());
            entity.remove(RemovalReason.KILLED); 
        });
    }

    @Override
    public ItemStack getIndicatorItemStack() {
        if(isEnabled()) {
            ItemStack itemStack = Items.SPAWNER.getDefaultStack();
            RegistryEntry<Enchantment> fortune = getEnchantment(Enchantments.FORTUNE);
            itemStack.addEnchantment(fortune, 3);
            return itemStack;
        } else {
            return Items.SPAWNER.getDefaultStack();
        }
    }
    
}
