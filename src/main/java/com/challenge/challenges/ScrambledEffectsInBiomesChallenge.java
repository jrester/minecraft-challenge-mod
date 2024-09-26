package com.challenge.challenges;

import com.challenge.events.PlayerEvents;
import com.challenge.utils.Helpers;
import com.challenge.utils.Scrambler;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeAccess;

public class ScrambledEffectsInBiomesChallenge extends BaseChallenge {
    private final String name = "Scrambled Effects in Bioms";

    private final Scrambler<StatusEffect> statusEffectScrambler = new Scrambler<>(Helpers.collectAllStatusEffects());


    @Override
    public void registerEventHandlers() {
        PlayerEvents.AFTER_PLAYER_TRAVEL.register((player, movementInput) -> {
            if(!this.isActive()) return;
            RegistryEntry<Biome> biomeEntry = player.getWorld().getBiome(player.getBlockPos());
            int biomeModifier = Math.abs(biomeEntry.getKey().hashCode());
            StatusEffect statusEffect = this.statusEffectScrambler.getScrambledForPlayer(biomeModifier, player);
            
            
        });
    }

    @Override
    public String getName() {
        return name;
    }
    
}
