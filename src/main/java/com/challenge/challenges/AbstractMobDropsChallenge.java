package com.challenge.challenges;


import com.challenge.events.LivingEntityEvents;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;


public abstract class AbstractMobDropsChallenge extends BaseChallenge {
    protected abstract ItemStack getItem(Level level, Player player, LivingEntity victim);
    
  @Override
  public void registerEventHandlers() {
    LivingEntityEvents.ON_LIVING_ENTITY_LOOT_DROP.register((victim, damageSource, causedByPlayer) -> {
      try{
        if(!this.isActive()) return false;
        if(!causedByPlayer) return false;
        Entity attacker = damageSource.getEntity();
        if(attacker == null) return false;
        // Even though causedByPlayer is true, it might not always be the case that the damageSource is also a player...
        // Probably happens in some cases, when e.g. creepers blow up other mobs.
        if(!(attacker instanceof Player)) return false;

        Player player = (Player) attacker;

        ItemStack itemStack = this.getItem(player.level(), player, victim);
        ItemEntity itemEntity = new ItemEntity(victim.level(), victim.getX(), victim.getY(), victim.getZ(), itemStack);
        itemEntity.setDefaultPickUpDelay();
        victim.level().addFreshEntity(itemEntity);

        // Event is fully processed. Don't drop default item.
        // TODO: This means, that we cannot have two instances of the this challenge running.
        return true;
    } catch(Exception e) {
      LOGGER.error("Exception occured while trying to replace mob drop for {}: {}", victim.toString(), e);
      // The mob was most probably not killed by a player, so continue to default mob drop.
      return false;
    }
    });
  }

  @Override
  public ItemStack getIndicatorItemStack() {
    if(this.isEnabled()) {
      return asEnchantedIndicatorItemStack(Items.NETHERITE_SWORD, Enchantments.LOOTING, 3);
    } else {
      return asIndicatorItemStack(Items.WOODEN_SWORD);
    }
  }
}
