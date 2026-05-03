package com.challenge.challenges;


import com.challenge.utils.MobSelector;
import com.challenge.utils.Scrambler;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;



public class ScrambledMobsOnDamageChallenge extends AbstractMobsOnDamageChallenge {
    private final String name = "Scrambled Mobs on Damage";
    private Scrambler<EntityType> mobScrambler;

    @Override
    public void enable() {
        super.enable();
        this.mobScrambler = new Scrambler<>(MobSelector.create(getServer()));
    }

    @Override
    protected EntityType getMob(LivingEntity victim, Player player) {
        int victimTypeHash = Math.abs(victim.getType().hashCode());
        return this.mobScrambler.getScrambledForPlayer(victimTypeHash, player, this.getServer());
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public ItemStack getIndicatorItemStack() {
        if(isEnabled()) {
            return asEnchantedIndicatorItemStack(Items.SPAWNER, Enchantments.FORTUNE, 3);
        } else {
            return asIndicatorItemStack(Items.SPAWNER);
        }
    }
    
}
