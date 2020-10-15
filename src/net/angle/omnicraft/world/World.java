/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.angle.omnicraft.random.OmniRandom;
import net.angle.omnicraft.textures.pixels.VariedColorPixelSource;
import net.angle.omnicraft.world.blocks.Block;
import net.angle.omnicraft.world.blocks.CubeShape;
import net.angle.omnicraft.world.blocks.SoilBlock;
import net.angle.omnicraft.world.blocks.SteppedCubeShape;
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
    private List<Block> blocks;
    private List<Substance> substances;
    private Chunk chunk;
    
    public World() {
            blocks = new ArrayList<>();
            substances = new ArrayList<>();
            substances.add(new Mineraloid(new VariedColorPixelSource(Color.darkGray, 60)));
            
            blocks.add(new SoilBlock(new SoilType(new SoilFraction(new GranularMaterial(
                    substances.get(0)), 100.0f)), new SteppedCubeShape(12),new OmniRandom()));
            
            chunk = new OctreeChunk(blocks.get(0));
            
            blocks.add(new SoilBlock(new SoilType(new SoilFraction(new GranularMaterial(
                    substances.get(0)), 100.0f)), new CubeShape(),new OmniRandom()));
            
            blocks.add(new SoilBlock(new SoilType(new SoilFraction(new GranularMaterial(
                    substances.get(0)), 100.0f)), new SteppedCubeShape(4),new OmniRandom()));
            
            chunk.setBlock(0, 0, 0, blocks.get(1));
            
            chunk.setBlock(0, 0, 1, blocks.get(2));
    }
    
    public void draw() {
        chunk.draw();
    }
    
    public void delete() {
        for (Block block : blocks)
            block.delete();
    }
}
