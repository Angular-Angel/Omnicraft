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
public class ArtisanalDirtTexture extends AbstractTextureSource {

    public ArtisanalDirtTexture(PixelSource pixelSource) {
        super(pixelSource);
    }
    
    public Color getBaseColor(OmniRandom random) {
        return this.getAveragedPixelColor(5, random, this);
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
        
        
        
        //for each line
        for (int i = 0; i < tex[0].length; i++) {
            //for each pixel
            for (int j = 0; j < tex.length; j++) {
                
            } 
        }
        
        return tex;
    }
    
}
