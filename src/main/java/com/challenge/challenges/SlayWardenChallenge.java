package com.challenge.challenges;

import com.challenge.utils.Helpers;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;

public class SlayWardenChallenge extends BaseChallenge {

    private final String name = "Slay Warden";

    @Override
    public void registerEventHandlers() {
        ServerLivingEntityEvents.AFTER_DEATH.register(
            (entity, damageSource) -> {
                if (!this.isActive()) return;
                if (!entity.getType().equals(EntityType.WARDEN)) return;
                this.challengeFinished(true);
            }
        );
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ItemStack getIndicatorItemStack() {
        if (this.isEnabled()) {
            ItemStack itemStack = Items.SCULK_SHRIEKER.getDefaultStack();
            RegistryEntry<Enchantment> power = Helpers.getEnchantment(
                this.getServer(),
                Enchantments.POWER
            );
            itemStack.addEnchantment(power, 3);
            return itemStack;
        } else {
            return Items.SCULK_SENSOR.getDefaultStack();
        }
    }
}
