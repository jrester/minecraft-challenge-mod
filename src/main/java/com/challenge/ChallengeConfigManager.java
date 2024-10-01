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
    // When the player is dragging items the slotIndex is -999...
    if(slotIndex >= 0) {
      Slot slot = this.slots.get(slotIndex);

      // Make sure that the player is interacting with the challenge inventory
      if(slot.inventory.equals(this.inventory)) {
        // Not all slots in the challenge collection inventory might be filled with challenges
        if(this.inventory.getStack(slotIndex) != ItemStack.EMPTY) {
          this.inventory.toggleChallenge(slotIndex);
        }
        return;
      }
    }
    // If the player did not perform any challenge configuration, they are clicking around in their inventory
    // Those actions are still allowed, therefore they are passed to the default implementation
    super.onSlotClick(slotIndex, button, actionType, player); 
  }

  public boolean canInsertIntoSlot(Slot slot) {
    return slot.inventory.equals(this.inventory) ? false : super.canInsertIntoSlot(slot);
  }

  public ItemStack quickMove(PlayerEntity player, int slot) {
    return null;
    
  }

  public boolean canUse(PlayerEntity player) {
    return true;
  }
}
