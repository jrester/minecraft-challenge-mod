package com.challenge.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.challenge.events.BlockEvents;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(Block.class)
public class BlockMixin {
    @Inject(at = @At(value = "HEAD"), method = "afterBreak", cancellable = true)
    public void onAfterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool, CallbackInfo info) throws Exception {
        boolean replaced = BlockEvents.AFTER_BLOCK_BROKEN_EVENT.invoker().afterBlockBroken(world, player, pos, state, blockEntity, tool);
        if (replaced) info.cancel();
    }
    
}
