package com.challenge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.challenge.events.PlayerEvents;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {
  @Inject(at = @At(value = "HEAD"), method = "insertStack(ILnet/minecraft/item/ItemStack;)Z", cancellable = true)
  public void onInsertStack(int slot, ItemStack stack, CallbackInfoReturnable info) {
    PlayerEntity player = ((PlayerInventory)(Object)this).player;
    PlayerEvents.ON_PLAYER_PICKUP.invoker().onPlayerPickup(player, stack);
  } 
}
