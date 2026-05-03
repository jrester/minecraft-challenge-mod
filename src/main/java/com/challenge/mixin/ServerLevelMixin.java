package com.challenge.mixin;

import com.challenge.events.WorldEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {
  @Inject(at = @At(value = "HEAD"), method = "addEntity")
  public void onSpawnEntity(Entity entity, CallbackInfoReturnable info) {
    ServerLevel world = (ServerLevel) (Object) this;
    WorldEvents.ON_SPAWN_ENTITY.invoker().onSpawnEntity(world, entity);
  }
}
