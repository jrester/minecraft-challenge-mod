package com.challenge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.challenge.events.BlockEvents;

import net.minecraft.block.BlockState;
import net.minecraft.block.CraftingTableBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(CraftingTableBlock.class)
public class CraftingTableBlockMixin {
    @Inject(at = @At(value = "HEAD"), method = "onUse", cancellable = true)
    public void onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit, CallbackInfoReturnable<ActionResult> info) {
        ActionResult result = BlockEvents.ON_CRAFTING_BLOCK_USE.invoker().onCraftingBlockUse(state, world, pos, player, hit);
        if(result == ActionResult.FAIL) {
            info.setReturnValue(result);
        }
    } 
}
