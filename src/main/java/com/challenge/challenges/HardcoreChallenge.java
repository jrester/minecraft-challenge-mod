package com.challenge.challenges;


import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;

public class HardcoreChallenge extends BaseChallenge {
  private final String name = "Hardcore";


  @Override
  public void registerEventHandlers() {
    ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
      if(!this.isActive()) return;
      if (!entity.isPlayer()) return;

      for(ServerPlayerEntity player : entity.getServer().getPlayerManager().getPlayerList()) {
        player.kill();
        player.changeGameMode(GameMode.SPECTATOR);
      }

      this.challengeFinished(false);
    });
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public ItemStack getIndicatorItemStack() {
    if(this.isEnabled()) {
      return Items.ENCHANTED_GOLDEN_APPLE.getDefaultStack();
    } else {
      return Items.APPLE.getDefaultStack();
    }
  }
}
