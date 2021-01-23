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
        world.addSubstance(new Mineraloid("Pebbles", new ColorSource.FlatColorSource(Color.gray)));
        world.addSubstance(new Mineraloid("Sand", new ColorSource.FlatColorSource(Color.yellow)));
        world.addSubstance(new Mineraloid("Silt", new ColorSource.FlatColorSource(new Color(128, 0, 0))));
        world.addSubstance(new Mineraloid("Clay", new ColorSource.FlatColorSource(new Color(128, 128, 200))));
        world.addSubstance(new Mineraloid("Compost", new ColorSource.FlatColorSource(new Color(128, 128, 0))));
        world.addSubstance(new Fluid("Water", new ColorSource.FlatColorSource(new Color(0, 0, 255, 20))));
        world.addSubstance(new Fluid("Air", new ColorSource.FlatColorSource(new Color(255, 0, 0, 20))));
        world.addSubstance(new Mixture("Gravel", new MixtureComponent(world.substances.get("Pebbles"), 100.0f)));
        world.addSubstance(new Mixture("Desert Sand", new MixtureComponent(world.substances.get("Sand"), 100.0f)));
        
        Mixture dirt = new Mixture("Dirt", new MixtureComponent(world.substances.get("Water"), 25.0f), 
                new MixtureComponent(world.substances.get("Air"), 25.0f), new MixtureComponent(world.substances.get("Silt"), 18.0f),
                new MixtureComponent(world.substances.get("Sand"), 15.0f), new MixtureComponent(world.substances.get("Clay"), 9.0f), 
                new MixtureComponent(world.substances.get("Compost"), 7.0f), new MixtureComponent(world.substances.get("Pebbles"), 1.0f));
        
        world.addSubstance(dirt);
    }
    
    public static void generateBlocks(World world) {
        world.addBlockType(new HomogenousBlock("Dirt Block", world.substances.get("Dirt"), new CubeShape(), new OmniRandom()));
        world.addBlockType(new HomogenousBlock("Gravel Block", world.substances.get("Gravel"), new CubeShape(), new OmniRandom()));
        world.addBlockType(new HomogenousBlock("Desert Sand Block", world.substances.get("Desert Sand"), new CubeShape(), new OmniRandom()));
    }
    
    public static void generateChunks(World world) {
        Region spawnRegion = world.getSpawnRegion();
        Chunk chunk = spawnRegion.getChunk(0, 0, 0);
        chunk.setAllBlocks(world.blockTypes.get("Desert Sand Block"));
        chunk.setBlock(0, 0, 0, world.blockTypes.get("Gravel Block"));
        chunk.setBlock(0, 0, 1, world.blockTypes.get("Dirt Block"));
        spawnRegion.getChunk(1, 0, 0).setAllBlocks(world.blockTypes.get("Dirt Block"));
        spawnRegion.getChunk(1, 1, 1).setAllBlocks(world.blockTypes.get("Desert Sand Block"));
        spawnRegion.getChunk(2, 2, 2).setAllBlocks(world.blockTypes.get("Gravel Block"));
        spawnRegion.getChunk(3, 3, 3).setAllBlocks(world.blockTypes.get("Dirt Block"));
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
