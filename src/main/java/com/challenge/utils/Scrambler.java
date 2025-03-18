package com.challenge.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;

public abstract class Scrambler<T> {
    protected Random random = new Random();
    private Map<String, Integer> playerModifiers = new HashMap<>();

    protected int getModifierForPlayer(PlayerEntity player) {
        String uuid = player.getUuidAsString();
        int modifier;
        if(!playerModifiers.containsKey(uuid)) {
            modifier = Math.abs(this.random.nextInt());
            this.playerModifiers.put(uuid, modifier);
        } else {
            modifier = this.playerModifiers.get(uuid).intValue();
        }
        return modifier;
    }
    
    abstract public T getScrambledForPlayer(int itemModifier, PlayerEntity player, MinecraftServer server);
}
