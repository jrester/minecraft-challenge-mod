
package com.challenge.challenges;


import com.challenge.events.GameEvents;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;


public class SlayEnderDragonChallenge extends BaseChallenge {
  private final String name = "Slay Ender Dragon";

  @Override
  public void registerEventHandlers() {
    GameEvents.ON_ENDER_DRAGON_KILLED.register(() -> {
      if(!this.isActive()) return;
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
      ItemStack itemStack = Items.DRAGON_HEAD.getDefaultStack();
      return itemStack;
    } else {
      return Items.ENDER_PEARL.getDefaultStack();
    }
  }
}
