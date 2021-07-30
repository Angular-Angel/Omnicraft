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
    private static final SimplexNoise simplex = new SimplexNoise();
    
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
    
    private static final float sqrt2o2 = (float) Math.sqrt(2)/2;
    
    private static final Vec2[] perlinDirs = new Vec2[]{new Vec2(1, 0), new Vec2(-1, 0), new Vec2(0, 1), new Vec2(0, -1),
                                           new Vec2(sqrt2o2, sqrt2o2), new Vec2(-sqrt2o2, sqrt2o2), new Vec2(sqrt2o2, -sqrt2o2), new Vec2(-sqrt2o2, -sqrt2o2)};
    
    public static Vec2 getRandomDir(int x, int z) {
        return new Vec2(getValue(x, z, 0) * 2 - 1, getValue(x, z, 1) * 2 - 1).normalize();
    }
    
    public static Vec2 getPerlinDir(int x, int z) {
        r.setSeed(x * 234567 + z * 98527);
        return perlinDirs[r.getBoundedInt(8)];
    }
    
    public static int getValueChunkHeight(int chunkx, int chunkz, int min, int max) {
        return OmniMath.mix(min, max, getValue(chunkx, chunkz));
    }
    
    public static int getValueBlockHeight(int blockx, int blockz, int topLeft, int topRight, int bottomLeft, int bottomRight) {
        float interpolatorX = blockx/16f;
        float interpolatorZ = blockz/16f;

        interpolatorX = OmniMath.fifthDegPoly(interpolatorX);
        interpolatorZ = OmniMath.fifthDegPoly(interpolatorZ);

        int upperCells = OmniMath.mix(topLeft, topRight, interpolatorX);
        int lowerCells = OmniMath.mix(bottomLeft, bottomRight, interpolatorX);
        return OmniMath.mix(upperCells, lowerCells, interpolatorZ);
    }
    
    public static Chunk generateDirtFromValueHeightMap(Chunk chunk, int min, int max) {
        Block dirt = chunk.world.blockTypes.get("Dirt Block");
        int chunkx = chunk.getX();
        int chunkz = chunk.getZ();
        
        int topLeft = HeightMapGenerator.getValueChunkHeight(chunkx, chunkz, min, max);
        int topRight = HeightMapGenerator.getValueChunkHeight(chunkx + 1, chunkz, min, max);
        int bottomLeft = HeightMapGenerator.getValueChunkHeight(chunkx, chunkz + 1, min, max);
        int bottomRight = HeightMapGenerator.getValueChunkHeight(chunkx + 1, chunkz + 1, min, max);
        
        for (int blockx = 0; blockx < chunk.getEdgeLength() ; blockx++) {
            for (int blockz = 0; blockz < chunk.getEdgeLength(); blockz++) {
                
                int height = HeightMapGenerator.getValueBlockHeight(blockx, blockz, topLeft, topRight, bottomLeft, bottomRight);
                
                chunk.setBlocksBelow(blockx, blockz, height, dirt);
            }
        }
        chunk.setAllSidesOnSurface(Block.BlockFace.top, chunk.world.sideTypes.get("Grass"));
        return chunk;
    }
    
    public static Chunk generateDirtFromPerlinHeightMap(Chunk chunk, int min, int max) {
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
        
                float topLeftValue = topLeftDir.dot(new Vec2(interpolator).sub(new Vec2(0, 0))) * 0.5f + 0.5f;
                float topRightValue = topRightDir.dot(new Vec2(interpolator).sub(new Vec2(1, 0))) * 0.5f + 0.5f;
                float bottomLeftValue = bottomLeftDir.dot(new Vec2(interpolator).sub(new Vec2(0, 1))) * 0.5f + 0.5f;
                float bottomRightValue = bottomRightDir.dot(new Vec2(interpolator).sub(new Vec2(1, 1))) * 0.5f + 0.5f;

                interpolatorX = OmniMath.fifthDegPoly(interpolatorX);
                interpolatorZ = OmniMath.fifthDegPoly(interpolatorZ);
                
                int topLeft = OmniMath.mix(min, max, topLeftValue);
                int topRight = OmniMath.mix(min, max, topRightValue);
                int bottomLeft = OmniMath.mix(min, max, bottomLeftValue);
                int bottomRight = OmniMath.mix(min, max, bottomRightValue);

                int upperCells = OmniMath.mix(topLeft, topRight, interpolatorX);
                int lowerCells = OmniMath.mix(bottomLeft, bottomRight, interpolatorX);
                int height = OmniMath.mix(upperCells, lowerCells, interpolatorZ);
                
                chunk.setBlocksBelow(blockx, blockz, height, dirt);
            }
        }
        chunk.setAllSidesOnSurface(Block.BlockFace.top, chunk.world.sideTypes.get("Grass"));
        return chunk;
    }
    
    public static Chunk generateDirtFromSimplexHeightMap(Chunk chunk, int min, int max) {
        Block dirt = chunk.world.blockTypes.get("Dirt Block");
        
        for (int blockx = 0; blockx < chunk.getEdgeLength() ; blockx++) {
            for (int blockz = 0; blockz < chunk.getEdgeLength(); blockz++) {
                int height = OmniMath.mix(min, max, (float) simplex.noise(chunk.getX() + blockx/16.0, chunk.getZ() + blockz/16.0) * 0.5f + 0.5f);
                
                chunk.setBlocksBelow(blockx, blockz, height, dirt);
            }
        }
        chunk.setAllSidesOnSurface(Block.BlockFace.top, chunk.world.sideTypes.get("Grass"));
        return chunk;
    }
}
