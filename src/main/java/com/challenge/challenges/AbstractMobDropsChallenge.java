package com.challenge.challenges;


import com.challenge.events.LivingEntityEvents;
import com.challenge.utils.Helpers;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
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
      if(!this.isActive()) return false;
      if(!causedByPlayer) return false;
      PlayerEntity player = (PlayerEntity) damageSource.getAttacker();
      if(player == null) return false;

      ItemStack itemStack = this.getItem(player.getWorld(), player, victim);
      ItemEntity itemEntity = new ItemEntity(victim.getWorld(), victim.getX(), victim.getY(), victim.getZ(), itemStack);
      itemEntity.setToDefaultPickupDelay();
      victim.getWorld().spawnEntity(itemEntity);

      return true;
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
