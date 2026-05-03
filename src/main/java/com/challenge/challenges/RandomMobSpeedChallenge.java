package com.challenge.challenges;

import com.challenge.events.WorldEvents;
import java.util.Random;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;

public class RandomMobSpeedChallenge extends BaseChallenge {
  public final String name = "Random Mob Speed";
  public final Random random = new Random();

  @Override
  public void registerEventHandlers() {
    WorldEvents.ON_SPAWN_ENTITY.register(
        (world, entity) -> {
          if (!this.isActive()) return;
          // Do not change the speed of players
          if (entity instanceof Player) return;
          // Only apply to mobs not e.g. dropped items
          if (!(entity instanceof LivingEntity)) return;
          int pivot = random.nextInt(100);
          double newSpeed = 0.0D;
          if (pivot < 20) {
            newSpeed = random.nextDouble(0.1D, 1.0D);
          } else if (pivot >= 20 && pivot < 60) {
            newSpeed = random.nextDouble(1.0D, 1.5D);
          } else if (pivot >= 60 && pivot < 90) {
            newSpeed = random.nextDouble(1.5D, 2.5D);
          } else if (pivot >= 90) {
            newSpeed = random.nextDouble(2.5D, 5.0D);
          }
          LivingEntity livingEntity = (LivingEntity) entity;
          livingEntity.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(newSpeed);
        });
  }

  @Override
  public ItemStack getIndicatorItemStack() {
    if (isEnabled()) {
      return asEnchantedIndicatorItemStack(Items.NETHERITE_BOOTS, Enchantments.SOUL_SPEED, 1);
    } else {
      return asIndicatorItemStack(Items.LEATHER_BOOTS);
    }
  }

  @Override
  public String getName() {
    return name;
  }
}
