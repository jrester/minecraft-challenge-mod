package com.challenge.challenges;


import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

public abstract class AbstractMobsOnDamageChallenge extends BaseChallenge {
    protected abstract EntityType getMob(LivingEntity victim, Player layer);

    @Override
    public void registerEventHandlers() {
        ServerLivingEntityEvents.AFTER_DAMAGE.register((entity, source, baseDamageTaken, damageTaken, blocked) -> {
            try{
                if(!this.isActive()) return;
                if(entity instanceof Player) return;
                if(source.getEntity() == null) return;
                if (!(source.getEntity() instanceof Player)) return;
                // Do not change ender dragon in the end
                Level level = entity.level();
                if(entity.getType().equals(EntityType.ENDER_DRAGON) && level.dimensionType().hasEnderDragonFight()) return;

                // If we got this far, this means that
                // 1. The entity is alive and was not killed by the damage (this is implied by the event definition)
                // 2. The entity can be safely replaced

                 
                Player player = (Player)source.getEntity();
                EntityType replacementType = this.getMob(entity, player);
                BlockPos entityPos = entity.getOnPos();
                LivingEntity replacement = (LivingEntity)replacementType.spawn((ServerLevel)level, entityPos.above(), EntitySpawnReason.TRIGGERED);
                
                // This is the After Damage event, which means that the damage was already applied to the entity.
                // Therefore, health of the replacement is set to the entity health, and will be automatically set to the max health of the replacement
                // if the new health exceeds the replacements max health
                replacement.setHealth(entity.getHealth());
                replacement.setArrowCount(entity.getArrowCount());
                entity.remove(RemovalReason.KILLED);
            } catch(Exception e) {
                  LOGGER.error("Exception occured while trying to replace entity {}: {}", entity.toString(), e);
                  return;
            }
        });
    }

    @Override
    public ItemStack getIndicatorItemStack() {
        if(isEnabled()) {
            return asEnchantedIndicatorItemStack(Items.SPAWNER, Enchantments.FORTUNE, 3);
        } else {
            return asIndicatorItemStack(Items.SPAWNER);
        }
    }
    
}
