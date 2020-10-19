/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.angle.omnicraft.random.OmniRandom;
import net.angle.omnicraft.textures.pixels.ColoredVariation;
import net.angle.omnicraft.textures.pixels.VariedColorPixelSource;
import net.angle.omnicraft.world.blocks.Block;
import net.angle.omnicraft.world.blocks.CubeShape;
import net.angle.omnicraft.world.blocks.SoilBlock;
import net.angle.omnicraft.world.blocks.SteppedCubeShape;
import net.angle.omnicraft.world.types.Fluid;
import net.angle.omnicraft.world.types.GranularMaterial;
import net.angle.omnicraft.world.types.Mineraloid;
import net.angle.omnicraft.world.types.SoilFraction;
import net.angle.omnicraft.world.types.SoilType;
import net.angle.omnicraft.world.types.Substance;

/**
 *
 * @author angle
 */
public class World {
    public final Map<String, Substance> substances;
    public final List<SoilType> soilTypes;
    public final List<Block> blocks;
    
    public Chunk chunk;
    
    public World() {
        substances = new HashMap<>();
        soilTypes = new ArrayList<>();
        blocks = new ArrayList<>();

//        substances.put("Grey Stuff", new Mineraloid("Grey Stuff", new VariedColorPixelSource(Color.darkGray, 60)));
//        substances.put("Grey Particles", new GranularMaterial("Grey Particles", substances.get("Grey Stuff")));
//        substances.put("Water", new Fluid("Water", new ColoredVariation(-3, -3, -1)));
//        substances.put("Air", new Fluid("Air", new ColoredVariation(2, 2, 1)));
//
//        soilTypes.add(new SoilType("Dirt", new SoilFraction(substances.get("Grey Particles"), 50.0f), 
//                new SoilFraction(substances.get("Water"), 25.0f), new SoilFraction(substances.get("Air"), 25.0f)));
//        soilTypes.add(new SoilType("Gravel", new SoilFraction(substances.get("Grey Particles"), 100.0f)));
//
//        blocks.add(new SoilBlock(soilTypes.get(0), new SteppedCubeShape(12), new OmniRandom()));
//        blocks.add(new SoilBlock(soilTypes.get(1), new CubeShape(), new OmniRandom()));
//        blocks.add(new SoilBlock(soilTypes.get(1), new SteppedCubeShape(4), new OmniRandom()));
//
//        chunk = new OctreeChunk(blocks.get(0));
//        chunk.setBlock(0, 0, 0, blocks.get(1));
//        chunk.setBlock(0, 0, 1, blocks.get(2));
    }
    
    public void draw() {
        chunk.draw();
    }
    
    public void delete() {
        for (Block block : blocks)
            block.delete();
    }
}
