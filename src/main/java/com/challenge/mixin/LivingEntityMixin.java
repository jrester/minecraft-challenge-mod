package com.challenge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.challenge.events.LivingEntityEvents;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;


@Mixin(LivingEntity.class)
public class LivingEntityMixin {
  @Inject(at = @At(value = "HEAD"), method = "dropFromLootTable", cancellable = true)
  public void onDropLoot(ServerLevel world, DamageSource damageSource, boolean causedByPlayer, CallbackInfo info) {
    LivingEntity victim = (LivingEntity)(Object)this;
    boolean replaced = LivingEntityEvents.ON_LIVING_ENTITY_LOOT_DROP.invoker().onLivingEntityLootDrop(victim, damageSource, causedByPlayer);
    if(replaced) info.cancel();
  }
  
}
