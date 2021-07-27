/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.utils;

import com.samrj.devil.math.Vec2;
import net.angle.omnicraft.world.Chunk;
import net.angle.omnicraft.world.blocks.Block;


/**
 *
 * @author angle
 */
public class HeightMapGenerator {
    private static final OmniRandom r = new OmniRandom();
    
    public static int[][] flat(int x, int z, int height) {
        int[][] heights = new int[x][z];
        
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < z; j++) {
                heights[i][j] = height;
            }
        }
        return heights;
    }
    
    public static float getValue(int x, int z) {
        return getValue(x, z, 0);
    }
    
    public static float getValue(int x, int z, int i) {
        r.setSeed(x * 234567 + z * 98527 + i * 345678);
        return r.nextFloat();
    }
    
    public static Vec2 getPerlinDir(int x, int z) {
        return new Vec2(getValue(x, z, 0) * 2 - 1, getValue(x, z, 1) * 2 - 1);
    }
    
    public static int getValueChunkHeight(int chunkx, int chunkz, int min, int max) {
        return OmniMath.mix(min, max, getValue(chunkx, chunkz));
    }
    
    public static int getValueBlockHeight(int blockx, int blockz, int topLeft, int topRight, int bottomLeft, int bottomRight) {
        float interpolatorX = blockx/16f;
        float interpolatorZ = blockz/16f;

        interpolatorX = OmniMath.smoothInterp(interpolatorX);
        interpolatorZ = OmniMath.smoothInterp(interpolatorZ);

        int upperCells = OmniMath.mix(topLeft, topRight, interpolatorX);
        int lowerCells = OmniMath.mix(bottomLeft, bottomRight, interpolatorX);
        return OmniMath.mix(upperCells, lowerCells, interpolatorZ);
    }
    
    public static Chunk generateDirtFromValueHeightMap(Chunk chunk) {
        Block dirt = chunk.world.blockTypes.get("Dirt Block");
        int chunkx = chunk.getX();
        int chunkz = chunk.getZ();
        
        int topLeft = HeightMapGenerator.getValueChunkHeight(chunkx, chunkz, 1, 15);
        int topRight = HeightMapGenerator.getValueChunkHeight(chunkx + 1, chunkz, 1, 15);
        int bottomLeft = HeightMapGenerator.getValueChunkHeight(chunkx, chunkz + 1, 1, 15);
        int bottomRight = HeightMapGenerator.getValueChunkHeight(chunkx + 1, chunkz + 1, 1, 15);
        
        for (int blockx = 0; blockx < chunk.getEdgeLength() ; blockx++) {
            for (int blockz = 0; blockz < chunk.getEdgeLength(); blockz++) {
                
                int height = HeightMapGenerator.getValueBlockHeight(blockx, blockz, topLeft, topRight, bottomLeft, bottomRight);
                
                chunk.setBlocksBelow(blockx, blockz, height, dirt);
            }
        }
        chunk.setAllSidesOnSurface(Block.BlockFace.top, chunk.world.sideTypes.get("Grass"));
        return chunk;
    }
    
    
    
    public static Chunk generateDirtFromPerlinHeightMap(Chunk chunk) {
        Block dirt = chunk.world.blockTypes.get("Dirt Block");
        
        int chunkx = chunk.getX();
        int chunkz = chunk.getZ();
        
        Vec2 topLeftDir = HeightMapGenerator.getPerlinDir(chunkx, chunkz);
        Vec2 topRightDir = HeightMapGenerator.getPerlinDir(chunkx + 1, chunkz);
        Vec2 bottomLeftDir = HeightMapGenerator.getPerlinDir(chunkx, chunkz + 1);
        Vec2 bottomRightDir = HeightMapGenerator.getPerlinDir(chunkx + 1, chunkz + 1);
        
        for (int blockx = 0; blockx < chunk.getEdgeLength() ; blockx++) {
            for (int blockz = 0; blockz < chunk.getEdgeLength(); blockz++) {
                
                float interpolatorX = blockx/16f;
                float interpolatorZ = blockz/16f;
                
                Vec2 interpolator = new Vec2(interpolatorX, interpolatorZ);
        
                float topLeftValue = topLeftDir.dot(interpolator);
                float topRightValue = topRightDir.dot(interpolator);
                float bottomLeftValue = bottomLeftDir.dot(interpolator);
                float bottomRightValue = bottomRightDir.dot(interpolator);

                interpolatorX = OmniMath.smoothInterp(interpolatorX);
                interpolatorZ = OmniMath.smoothInterp(interpolatorZ);
                
                int topLeft = OmniMath.mix(1, 15, topLeftValue);
                int topRight = OmniMath.mix(1, 15, topRightValue);
                int bottomLeft = OmniMath.mix(1, 15, bottomLeftValue);
                int bottomRight = OmniMath.mix(1, 15, bottomRightValue);

                int upperCells = OmniMath.mix(topLeft, topRight, interpolatorX);
                int lowerCells = OmniMath.mix(bottomLeft, bottomRight, interpolatorX);
                int height = OmniMath.mix(upperCells, lowerCells, interpolatorZ);
                
                chunk.setBlocksBelow(blockx, blockz, height, dirt);
            }
        }
        chunk.setAllSidesOnSurface(Block.BlockFace.top, chunk.world.sideTypes.get("Grass"));
        return chunk;
    }
}
