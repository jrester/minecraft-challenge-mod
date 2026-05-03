package com.challenge.utils;

import java.util.List;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;

public class MobScrambler extends Scrambler<EntityType> {
  private final List<EntityType> allEntityTypes;

  public MobScrambler() {
    this.allEntityTypes = Helpers.collectAllSpawnableMobs();
  }

	@Override
	public EntityType getScrambledForPlayer(int itemModifier, Player player, MinecraftServer server) {
	  int playerModifier = this.getModifierForPlayer(player);
    // TODO: Cap by mob index
    int totalModifier = Math.abs(itemModifier + playerModifier);

    EntityType entityType = this.allEntityTypes.get(totalModifier % this.allEntityTypes.size());
	  return entityType;
	}
}
