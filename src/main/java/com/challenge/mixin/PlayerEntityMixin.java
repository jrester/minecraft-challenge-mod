package com.challenge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.challenge.events.PlayerEvents;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(at = @At(value = "TAIL"), method="travel", cancellable = true)
    public void afterTravel(Vec3d movementInput, CallbackInfo info) {
        PlayerEntity player = (PlayerEntity)(Object)this;
        PlayerEvents.AFTER_PLAYER_TRAVEL.invoker().afterPlayerTravel(player, movementInput);
    }
    
}
