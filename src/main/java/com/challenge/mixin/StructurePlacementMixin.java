package com.challenge.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.challenge.events.StructureEvents;

import net.minecraft.world.gen.chunk.placement.StructurePlacement;
import net.minecraft.world.gen.chunk.placement.StructurePlacementCalculator;

@Mixin(StructurePlacement.class)
public class StructurePlacementMixin {
    @Inject(at = @At(value = "HEAD"), method = "shouldGenerate", cancellable = true)
    public void onShouldGenerate(StructurePlacementCalculator calculator, int chunkX, int chunkY, CallbackInfoReturnable<Boolean> info) {
        Optional<Boolean> res = StructureEvents.ON_SHOULD_GENERATED.invoker().onShouldGenerate(calculator, chunkX, chunkY);
        if(res.isPresent()) info.setReturnValue(res.get());
    }
}
