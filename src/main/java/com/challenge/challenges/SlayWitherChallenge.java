package com.challenge.challenges;



import com.challenge.utils.Helpers;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;

public class SlayWitherChallenge extends BaseChallenge {
  private final String name = "Slay Wither";

  @Override
  public void registerEventHandlers() {
    ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
      if(!this.isActive()) return;
      if(!entity.getType().equals(EntityType.WITHER)) return;
      this.challengeFinished(true);
    });
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public ItemStack getIndicatorItemStack() {
    if(this.isEnabled()) {
      ItemStack itemStack = Items.NETHER_STAR.getDefaultStack();
      RegistryEntry<Enchantment> fortune = Helpers.getEnchantment(this.getServer(), Enchantments.POWER);
      itemStack.addEnchantment(fortune, 3);
      return itemStack;
    } else {
      return Items.WITHER_SKELETON_SKULL.getDefaultStack();
    }
  }
}
