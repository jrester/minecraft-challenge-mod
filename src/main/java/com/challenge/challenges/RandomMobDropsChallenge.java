package com.challenge.challenges;

import com.challenge.utils.CategorizedItemSelector;
import com.challenge.utils.Randomizer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class RandomMobDropsChallenge extends AbstractMobDropsChallenge {
  private final String name = "Random Mob Drops";
  private Randomizer<ItemStack> itemRandomizer = null;

  @Override
  public void enable() {
    super.enable();
    this.itemRandomizer = new Randomizer<>(CategorizedItemSelector.create(getServer()));
  }

  @Override
  protected ItemStack getItem(Level level, Player player, LivingEntity victim) {
    return this.itemRandomizer.getRandom();
  }

  @Override
  public String getName() {
    return name;
  }
}
