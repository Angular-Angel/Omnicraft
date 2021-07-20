/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world;

import net.angle.omnicraft.random.OmniRandom;

/**
 *
 * @author angle
 */
public class HeightMapGenerator {
    public static int[][] flat(int x, int z, int height) {
        int[][] heights = new int[x][z];
        
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < z; j++) {
                heights[i][j] = height;
            }
        }
        return heights;
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
                float upperCells = random.mix(relativeHeights[i][j], relativeHeights[i + 1][j], 0.5f);
                float lowerCells = random.mix(relativeHeights[i][j + 1], relativeHeights[i + 1][j + 1], 0.5f);
                heights[i][j] = random.mix(min, max, random.mix(lowerCells, upperCells, 0.5f));
            }
        }
        
        return heights;
    }
}
