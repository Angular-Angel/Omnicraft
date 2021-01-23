/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.graphics;

import java.awt.Color;
import net.angle.omnicraft.random.OmniRandom;

/**
 *
 * @author angle
 */
@FunctionalInterface
public interface ColorSource {
    public Color getColor(OmniRandom random);
    
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
}
