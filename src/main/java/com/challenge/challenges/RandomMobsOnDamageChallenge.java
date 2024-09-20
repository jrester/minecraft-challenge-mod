package com.challenge.challenges;

import com.challenge.utils.Helpers;
import com.challenge.utils.Randomizer;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;

public class RandomMobsOnDamageChallenge extends AbstractMobsOnDamageChallenge {
    private final String name = "Random Mobs on Damage";

    private final Randomizer<EntityType> mobRandomizer = new Randomizer<>(Helpers.collectAllSpawnableMobs());

    @Override
    protected EntityType getMob(LivingEntity victim, PlayerEntity player) {
        return this.mobRandomizer.getRandom();
    } 

    @Override
    public String getName() {
        return name;
    } 
}
