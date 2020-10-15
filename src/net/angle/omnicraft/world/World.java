/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world;

import java.awt.Color;
import net.angle.omnicraft.pixel.GreyVariedPixelSource;
import net.angle.omnicraft.random.OmniRandom;
import net.angle.omnicraft.world.blocks.Block;
import net.angle.omnicraft.world.blocks.CubeShape;
import net.angle.omnicraft.world.blocks.SoilBlock;
import net.angle.omnicraft.world.blocks.SteppedCubeShape;
import net.angle.omnicraft.world.types.MineralGrain;
import net.angle.omnicraft.world.types.Mineraloid;
import net.angle.omnicraft.world.types.SoilFraction;
import net.angle.omnicraft.world.types.SoilType;

/**
 *
 * @author angle
 */
public class World {
    private Block[] blocks;
    private Chunk chunk;
    
    public World() {
            blocks = new Block[2];
            blocks[0] = new SoilBlock(new SoilType(new SoilFraction(new MineralGrain(
                    new Mineraloid(new GreyVariedPixelSource(Color.darkGray, 60)), 1.0f), 100.0f)),
                new SteppedCubeShape(12),new OmniRandom());
            
            chunk = new OctreeChunk(blocks[0]);
            
            blocks[1] = new SoilBlock(new SoilType(new SoilFraction(new MineralGrain(
                    new Mineraloid(new GreyVariedPixelSource(Color.darkGray, 60)), 1.0f), 100.0f)),
                new CubeShape(),new OmniRandom());
            
            chunk.setBlock(0, 0, 0, blocks[1]);
        
    }
    
    public void draw() {
        chunk.draw();
    }
    
    public void delete() {
        for (Block block : blocks)
            block.delete();
    }
}
