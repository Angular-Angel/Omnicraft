/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.textures;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.angle.omnicraft.random.OmniRandom;
import net.angle.omnicraft.textures.pixels.PixelSource;

/**
 *
 * @author angle
 */
public class PaletteLayeredTextureSource extends LayeredTextureSource {
    
    public final int paletteSize;
    protected List<Color> palette;
    
    public PaletteLayeredTextureSource(PixelSource pixelSource, int palletteSize, ColorVariationCallback... colorVariationCallbacks) {
        super(pixelSource, colorVariationCallbacks);
        this.paletteSize = palletteSize;
        palette = new ArrayList<>();
    }
    
    public void createPalette(OmniRandom random) {
        palette.clear();
        for (int i = 0; i < paletteSize; i++)
            palette.add(this.getAveragedPixelColor(3, random, this));
    }
    
    public Color getPaletteColor(OmniRandom random) {
        return palette.get(random.getBoundedInt(paletteSize));
    }
    
    @Override
    public void colorLine(int y, Color[][] tex, OmniRandom random) {
        Color lineColor = this.getPaletteColor(random);
        for (int i = 0; i < tex.length; i++) {
            tex[i][y] = lineColor;
            lineColor = varyLineColor(i, y, lineColor, random);
        } 
    }
    
    @Override
    public Color[][] getPixelColors(int width, int height, OmniRandom random) {
        createPalette(random);
        
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
        
        for (int i = 0; i < tex.length; i++) {
            for (int j = 0; j < tex[i].length; j++) {
                tex = setPixel(i, j, tex, tex[i][j], random);
            }
        }
        
        return tex;
    }
    
}
