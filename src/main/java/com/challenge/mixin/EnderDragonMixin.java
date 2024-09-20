package com.challenge.mixin;

import net.minecraft.entity.boss.dragon.EnderDragonEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.challenge.events.GameEvents;

@Mixin(EnderDragonEntity.class)
public class EnderDragonMixin {
  @Inject(at = @At(value = "HEAD"), method = "updatePostDeath")
  public void onEnderDragonDeath(CallbackInfo info) {
    GameEvents.ON_ENDER_DRAGON_KILLED.invoker().onDragonKilled();
  }

  @Inject(at = @At(value = "HEAD"), method = "kill")
  public void onEnderDragonKilled(CallbackInfo info) {
    GameEvents.ON_ENDER_DRAGON_KILLED.invoker().onDragonKilled();
  }
  
}
