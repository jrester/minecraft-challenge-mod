package com.challenge.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

public class LivingEntityEvents {
  public static final Event<LivingEntityLootDropCallback> ON_LIVING_ENTITY_LOOT_DROP = EventFactory.createArrayBacked(LivingEntityLootDropCallback.class, callbacks -> (victim, damageSource, causedByPlayer) -> {
      for(LivingEntityLootDropCallback callback : callbacks) {
        boolean replaced = callback.onLivingEntityLootDrop(victim, damageSource, causedByPlayer);
        if (replaced) return replaced;
      }
      return false;
    });

  @FunctionalInterface
  public interface LivingEntityLootDropCallback {
    public boolean onLivingEntityLootDrop(LivingEntity victim, DamageSource damageSource, boolean causedByPlayer);
  }

}
