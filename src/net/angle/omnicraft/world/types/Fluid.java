/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world.types;

import com.samrj.devil.gl.Texture2D;
import java.awt.Color;
import net.angle.omnicraft.textures.pixels.PixelSource;
import net.angle.omnicraft.random.OmniRandom;
import net.angle.omnicraft.textures.pixels.PixelVariation;

/**
 *
 * @author angle
 * @license https://gitlab.com/AngularAngel/omnicraft/-/blob/master/LICENSE
 */
public class Fluid extends Substance {

    private final PixelSource pixelSource;
    
    public Fluid(String name, PixelSource pixelSource) {
        super(name);
        this.pixelSource = pixelSource;
    }
    
    @Override
    public Color getPixelColor(OmniRandom random, PixelSource context) {
        Color color = pixelSource.getPixelColor(random, context);
        
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), 10);
    }
}
