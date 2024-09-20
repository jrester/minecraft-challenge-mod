package com.challenge;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

public class ChallengeConfigManager extends ShulkerBoxScreenHandler {

  private ChallengeCollection inventory;

  public ChallengeConfigManager(int syncId, PlayerInventory playerInventory, ChallengeCollection inventory) {
    super(syncId, playerInventory, inventory);
    this.inventory = inventory;
  }

  public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
    Slot slot = this.slots.get(slotIndex);
    if(slot.hasStack()) {
      this.inventory.toggleChallenge(slotIndex);
    };
    return;
  }

  public boolean canInsertIntoSlot(Slot slot) {
    return false;
  }

  public ItemStack quickMove(PlayerEntity player, int slot) {
    return null;
    
  }

  public boolean canUse(PlayerEntity player) {
    return true;
  }
}
