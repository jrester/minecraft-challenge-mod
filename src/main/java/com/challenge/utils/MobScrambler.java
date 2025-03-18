package com.challenge.utils;

import java.util.List;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;

public class MobScrambler extends Scrambler<EntityType> {
  private final List<EntityType> allEntityTypes;

  public MobScrambler() {
    this.allEntityTypes = Helpers.collectAllSpawnableMobs();
  }

	@Override
	public EntityType getScrambledForPlayer(int itemModifier, PlayerEntity player, MinecraftServer server) {
	  int playerModifier = this.getModifierForPlayer(player);
    // TODO: Cap by mob index
    int totalModifier = Math.abs(itemModifier + playerModifier);

    EntityType entityType = this.allEntityTypes.get(totalModifier % this.allEntityTypes.size());
	  return entityType;
	}
}
