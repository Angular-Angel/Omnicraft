/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.graphics;

import com.samrj.devil.math.Vec3i;
import java.awt.Color;
import net.angle.omnicraft.random.OmniRandom;

/**
 *
 * @author angle
 */
@FunctionalInterface
public interface ColorSource {
    public Color getColor(OmniRandom random);
    
    public default Color[] getPalette(int paletteSize, OmniRandom random) {
        Color[] palette = new Color[paletteSize + 1];
        
        Vec3i average = new Vec3i();
        for (int i = 0; i < paletteSize; i++) {
            palette[i] = getColor(random);
            average.x += palette[i].getRed();
            average.y += palette[i].getGreen();
            average.z += palette[i].getBlue();
        }
        average = average.div(paletteSize);
        
        palette[paletteSize] = new Color(average.x, average.y, average.z);
        
        return palette;
    }
    
    public static class FlatColorSource implements ColorSource {

        public final Color color;
        
        public FlatColorSource(Color color) {
            this.color = color;
        }
        
        @Override
        public Color getColor(OmniRandom random) {
            return color;
        }
        
    }
    
    public static class GreyVariedColorSource extends ColorSource.FlatColorSource {
        public final int variation;

        public GreyVariedColorSource(Color color, int variation) {
            super(color);
            this.variation = variation;
        }
        
        @Override
        public Color getColor(OmniRandom random) {
            int local_variation = random.getBoundedInt(this.variation);
            return new Color(Math.max(0, color.getRed() + local_variation), 
                             Math.max(0, color.getGreen() + local_variation), 
                             Math.max(0, color.getBlue() + local_variation), color.getAlpha());
        }
        
    }
}
