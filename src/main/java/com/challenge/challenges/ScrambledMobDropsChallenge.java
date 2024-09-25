
package com.challenge.challenges;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;


import com.challenge.events.LivingEntityEvents;
import com.challenge.utils.Helpers;
import com.challenge.utils.Scrambler;

public class ScrambledMobDropsChallenge extends BaseChallenge {
  private final String name = "Scrambled Mob Drops ";

  private final Scrambler<Item> itemScrambler = new Scrambler<>(Helpers.collectAllItems());

  @Override
  public void registerEventHandlers() {
    LivingEntityEvents.ON_LIVING_ENTITY_LOOT_DROP.register((victim, damageSource, causedByPlayer) -> {
      if(!this.isActive()) return false;
      if(!causedByPlayer) return false;
      PlayerEntity player = (PlayerEntity) damageSource.getAttacker();

      int victimTypeHash = Math.abs(victim.getType().hashCode());
      Item item = this.itemScrambler.getScrambledForPlayer(victimTypeHash, player);
      ItemStack itemStack = new ItemStack(item);
      ItemEntity itemEntity = new ItemEntity(victim.getWorld(), victim.getX(), victim.getY(), victim.getZ(), itemStack);
      itemEntity.setToDefaultPickupDelay();
      victim.getWorld().spawnEntity(itemEntity);

      return true;
    });
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public ItemStack getIndicatorItemStack() {
    if(this.isEnabled()) {
      ItemStack itemStack = Items.NETHERITE_SWORD.getDefaultStack();
      RegistryEntry<Enchantment> looting = getEnchantment(Enchantments.LOOTING);
      itemStack.addEnchantment(looting, 3);
      return itemStack;
    } else {
      return Items.WOODEN_SWORD.getDefaultStack();
    }
  }
}
