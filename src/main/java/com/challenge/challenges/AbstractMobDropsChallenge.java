package com.challenge.challenges;


import com.challenge.events.LivingEntityEvents;
import com.challenge.utils.Helpers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;

public abstract class AbstractMobDropsChallenge extends BaseChallenge {
    protected abstract ItemStack getItem(World world, PlayerEntity player, LivingEntity victim);
    
  @Override
  public void registerEventHandlers() {
    LivingEntityEvents.ON_LIVING_ENTITY_LOOT_DROP.register((victim, damageSource, causedByPlayer) -> {
      try{
        if(!this.isActive()) return false;
        if(!causedByPlayer) return false;
        Entity attacker = damageSource.getAttacker();
        if(attacker == null) return false;
        // Even though causedByPlayer is true, it might not always be the case that the damageSource is also a player...
        // Probably happens in some cases, when e.g. creepers blow up other mobs.
        if(!attacker.isPlayer()) return false;
        // Just to be extra sure...
        if(!(attacker instanceof PlayerEntity)) return false;

        PlayerEntity player = (PlayerEntity) attacker;

        ItemStack itemStack = this.getItem(player.getWorld(), player, victim);
        ItemEntity itemEntity = new ItemEntity(victim.getWorld(), victim.getX(), victim.getY(), victim.getZ(), itemStack);
        itemEntity.setToDefaultPickupDelay();
        victim.getWorld().spawnEntity(itemEntity);

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
      ItemStack itemStack = Items.NETHERITE_SWORD.getDefaultStack();
      RegistryEntry<Enchantment> looting = Helpers.getEnchantment(this.getServer(), Enchantments.LOOTING);
      itemStack.addEnchantment(looting, 3);
      return itemStack;
    } else {
      return Items.WOODEN_SWORD.getDefaultStack();
    }
  }
}
