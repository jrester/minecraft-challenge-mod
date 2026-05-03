
package com.challenge.challenges;

import com.challenge.utils.ItemScrambler;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ScrambledMobDropsChallenge extends AbstractMobDropsChallenge {
  private final String name = "Scrambled Mob Drops ";
  private ItemScrambler itemScrambler = null;

  @Override
  public void enable() {
    super.enable();
    this.itemScrambler = new ItemScrambler(getServer());
  }

  @Override
  protected ItemStack getItem(Level level, Player player, LivingEntity victim) {
      int victimTypeHash = Math.abs(victim.getType().hashCode());
      return this.itemScrambler.getScrambledForPlayer(victimTypeHash, player, this.getServer());
  }


  @Override
  public String getName() {
    return name;
  }
}
