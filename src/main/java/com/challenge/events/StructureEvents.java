package com.challenge.events;

import java.util.Optional;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.gen.chunk.placement.StructurePlacementCalculator;

public class StructureEvents {
    public static final Event<ShouldGenerateCallback> ON_SHOULD_GENERATED = EventFactory.createArrayBacked(ShouldGenerateCallback.class, callbacks -> (calculator, chunkX, chunkY) -> {
        for(ShouldGenerateCallback callback : callbacks) {
            Optional<Boolean> res = callback.onShouldGenerate(calculator, chunkX, chunkY);
            if (res.isPresent()) return res;
        }
        return Optional.empty();
    });
    
    @FunctionalInterface
    public interface ShouldGenerateCallback {
        Optional<Boolean> onShouldGenerate(StructurePlacementCalculator calculator, int chunkX, int chunkY);
    }
}
