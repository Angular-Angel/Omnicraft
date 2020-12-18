/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world.types;

import com.samrj.devil.gl.Texture2D;
import java.awt.Color;
import net.angle.omnicraft.random.OmniRandom;
import net.angle.omnicraft.textures.pixels.PixelSource;

/**
 * I'm going to have to rewrite this if I ever want transparent minerals... 
 * Oh well, I'll deal with it then.
 * @author angle
 * @license https://gitlab.com/AngularAngel/omnicraft/-/blob/master/LICENSE
 */
public class Mineraloid extends Substance {
    
    private final PixelSource pixelSource;
    private final int grainSize;
    
    public Mineraloid(String name, PixelSource pixelSource, int grainSize) {
        super(name);
        this.pixelSource = pixelSource;
        this.grainSize = grainSize;
    }

    @Override
    public Color getPixelColor(OmniRandom random, PixelSource context) {
        Color color = pixelSource.getPixelColor(random, context);
        
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), grainSize);
    }
    
}
