/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.textures;

import com.samrj.devil.gl.Texture2D;
import net.angle.omnicraft.random.OmniRandom;
import net.angle.omnicraft.textures.pixels.PixelSource;

/**
 *
 * @author angle
 * @license https://gitlab.com/AngularAngel/omnicraft/-/blob/master/LICENSE
 */
public interface TextureSource extends PixelSource {
    
    public Texture2D generateTexture(OmniRandom random);
    
    public Texture2D generateTexture(int width, int height, OmniRandom random);
}
