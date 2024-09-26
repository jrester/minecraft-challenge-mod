package com.challenge.events;


import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public class PlayerEvents {
    public static final Event<PlayerTravelCallback> AFTER_PLAYER_TRAVEL = EventFactory.createArrayBacked(PlayerTravelCallback.class, callbacks -> (player, inputMovement) -> {
        for (PlayerTravelCallback callback : callbacks) {
            callback.afterPlayerTravel(player, inputMovement);
        }
    });

    @FunctionalInterface
    public interface PlayerTravelCallback {
        public void afterPlayerTravel(PlayerEntity player, Vec3d inputMovement);
    }
}
