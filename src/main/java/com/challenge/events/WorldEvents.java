package com.challenge.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class WorldEvents {
    public static final Event<SpawnEntityCallback> ON_SPAWN_ENTITY = EventFactory.createArrayBacked(SpawnEntityCallback.class, callbacks -> (world, entity) -> {
        for(SpawnEntityCallback callback : callbacks) {
            callback.onSpawnEntity(world, entity);
        }
    });

    @FunctionalInterface
    public interface SpawnEntityCallback {
        public void onSpawnEntity(World world, Entity entity);     
    }
}
