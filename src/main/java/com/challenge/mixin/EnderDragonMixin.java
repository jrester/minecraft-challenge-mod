package com.challenge.mixin;

import com.challenge.events.GameEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnderDragon.class)
public class EnderDragonMixin {
  @Inject(at = @At(value = "HEAD"), method = "kill")
  public void onEnderDragonKilled(ServerLevel level, CallbackInfo info) {
    GameEvents.ON_ENDER_DRAGON_KILLED.invoker().onDragonKilled();
  }
}
