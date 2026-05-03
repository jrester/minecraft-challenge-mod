package com.challenge.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.challenge.events.BlockEvents;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;


@Mixin(Block.class)
public class BlockMixin {
    @Inject(at = @At(value = "HEAD"), method = "playerDestroy", cancellable = true)
    public void onPlayerDestroy(final Level level, final Player player, final BlockPos pos, final BlockState state, @Nullable final BlockEntity blockEntity, final ItemStack destroyedWith, CallbackInfo info) throws Exception {
        boolean replaced = BlockEvents.AFTER_PLAYER_DESTROY_EVENT.invoker().afterPlayerDestroy(level, player, pos, state, blockEntity, destroyedWith, false);
        if (replaced) info.cancel();
    }
    
}
