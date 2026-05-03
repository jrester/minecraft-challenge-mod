package com.challenge.mixin;

import com.challenge.events.BlockEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CraftingTableBlock.class)
public class CraftingTableBlockMixin {
  @Inject(at = @At(value = "HEAD"), method = "useWithoutItem", cancellable = true)
  public void onUse(
      BlockState state,
      Level level,
      BlockPos pos,
      Player player,
      BlockHitResult hit,
      CallbackInfoReturnable<InteractionResult> info) {
    InteractionResult result =
        BlockEvents.ON_CRAFTING_BLOCK_USE
            .invoker()
            .onCraftingBlockUse(state, level, pos, player, hit);
    if (result == InteractionResult.FAIL) {
      info.setReturnValue(result);
    }
  }
}
