/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.utils;

import com.samrj.devil.math.Vec2;


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
        return new Vec2(getValue(x, z, 0), getValue(x, z, 1));
    }
    
    public static int getChunkHeight(int chunkx, int chunkz, int min, int max) {
        return OmniMath.mix(min, max, getValue(chunkx, chunkz));
    }
    
    public static int getBlockHeight(int blockx, int blockz, int topLeft, int topRight, int bottomLeft, int bottomRight) {
        float interpolatorX = blockx/16f;
        float interpolatorZ = blockz/16f;

        interpolatorX = OmniMath.smoothInterp(interpolatorX);
        interpolatorZ = OmniMath.smoothInterp(interpolatorZ);

        int upperCells = OmniMath.mix(topLeft, topRight, interpolatorX);
        int lowerCells = OmniMath.mix(bottomLeft, bottomRight, interpolatorX);
        return OmniMath.mix(upperCells, lowerCells, interpolatorZ);
    }
    
    public static int getValueHeight(int x, int z, int min, int max) {
        float upperCells = OmniMath.mix(getValue(x, z), getValue(x + 1, z), 0.5f);
        float lowerCells = OmniMath.mix(getValue(x, z + 1), getValue(x + 1, z + 1), 0.5f);
        return OmniMath.mix(min, max, OmniMath.mix(lowerCells, upperCells, 0.5f));
    }
    
    public static int[][] valueNoise(int x, int z, int min, int max, OmniRandom random) {
        float[][] relativeHeights = new float[x + 1][z + 1];
        
        for (int i = 0; i <= x; i++) {
            for (int j = 0; j <= z; j++) {
                relativeHeights[i][j] = random.nextFloat();
            }
        }
        
        int[][] heights = new int[x][z];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < z; j++) {
                float upperCells = OmniMath.mix(relativeHeights[i][j], relativeHeights[i + 1][j], 0.5f);
                float lowerCells = OmniMath.mix(relativeHeights[i][j + 1], relativeHeights[i + 1][j + 1], 0.5f);
                heights[i][j] = OmniMath.mix(min, max, OmniMath.mix(lowerCells, upperCells, 0.5f));
            }
        }
        
        return heights;
    }
}
