package com.challenge.challenges;

import java.util.Optional;
import java.util.Random;

import com.challenge.events.StructureEvents;
import com.challenge.utils.Helpers;
import com.challenge.utils.Randomizer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.chunk.ChunkGenerating;
import net.minecraft.world.gen.chunk.placement.ConcentricRingsStructurePlacement;
import net.minecraft.world.gen.chunk.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.gen.chunk.placement.StructurePlacement;
import net.minecraft.world.gen.structure.MineshaftStructure;

public class StructureOverloadChallenge extends BaseChallenge {
    private final String name = "Structure Overload";
    //public Randomizer<Identifier> structureRandomizer = new Randomizer<>(Helpers.collectAllStructureIdentifiers());
    public Random random = new Random();

    @Override
    public void registerEventHandlers() {
        StructureEvents.ON_SHOULD_GENERATED.register((calculator, chunkX, chunkY) -> {
            //MineshaftStructure
            if(!isActive()) return Optional.empty();
            if(random.nextInt(0, 100) != 1) return Optional.empty(); 
            LOGGER.info("Generate structure at " + chunkX + " " + chunkY);
            return Optional.of(true);
        });
        // ServerChunkEvents.CHUNK_LOAD.register((world, chunk) -> {
        //     if(!isActive()) return;
        //     if(random.nextInt(0, 50) == 1) return; 
        //     int y = chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE).get(0, 0);
        //     String locationString = pos.getCenterX() + " " + y + " " + pos.getCenterZ();
        //     Identifier structureIdentifier = structureRandomizer.getRandom();
        //     String structure = structureIdentifier.toString();
        //     String placeCommand ="place structure " + structure + " " + locationString;
        //     LOGGER.info(placeCommand);
        //     ServerPlayerEntity player = world.getPlayers().getFirst();
        //     world.getServer().getCommandManager().executeWithPrefix(player.getCommandSource(), placeCommand);
        // });
    }

    @Override
    public String getName() {
        return name;
    }
    
}
