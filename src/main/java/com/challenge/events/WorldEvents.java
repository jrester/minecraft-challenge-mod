package com.challenge.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class WorldEvents {
    public static final Event<SpawnEntityCallback> ON_SPAWN_ENTITY = EventFactory.createArrayBacked(SpawnEntityCallback.class, callbacks -> (world, entity) -> {
        for(SpawnEntityCallback callback : callbacks) {
            callback.onSpawnEntity(world, entity);
        }
    });

    @FunctionalInterface
    public interface SpawnEntityCallback {
        public void onSpawnEntity(Level level, Entity entity);     
    }
}
