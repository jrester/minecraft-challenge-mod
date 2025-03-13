
package com.challenge.challenges;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;

import net.minecraft.world.World;

import com.challenge.utils.Helpers;
import com.challenge.utils.Scrambler;

public class ScrambledMobDropsChallenge extends AbstractMobDropsChallenge {
  private final String name = "Scrambled Mob Drops ";

  private final Scrambler<Item> itemScrambler = new Scrambler<>(Helpers.collectAllItems());

  @Override
  protected Item getItem(World world, PlayerEntity player, LivingEntity victim) {
      int victimTypeHash = Math.abs(victim.getType().hashCode());
      return this.itemScrambler.getScrambledForPlayer(victimTypeHash, player);
  }


  @Override
  public String getName() {
    return name;
  }
}
