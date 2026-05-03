
package com.challenge.challenges;

import com.challenge.utils.CategorizedItemSelector;
import com.challenge.utils.Scrambler;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ScrambledMobDropsChallenge extends AbstractMobDropsChallenge {
  private final String name = "Scrambled Mob Drops ";
  private Scrambler<ItemStack> itemScrambler;

  @Override
  public void enable() {
    super.enable();
    this.itemScrambler = new Scrambler<>(CategorizedItemSelector.create(getServer()));
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
