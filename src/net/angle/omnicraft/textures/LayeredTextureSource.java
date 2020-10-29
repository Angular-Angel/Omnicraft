/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.textures;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.angle.omnicraft.random.OmniRandom;
import net.angle.omnicraft.textures.pixels.PixelSource;

/**
 *
 * @author angle
 */
public class LayeredTextureSource extends AbstractTextureSource {
    
    public final List<ColorVariationCallback> colorVariationCallbacks;
    
    public LayeredTextureSource(PixelSource pixelSource, ColorVariationCallback... colorVariationCallbacks) {
        super(pixelSource);
        this.colorVariationCallbacks = new ArrayList<>();
        this.colorVariationCallbacks.addAll(Arrays.asList(colorVariationCallbacks));
    }
    
    public Color getBaseColor(OmniRandom random) {
        return this.getPixelColor(random, this);
    }
    
    public void colorLine(int y, Color[][] tex, OmniRandom random) {
        Color lineColor = this.getPixelColor(random, this);
        for (int i = 0; i < tex.length; i++) {
            tex[i][y] = lineColor;
            lineColor = varyLineColor(i, y, lineColor, random);
        } 
    }
    
    public Color varyLineColor(int x, int y, Color currentLineColor, OmniRandom random) {
        for (ColorVariationCallback colorVariationCallback : colorVariationCallbacks) {
            currentLineColor = colorVariationCallback.varyColor(x, y, currentLineColor, random);
        }
        
        return currentLineColor;
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
        
        for (int i = 0; i < tex[0].length; i++) {
            colorLine(i, tex, random);
        }
        
        return tex;
    }
}
