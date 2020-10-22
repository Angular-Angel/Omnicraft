/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world.types;

import com.samrj.devil.gl.Texture2D;
import net.angle.omnicraft.random.OmniRandom;
import net.angle.omnicraft.textures.pixels.TextureSource;
/**
 *
 * @author angle
 * @license https://gitlab.com/AngularAngel/omnicraft/-/blob/master/LICENSE
 */
public abstract class Substance implements TextureSource {
    private final String name;
    protected TextureSource textureSource;
    
    public Substance(String name){
        this(name, null);
    }
    
    public Substance(String name, TextureSource textureSource) {
        this.name = name;
        
        this.textureSource = textureSource;
    }

    @Override
    public Texture2D generateTexture(OmniRandom random) {
        return textureSource.generateTexture(random);
    }

    @Override
    public Texture2D generateTexture(int width, int height, OmniRandom random) {
        return textureSource.generateTexture(width, height, random);
    }
}
