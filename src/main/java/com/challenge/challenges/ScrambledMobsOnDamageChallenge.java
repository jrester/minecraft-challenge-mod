package com.challenge.challenges;

import com.challenge.utils.Helpers;
import com.challenge.utils.MobScrambler;


import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;

public class ScrambledMobsOnDamageChallenge extends AbstractMobsOnDamageChallenge {
    private final String name = "Scrambled Mobs on Damage";
    private final MobScrambler mobScrambler = new MobScrambler();

    @Override
    protected EntityType getMob(LivingEntity victim, PlayerEntity player) {
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
            ItemStack itemStack = Items.SPAWNER.getDefaultStack();
            RegistryEntry<Enchantment> fortune = Helpers.getEnchantment(this.getServer(), Enchantments.FORTUNE);
            itemStack.addEnchantment(fortune, 3);
            return itemStack;
        } else {
            return Items.SPAWNER.getDefaultStack();
        }
    }
    
}
