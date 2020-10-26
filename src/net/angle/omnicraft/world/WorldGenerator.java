/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world;

import java.awt.Color;
import net.angle.omnicraft.random.OmniRandom;
import net.angle.omnicraft.textures.LayeredTextureSource;
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
    
    static int dirtCount;
    
    public static void generateSubstances(World world) {
        world.substances.put("Pebbles", new Mineraloid("Pebbles", new VariedColorPixelSource(Color.darkGray, 60)));
        world.substances.put("Sand", new Mineraloid("Sand", new VariedColorPixelSource(new Color(80, 60, 30), 60)));
        world.substances.put("Silt", new Mineraloid("Silt", new VariedColorPixelSource(new Color(70, 40, 40), 60)));
        world.substances.put("Clay", new Mineraloid("Clay", new VariedColorPixelSource(new Color(60, 30, 30), 60)));
        world.substances.put("Compost", new Mineraloid("Compost", new VariedColorPixelSource(new Color(80, 40, 30), 60)));
        world.substances.put("Water", new Fluid("Water", new ColoredVariation(-5, -3, -2)));
        world.substances.put("Air", new Fluid("Air", new ColoredVariation(2, 1, 2)));
        world.substances.put("Gravel", new Mixture("Gravel", new MixtureComponent(world.substances.get("Pebbles"), 100.0f)));
        
        dirtCount = 0;
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                generateDirtType(world, "Dirt" + dirtCount, 6, 6, 3, 0.05f * (1 + i), 0.05f * (1 + j), 0.6f);
                dirtCount++;
            }
        }
    }
    
    public static Mixture generateDirtType(World world, String name, int redVar, int greenVar, int blueVar, float downChance, float upChance, float randomChance) {
        Mixture dirt = new Mixture(name, new MixtureComponent(world.substances.get("Sand"), 15.0f),
                new MixtureComponent(world.substances.get("Silt"), 18.0f), new MixtureComponent(world.substances.get("Clay"), 9.0f), 
                new MixtureComponent(world.substances.get("Compost"), 5.0f), new MixtureComponent(world.substances.get("Pebbles"), 3.0f), 
                new MixtureComponent(world.substances.get("Water"), 25.0f), new MixtureComponent(world.substances.get("Air"), 25.0f));
        
        ColoredVariation upVariation = new ColoredVariation(redVar, greenVar, blueVar);
        ColoredVariation downVariation = new ColoredVariation(-redVar, -greenVar, -blueVar);
        
        dirt.setTextureSource(new LayeredTextureSource(dirt, (int x, int y, Color currentLineColor, OmniRandom random) -> {
            if (random.nextFloat() <= downChance)
                return upVariation.varyPixel(currentLineColor, random);
            else if (random.nextFloat() <= upChance)
                return downVariation.varyPixel(currentLineColor, random);
            else if (random.nextFloat() <= randomChance)
                return dirt.getPixelColor(random, dirt);
            else return currentLineColor;
        }));
        
        world.substances.put(name, dirt);
        
        return dirt;
    }
    
    public static void generateBlocks(World world) {
        for (int i = 0; i < dirtCount; i++) {
            world.blocks.add(new HomogenousBlock(world.substances.get("Dirt" + i), new CubeShape(), new OmniRandom()));
        }
    }
    
    public static void generateChunk(World world) {
        world.chunk = new OctreeChunk(world.blocks.get(0));
        int x = 0, y = 0, z = 0;
        for (int i = 1; i < dirtCount; i++) {
            z++;
            if (z >= world.chunk.size) {
                z = 0;
                y++;
            }
            world.chunk.setBlock(x, y, z, world.blocks.get(i));
        }
    }
    
    public static World generateWorld() {
        World world = new World();
        
        generateSubstances(world);
        
        generateBlocks(world);

        generateChunk(world);
        
        return world;
    }
}
