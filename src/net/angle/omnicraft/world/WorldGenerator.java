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
import net.angle.omnicraft.world.blocks.SoilBlock;
import net.angle.omnicraft.world.blocks.SteppedCubeShape;
import net.angle.omnicraft.world.types.Fluid;
import net.angle.omnicraft.world.types.GranularMaterial;
import net.angle.omnicraft.world.types.Mineraloid;
import net.angle.omnicraft.world.types.SoilFraction;
import net.angle.omnicraft.world.types.SoilType;

/**
 *
 * @author angle
 */
public class WorldGenerator {
    
    public static void generateSubstances(World world) {
        world.substances.put("Grey Stuff", new Mineraloid("Grey Stuff", new VariedColorPixelSource(Color.darkGray, 60)));
        world.substances.put("Grey Particles", new GranularMaterial("Grey Particles", world.substances.get("Grey Stuff")));
        world.substances.put("Water", new Fluid("Water", new ColoredVariation(-3, -3, -1)));
        world.substances.put("Air", new Fluid("Air", new ColoredVariation(2, 2, 1)));
    }
    
    public static void generateSoilTypes(World world) {
        world.soilTypes.add(new SoilType("Dirt", new SoilFraction(world.substances.get("Grey Particles"), 50.0f), 
                new SoilFraction(world.substances.get("Water"), 25.0f), new SoilFraction(world.substances.get("Air"), 25.0f)));
        world.soilTypes.add(new SoilType("Gravel", new SoilFraction(world.substances.get("Grey Particles"), 100.0f)));
    }
    
    public static void generateBlocks(World world) {
        world.blocks.add(new SoilBlock(world.soilTypes.get(0), new SteppedCubeShape(12), new OmniRandom()));
        world.blocks.add(new SoilBlock(world.soilTypes.get(1), new CubeShape(), new OmniRandom()));
        world.blocks.add(new SoilBlock(world.soilTypes.get(1), new SteppedCubeShape(4), new OmniRandom()));
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
