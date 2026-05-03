package com.challenge.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;

public class Scrambler<T> {
  protected Random random = new Random();
  private final Selector<T> selector;

  private Map<String, Integer> playerModifiers = new HashMap<>();

  public Scrambler(Selector<T> selector) {
    this.selector = selector;
  }

  protected int getModifierForPlayer(Player player) {
    String uuid = player.getStringUUID();
    int modifier;
    if (!playerModifiers.containsKey(uuid)) {
      modifier = Math.abs(this.random.nextInt());
      this.playerModifiers.put(uuid, modifier);
    } else {
      modifier = this.playerModifiers.get(uuid).intValue();
    }
    return modifier;
  }

  public T getScrambledForPlayer(int itemModifier, Player player, MinecraftServer server) {
    int playerModifier = this.getModifierForPlayer(player);
    int totalModifier = Math.abs(itemModifier * playerModifier + playerModifier);
    return this.selector.selectWithModifier(totalModifier);
  }
}
