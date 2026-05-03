package com.challenge.challenges;

import com.challenge.utils.MobSelector;
import com.challenge.utils.Randomizer;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;


public class RandomMobsOnDamageChallenge extends AbstractMobsOnDamageChallenge {
    private final String name = "Random Mobs on Damage";

    private Randomizer<EntityType> mobRandomizer;

    @Override
    public void enable() {
        super.enable();
        this.mobRandomizer = new Randomizer<>(MobSelector.create(getServer()));
    }

    @Override
    protected EntityType getMob(LivingEntity victim, Player player) {
        return this.mobRandomizer.getRandom();
    } 

    @Override
    public String getName() {
        return name;
    } 
}
