package com.challenge.challenges;

import com.challenge.utils.CategorizedItemSelector;
import com.challenge.utils.Scrambler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class ScrambledBlockDropsChallenge extends AbstractBlockDropsChallenge {
  private final String name = "Scrambled Block Drops";

  private Scrambler<ItemStack> itemScrambler;

  @Override
  public void enable() {
    super.enable();
    this.itemScrambler = new Scrambler<>(CategorizedItemSelector.create(getServer()));
  }

  @Override
  protected ItemStack getItem(Level level, Player player, BlockState state) {
    int itemModifier = Math.abs(state.getBlock().hashCode());
    return this.itemScrambler.getScrambledForPlayer(itemModifier, player, this.getServer());
  }

  @Override
  public String getName() {
    return name;
  }
}
