/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world;

import java.awt.Color;
import net.angle.omnicraft.random.OmniRandom;
import net.angle.omnicraft.textures.pixels.VariedColorPixelSource;
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
        world.addSubstance(new Mineraloid("Pebbles", new VariedColorPixelSource(Color.darkGray, 60), 240));
        world.addSubstance(new Mineraloid("Sand", new VariedColorPixelSource(new Color(200, 150, 80), 30), 80));
        world.addSubstance(new Mineraloid("Silt", new VariedColorPixelSource(new Color(120, 75, 0), 10), 40));
        world.addSubstance(new Mineraloid("Clay", new VariedColorPixelSource(new Color(120, 120, 130), -25), 20));
        world.addSubstance(new Mineraloid("Compost", new VariedColorPixelSource(new Color(80, 40, 15), -20), 130));
        world.addSubstance(new Fluid("Water", new VariedColorPixelSource(new Color(0, 0, 200, 20), -20)));
        world.addSubstance(new Fluid("Air", new VariedColorPixelSource(new Color(20, 0, 50, 25), -50)));
        world.addSubstance(new Mixture("Gravel", new MixtureComponent(world.substances.get("Pebbles"), 100.0f)));
        world.addSubstance(new Mixture("Desert Sand", new MixtureComponent(world.substances.get("Sand"), 100.0f)));
        
        Mixture dirt = new Mixture("Dirt", new MixtureComponent(world.substances.get("Water"), 25.0f), 
                new MixtureComponent(world.substances.get("Air"), 25.0f), new MixtureComponent(world.substances.get("Silt"), 18.0f),
                new MixtureComponent(world.substances.get("Sand"), 15.0f), new MixtureComponent(world.substances.get("Clay"), 9.0f), 
                new MixtureComponent(world.substances.get("Compost"), 5.0f), new MixtureComponent(world.substances.get("Pebbles"), 3.0f));
        
        world.addSubstance(dirt);
    }
    
    public static void generateBlocks(World world) {
        world.blockTypes.add(new HomogenousBlock("Dirt Block", world.substances.get("Dirt"), new CubeShape(), new OmniRandom()));
        world.blockTypes.add(new HomogenousBlock("Gravel Block", world.substances.get("Gravel"), new CubeShape(), new OmniRandom()));
        world.blockTypes.add(new HomogenousBlock("Desert Sand Block", world.substances.get("Desert Sand"), new CubeShape(), new OmniRandom()));
    }
    
    public static void generateChunks(World world) {
        world.loadedChunks.add(new OctreeChunk(world.spawnRegion, world.blockTypes.get(0)));
        world.loadedChunks.get(0).setBlock(0, 0, 0, world.blockTypes.get(1));
        world.loadedChunks.get(0).setBlock(0, 0, 1, world.blockTypes.get(2));
        world.loadedChunks.add(new OctreeChunk(world.spawnRegion, world.blockTypes.get(0), 16, 0, 0));
    }
    
    public static World generateWorld() {
        World world = new World();
        
        generateSubstances(world);
        
        generateBlocks(world);

        generateChunks(world);
        
        return world;
    }
}
