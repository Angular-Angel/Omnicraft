/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world;

import java.awt.Color;
import net.angle.omnicraft.graphics.ColorSource;
import net.angle.omnicraft.graphics.RenderData;
import net.angle.omnicraft.random.OmniRandom;
import net.angle.omnicraft.world.blocks.Block;
import net.angle.omnicraft.world.blocks.CubeShape;
import net.angle.omnicraft.world.blocks.HomogenousBlock;
import net.angle.omnicraft.world.blocks.Splatter;
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
    
    public int[][] heightMap;
    
    public void generateAbstract(World world) {
        generateSubstances(world);
        generateBlocks(world);
        heightMap = HeightMapGenerator.valueNoise(10, 10, 1, 16, new OmniRandom());
    }
    
    public void generateSubstances(World world) {
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
    
    public void generateBlocks(World world) {
        world.addBlockType(new HomogenousBlock("Dirt Block", world.get_next_block_id(), world.substances.get("Dirt"), new CubeShape(), new OmniRandom()));
        world.addBlockType(new HomogenousBlock("Gravel Block", world.get_next_block_id(), world.substances.get("Gravel"), new CubeShape(), new OmniRandom()));
        world.addBlockType(new HomogenousBlock("Desert Sand Block", world.get_next_block_id(), world.substances.get("Desert Sand"), new CubeShape(), new OmniRandom()));
        world.addSideType(new Splatter("Moss", world.get_next_side_id(), new RenderData(new Color(1, 0, 0), new Color(0, 128, 0))));
    }
    
    public Chunk generateDirtFloor(Chunk chunk) {
        chunk.setAllBlocks(chunk.world.blockTypes.get("Dirt Block"));
        return chunk;
    }
    
    public Chunk generateDirtFromHeightMap(Chunk chunk) {
        Block dirt = chunk.world.blockTypes.get("Dirt Block");
        int chunkx = chunk.getX();
        int chunkz = chunk.getZ();
        for (int blockx = 0; blockx < chunk.getEdgeLength() ; blockx++) {
            for (int blockz = 0; blockz < chunk.getEdgeLength(); blockz++) {
                
                float interpolatorX = blockx/16f;
                float interpolatorZ = blockz/16f;
                
                int upperCells = OmniRandom.mix(heightMap[chunkx][chunkz], heightMap[chunkx + 1][chunkz], interpolatorX);
                int lowerCells = OmniRandom.mix(heightMap[chunkx][chunkz + 1], heightMap[chunkx + 1][chunkz + 1], interpolatorX);
                int height = OmniRandom.mix(lowerCells, upperCells, interpolatorZ);
                
                chunk.setBlocksBelow(blockx, blockz, height, dirt);
            }
        }
        return chunk;
    }
    
    public Chunk generateChunk(Chunk chunk) {
        if (chunk.getY() == 0)
            return generateDirtFloor(chunk);
        if (chunk.getY() == 1 && chunk.getX() < heightMap.length - 1 && chunk.getX() >= 0
                              && chunk.getZ() < heightMap[0].length - 1 && chunk.getZ() >= 0)
            return generateDirtFromHeightMap(chunk);
            
        else return chunk;
    }
    
    public void generateSpawnRegion(World world) {
        Chunk chunk = world.checkChunk(0, 0, 0);
        chunk.setAllBlocks(world.blockTypes.get("Desert Sand Block"));
        chunk.setBlock(0, 0, 0, world.blockTypes.get("Gravel Block"));
        chunk.setBlock(chunk.getEdgeLength() - 1, 1, 0, world.blockTypes.get("Gravel Block"));
        chunk.setBlock(0, chunk.getEdgeLength() - 1, 0, world.blockTypes.get("Gravel Block"));

        chunk.setSide(Block.BlockFace.left, 0, 0, 0, world.sideTypes.get("Moss"));
        chunk.setSide(Block.BlockFace.front, 0, 0, 0, world.sideTypes.get("Moss"));
        
        world.checkChunk(1, 1, 1).setAllBlocks(world.blockTypes.get("Desert Sand Block"));
        world.checkChunk(2, 2, 2).setAllBlocks(world.blockTypes.get("Gravel Block"));
        world.checkChunk(15, 1, 1).setAllBlocks(world.blockTypes.get("Desert Sand Block"));
        
        world.checkChunk(0, 3, 0).setBlock(0, 0, 0, world.blockTypes.get("Gravel Block"));
        
        world.checkChunk(0, 3, 0).setSide(Block.BlockFace.left, 0, 0, 0, world.sideTypes.get("Moss"));
        world.checkChunk(0, 3, 0).setSide(Block.BlockFace.front, 0, 0, 0, world.sideTypes.get("Moss"));
    }
}
