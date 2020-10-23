/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world;

import java.awt.Color;
import net.angle.omnicraft.random.OmniRandom;
import net.angle.omnicraft.textures.pixels.ColoredVariation;
import net.angle.omnicraft.textures.pixels.VariedColorPixelSource;
import net.angle.omnicraft.world.blocks.CubeShape;
import net.angle.omnicraft.world.blocks.HomogenousBlock;
import net.angle.omnicraft.world.blocks.SteppedCubeShape;
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
        world.substances.put("Gravel", new Mineraloid("Gravel", new VariedColorPixelSource(Color.darkGray, 60)));
        world.substances.put("Sand", new Mineraloid("Sand", new VariedColorPixelSource(new Color(80, 60, 30), 60)));
        world.substances.put("Silt", new Mineraloid("Silt", new VariedColorPixelSource(new Color(70, 40, 40), 60)));
        world.substances.put("Clay", new Mineraloid("Clay", new VariedColorPixelSource(new Color(60, 30, 30), 60)));
        world.substances.put("Compost", new Mineraloid("Compost", new VariedColorPixelSource(new Color(80, 40, 30), 60)));
        world.substances.put("Water", new Fluid("Water", new ColoredVariation(-5, -3, -2)));
        world.substances.put("Air", new Fluid("Air", new ColoredVariation(2, 1, 2)));
    }
    
    public static void generateSoilTypes(World world) {
        world.soilTypes.add(new Mixture("Dirt", new MixtureComponent(world.substances.get("Sand"), 15.0f),
                new MixtureComponent(world.substances.get("Silt"), 18.0f), new MixtureComponent(world.substances.get("Clay"), 9.0f), 
                new MixtureComponent(world.substances.get("Compost"), 5.0f), new MixtureComponent(world.substances.get("Gravel"), 3.0f), 
                new MixtureComponent(world.substances.get("Water"), 25.0f), new MixtureComponent(world.substances.get("Air"), 25.0f)));
        world.soilTypes.add(new Mixture("Gravel", new MixtureComponent(world.substances.get("Gravel"), 100.0f)));
    }
    
    public static void generateBlocks(World world) {
        world.blocks.add(new HomogenousBlock(world.soilTypes.get(0), new SteppedCubeShape(12), new OmniRandom()));
        world.blocks.add(new HomogenousBlock(world.soilTypes.get(1), new CubeShape(), new OmniRandom()));
        world.blocks.add(new HomogenousBlock(world.soilTypes.get(1), new SteppedCubeShape(4), new OmniRandom()));
    }
    
    public static void generateChunk(World world) {
        world.chunk = new OctreeChunk(world.blocks.get(0));
        world.chunk.setBlock(0, 0, 0, world.blocks.get(1));
        world.chunk.setBlock(0, 0, 1, world.blocks.get(2));
    }
    
    public static World generateWorld() {
        World world = new World();
        
        generateSubstances(world);
        
        generateSoilTypes(world);
        
        generateBlocks(world);

        generateChunk(world);
        
        return world;
    }
}