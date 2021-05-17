/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world;

import java.awt.Color;
import net.angle.omnicraft.graphics.ColorSource;
import net.angle.omnicraft.random.OmniRandom;
import net.angle.omnicraft.world.blocks.CubeShape;
import net.angle.omnicraft.world.blocks.HomogenousBlock;
import net.angle.omnicraft.world.types.Fluid;
import net.angle.omnicraft.world.types.Mineraloid;
import net.angle.omnicraft.world.types.MixtureComponent;
import net.angle.omnicraft.world.types.Mixture;

/**
 *
 * @author angle
 * @license https://gitlab.com/AngularAngel/omnicraft/-/blob/master/LICENSE
 */
public class WorldGenerator {
    
    public static void generateSubstances(World world) {
        world.addSubstance(new Mineraloid("Pebbles", new ColorSource.GreyVariedColorSource(new Color(64, 64, 64, 30), 60)));
        world.addSubstance(new Mineraloid("Sand", new ColorSource.GreyVariedColorSource(new Color(200, 150, 80, 60), 30)));
        world.addSubstance(new Mineraloid("Silt", new ColorSource.GreyVariedColorSource(new Color(120, 75, 0, 40), 10)));
        world.addSubstance(new Mineraloid("Clay", new ColorSource.GreyVariedColorSource(new Color(120, 120, 130, 20), -25)));
        world.addSubstance(new Mineraloid("Compost", new ColorSource.GreyVariedColorSource(new Color(80, 40, 15, 80), -20)));
        world.addSubstance(new Fluid("Water", new ColorSource.GreyVariedColorSource(new Color(0, 0, 150, 30), -20)));
        world.addSubstance(new Fluid("Air", new ColorSource.GreyVariedColorSource(new Color(120, 100, 150, 15), -50)));
        world.addSubstance(new Mixture("Gravel", new MixtureComponent(world.substances.get("Pebbles"), 100.0f)));
        world.addSubstance(new Mixture("Desert Sand", new MixtureComponent(world.substances.get("Sand"), 100.0f)));
        
        Mixture dirt = new Mixture("Dirt", new MixtureComponent(world.substances.get("Water"), 25.0f), 
                new MixtureComponent(world.substances.get("Air"), 25.0f), new MixtureComponent(world.substances.get("Silt"), 18.0f),
                new MixtureComponent(world.substances.get("Sand"), 15.0f), new MixtureComponent(world.substances.get("Clay"), 9.0f), 
                new MixtureComponent(world.substances.get("Compost"), 7.0f), new MixtureComponent(world.substances.get("Pebbles"), 1.0f));
        
        world.addSubstance(dirt);
    }
    
    public static void generateBlocks(World world) {
        world.addBlockType(new HomogenousBlock("Dirt Block", world.get_next_id(), world.substances.get("Dirt"), new CubeShape(), new OmniRandom()));
        world.addBlockType(new HomogenousBlock("Gravel Block", world.get_next_id(), world.substances.get("Gravel"), new CubeShape(), new OmniRandom()));
        world.addBlockType(new HomogenousBlock("Desert Sand Block", world.get_next_id(), world.substances.get("Desert Sand"), new CubeShape(), new OmniRandom()));
    }
    
    public static void generateChunks(World world) {
        ChunkContainer spawnRegion = world.getSpawnRegion();
        int farEdge = world.chunkEdgeLengthOfRegion - 1;
        for (int i = 0; i < world.chunkEdgeLengthOfRegion; i++)
            for (int j = 0; j < world.chunkEdgeLengthOfRegion; j++)
                for (int k = 0; k < world.chunkEdgeLengthOfRegion; k++) {
                    if (i == 0 || j == 0 || k == 0 || i == farEdge || j == farEdge || k == farEdge)
                        spawnRegion.getChunk(i, j, k).setAllBlocks(world.blockTypes.get("Dirt Block"));
                }
        
        Chunk chunk = spawnRegion.getChunk(0, 0, 0);
        chunk.setAllBlocks(world.blockTypes.get("Desert Sand Block"));
        chunk.setBlock(0, 0, 0, world.blockTypes.get("Gravel Block"));
        chunk.setBlock(15, 1, 0, world.blockTypes.get("Gravel Block"));
        chunk.setBlock(0, 15, 0, world.blockTypes.get("Gravel Block"));
//        chunk.setBlock(0, 0, 1, world.blockTypes.get("Dirt Block"));
        
        spawnRegion.getChunk(1, 1, 1).setAllBlocks(world.blockTypes.get("Desert Sand Block"));
        spawnRegion.getChunk(2, 2, 2).setAllBlocks(world.blockTypes.get("Gravel Block"));
        spawnRegion.getChunk(15, 1, 1).setAllBlocks(world.blockTypes.get("Desert Sand Block"));
            
        world.loadRegion(spawnRegion);
    }
    
    public static World generateWorld() {
        World world = new World();
        
        generateSubstances(world);
        
        generateBlocks(world);

        generateChunks(world);
        
        return world;
    }
}
