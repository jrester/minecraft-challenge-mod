package com.challenge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.challenge.events.WorldEvents;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {
    @Inject(at = @At(value = "HEAD"), method = "spawnEntity")
    public void onSpawnEntity(Entity entity, CallbackInfoReturnable info) {
        ServerWorld world = (ServerWorld)(Object)this;
        WorldEvents.ON_SPAWN_ENTITY.invoker().onSpawnEntity(world, entity);
    }
    
}
