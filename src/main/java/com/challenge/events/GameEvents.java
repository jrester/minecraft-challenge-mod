package com.challenge.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public final class GameEvents {
  public static final Event<EnderDragonKilledCallback> ON_ENDER_DRAGON_KILLED =
      EventFactory.createArrayBacked(
          EnderDragonKilledCallback.class,
          callbacks ->
              () -> {
                for (EnderDragonKilledCallback callback : callbacks) {
                  callback.onDragonKilled();
                }
              });

  public interface EnderDragonKilledCallback {
    public void onDragonKilled();
  }
}
