package com.challenge.challenges;

import java.util.Random;

import com.challenge.events.WorldEvents;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;

public class RandomMobSpeedChallenge extends BaseChallenge {
    public final String name = "Random Mob Speed";
    public final Random random = new Random();

    @Override
    public void registerEventHandlers() {
       WorldEvents.ON_SPAWN_ENTITY.register((world, entity) -> {
        if(!this.isActive()) return;
        // Do not change the speed of players
        if(entity.isPlayer()) return;
        // Only apply to mobs not e.g. dropped items
        if(!entity.isLiving()) return;
        int pivot = random.nextInt(100);
        double newSpeed = 0.0D;
        if (pivot < 20) {
            newSpeed = random.nextDouble(0.1D, 1.0D);
        } else if (pivot >= 20 && pivot < 60) {
            newSpeed = random.nextDouble(1.0D, 1.5D);
        } else if (pivot >= 60 && pivot < 90) {
            newSpeed = random.nextDouble(1.5D, 2.5D);
        } else if (pivot >= 90) {
            newSpeed = random.nextDouble(2.5D, 5.0D);
        }
        LivingEntity livingEntity = (LivingEntity)entity;
        livingEntity.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(newSpeed);
       }); 
    }

    @Override
    public ItemStack getIndicatorItemStack() {
        if(isEnabled()) {
            ItemStack itemStack = Items.NETHERITE_BOOTS.getDefaultStack();
            RegistryEntry<Enchantment> enchantment = getEnchantment(Enchantments.SOUL_SPEED);
            itemStack.addEnchantment(enchantment, 1);
            return itemStack;
        } else {
            return Items.LEATHER_BOOTS.getDefaultStack();
        }
    }

    @Override
    public String getName() {
        return name;
    } 
}
