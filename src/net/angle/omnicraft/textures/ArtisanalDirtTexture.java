/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.textures;

import java.awt.Color;
import net.angle.omnicraft.random.OmniRandom;
import net.angle.omnicraft.textures.pixels.PixelSource;

/**
 *
 * @author angle
 */
public class ArtisanalDirtTexture extends PaletteLayeredTextureSource {

    public ArtisanalDirtTexture(PixelSource pixelSource, int palletteSize, ColorVariationCallback... colorVariationCallbacks) {
        super(pixelSource, palletteSize, colorVariationCallbacks);
    }
    
    public Color getBaseColor(OmniRandom random) {
        return this.getAveragedPixelColor(5, random, this);
    }
    
    public Color[][] drawClump(int x, int y, Color[][] tex) {
        
        
        return tex;
    }

    @Override
    public Color[][] getPixelColors(int width, int height, OmniRandom random) {
        Color[][] tex = new Color[width][height];
        
        Color baseColor = getBaseColor(random);
        
        for (Color[] line : tex) {
            for (int i = 0; i < line.length; i++) {
                line[i] = baseColor;
            }
        }
        
        int clumpDistance = 2;
        
        //for each line
        for (int i = 0; i < tex[0].length/clumpDistance; i++) {
            //for every few pixels
            for (int j = 0; j < tex.length/clumpDistance; j++) {
                tex = drawClump(j, i, tex);
            } 
        }
        
        return tex;
    }
    
}
