package com.challenge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.challenge.challenges.BaseChallenge;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ChallengeConfigManager extends AbstractContainerMenu {
  private static final int COLUMNS = 9;
  private static final int ROWS = 6;
	public static final Logger LOGGER = LoggerFactory.getLogger(ChallengeMod.MOD_ID);


  private Inventory inventory;
  private ChallengeCollection challengeCollection;

  public ChallengeConfigManager(int containerId, Inventory Inventory, Player player, ChallengeCollection challengeCollection) {
    super(MenuType.GENERIC_9x6, containerId);
    this.challengeCollection = challengeCollection;

    this.addChallengeGrid();
  }

	private void addChallengeGrid() {
		for (int i = 0; i < COLUMNS * ROWS; i++) {
      int x = i % COLUMNS;
      int y = (int)(i / COLUMNS);
      this.addSlot(new ChallengeSlot(challengeCollection, i, x, y));
		}
	}

	@Override
	public boolean stillValid(final Player player) {
		return true;
	}


  @Override
	public ItemStack quickMoveStack(final Player player, final int slotIndex) {
    return ItemStack.EMPTY;
  }

  public boolean canUse(Player player) {
    return true;
  }
}
