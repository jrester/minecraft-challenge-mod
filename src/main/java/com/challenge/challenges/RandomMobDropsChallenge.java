package com.challenge.challenges;

import com.challenge.utils.Helpers;
import com.challenge.utils.Randomizer;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class RandomMobDropsChallenge extends AbstractMobDropsChallenge {
    private final String name = "Random Mob Drops";
  private final Randomizer<Item> itemRandomizer = new Randomizer<>(Helpers.collectAllItems());

  @Override
  protected Item getItem(World world, PlayerEntity player, LivingEntity victim) {
      return this.itemRandomizer.getRandom();
  }


  @Override
  public String getName() {
    return name;
  }
}
