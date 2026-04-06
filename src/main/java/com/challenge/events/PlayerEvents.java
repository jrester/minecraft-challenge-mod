package com.challenge.events;


import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.player.Player;

public class PlayerEvents {
    public static final Event<PlayerTravelCallback> AFTER_PLAYER_TRAVEL = EventFactory.createArrayBacked(PlayerTravelCallback.class, callbacks -> (player, inputMovement) -> {
        for (PlayerTravelCallback callback : callbacks) {
            callback.afterPlayerTravel(player, inputMovement);
        }
    });

    @FunctionalInterface
    public interface PlayerTravelCallback {
        public void afterPlayerTravel(Player player, Vec3 inputMovement);
    }
}
