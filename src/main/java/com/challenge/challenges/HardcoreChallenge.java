package com.challenge.challenges;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;

public class HardcoreChallenge extends BaseChallenge {
  private final String name = "Hardcore";

  @Override
  public void registerEventHandlers() {
    ServerLivingEntityEvents.AFTER_DEATH.register(
        (entity, damageSource) -> {
          if (!this.isActive()) return;
          if (!(entity instanceof Player)) return;

          for (ServerPlayer player : this.getServer().getPlayerList().getPlayers()) {
            player.kill(player.level());
            player.setGameMode(GameType.SPECTATOR);
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
    if (this.isEnabled()) {
      return asIndicatorItemStack(Items.ENCHANTED_GOLDEN_APPLE);
    } else {
      return asIndicatorItemStack(Items.APPLE);
    }
  }
}
