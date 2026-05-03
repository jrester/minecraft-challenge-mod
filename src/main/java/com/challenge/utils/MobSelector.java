package com.challenge.utils;

import java.util.List;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.EntityType;

public class MobSelector implements Selector<EntityType> {
    private final List<EntityType> mobs; 

    public MobSelector(List<EntityType> mobs) {
        this.mobs = mobs;
    }    

    public static MobSelector create(MinecraftServer server) {
        return new MobSelector(Helpers.collectAllSpawnableMobs(server));
    }

    public int getMaxSelectionModifier() {
        return mobs.size() - 1;
    }

    public EntityType selectWithModifier(final int modifier) {
        int scopedIndex = modifier % getMaxSelectionModifier();
        return this.mobs.get(scopedIndex);
    }
}
