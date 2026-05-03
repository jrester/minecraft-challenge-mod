package com.challenge.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class BlockEvents {
  public static final Event<AfterPlayerDestroyCallback> AFTER_PLAYER_DESTROY_EVENT =
      EventFactory.createArrayBacked(
          AfterPlayerDestroyCallback.class,
          callbacks ->
              (world, player, pos, state, blockEntity, tool, isBlockReplaced) -> {
                boolean replaced = false;
                for (AfterPlayerDestroyCallback callback : callbacks) {
                  replaced |=
                      callback.afterPlayerDestroy(
                          world, player, pos, state, blockEntity, tool, replaced);
                }
                return replaced;
              });

  @FunctionalInterface
  public interface AfterPlayerDestroyCallback {
    public boolean afterPlayerDestroy(
        Level level,
        Player layer,
        BlockPos pos,
        BlockState state,
        @Nullable BlockEntity blockEntity,
        ItemStack tool,
        boolean isBlockReplaced);
  }

  public static final Event<OnCraftingBlockCallback> ON_CRAFTING_BLOCK_USE =
      EventFactory.createArrayBacked(
          OnCraftingBlockCallback.class,
          callbacks ->
              (state, world, pos, player, hit) -> {
                for (OnCraftingBlockCallback callback : callbacks) {
                  InteractionResult result =
                      callback.onCraftingBlockUse(state, world, pos, player, hit);
                  if (result != InteractionResult.PASS) {
                    return result;
                  }
                }
                return InteractionResult.PASS;
              });

  @FunctionalInterface
  public interface OnCraftingBlockCallback {
    public InteractionResult onCraftingBlockUse(
        BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit);
  }
}
