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
    
    public Mineraloid(String name, PixelSource pixelSource) {
        super(name);
        this.pixelSource = pixelSource;
    }

    @Override
    public Color getPixelColor(OmniRandom random, PixelSource context) {
        return pixelSource.getPixelColor(random, context);
    }

    @Override
    public Texture2D generateTexture(OmniRandom random) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Texture2D generateTexture(int width, int height, OmniRandom random) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
