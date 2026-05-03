package com.challenge;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChallengeSlot extends Slot {
  public static final Logger LOGGER = LoggerFactory.getLogger(ChallengeMod.MOD_ID);

  public ChallengeSlot(final Container container, final int slot, final int x, final int y) {
    super(container, slot, x, y);
  }

  @Override
  public boolean mayPlace(final ItemStack itemStack) {
    return false;
  }

  @Override
  public void setByPlayer(final ItemStack itemStack) {}

  @Override
  public void setByPlayer(final ItemStack itemStack, final ItemStack previous) {}

  @Override
  public void set(final ItemStack itemStack) {}

  @Override
  public ItemStack safeInsert(final ItemStack stack) {
    return stack;
  }

  @Override
  public ItemStack safeInsert(final ItemStack inputStack, final int inputAmount) {
    return inputStack;
  }

  @Override
  public boolean allowModification(final Player player) {
    return false;
  }
}
