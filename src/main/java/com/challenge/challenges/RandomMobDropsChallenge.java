package com.challenge.challenges;

import com.challenge.utils.Helpers;
import com.challenge.utils.Randomizer;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;


public class RandomMobDropsChallenge extends AbstractMobDropsChallenge {
  private final String name = "Random Mob Drops";
  private Randomizer<Item> itemRandomizer = null;

  @Override
  public void enable() {
    super.enable();
    this.itemRandomizer = new Randomizer<>(Helpers.collectAllItems(getServer()));
  }

  @Override
  protected ItemStack getItem(Level level, Player player, LivingEntity victim) {
      return new ItemStack(this.itemRandomizer.getRandom());
  }

  @Override
  public String getName() {
    return name;
  }
}
