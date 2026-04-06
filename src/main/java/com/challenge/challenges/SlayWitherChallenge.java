package com.challenge.challenges;




import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;

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
      return asEnchantedIndicatorItemStack(Items.NETHER_STAR, Enchantments.POWER, 3);
    } else {
      return asIndicatorItemStack(Items.WITHER_SKELETON_SKULL);
    }
  }
}
