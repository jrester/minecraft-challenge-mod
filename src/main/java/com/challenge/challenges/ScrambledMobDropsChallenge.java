
package com.challenge.challenges;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.challenge.utils.ItemScrambler;

public class ScrambledMobDropsChallenge extends AbstractMobDropsChallenge {
  private final String name = "Scrambled Mob Drops ";
  private final ItemScrambler itemScrambler = new ItemScrambler();

  @Override
  protected ItemStack getItem(World world, PlayerEntity player, LivingEntity victim) {
      int victimTypeHash = Math.abs(victim.getType().hashCode());
      return this.itemScrambler.getScrambledForPlayer(victimTypeHash, player, this.getServer());
  }


  @Override
  public String getName() {
    return name;
  }
}
