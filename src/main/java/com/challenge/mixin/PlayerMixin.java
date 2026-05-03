package com.challenge.mixin;

import com.challenge.events.PlayerEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerMixin {
  @Inject(at = @At(value = "TAIL"), method = "travel", cancellable = true)
  public void afterTravel(Vec3 movementInput, CallbackInfo info) {
    Player player = (Player) (Object) this;
    PlayerEvents.AFTER_PLAYER_TRAVEL.invoker().afterPlayerTravel(player, movementInput);
  }
}
