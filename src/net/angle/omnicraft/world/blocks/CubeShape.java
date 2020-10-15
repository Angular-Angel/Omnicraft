/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world.blocks;

import net.angle.omnicraft.textures.BlockTexture;
import net.angle.omnicraft.textures.CubeTexture;
import net.angle.omnicraft.textures.pixels.TextureSource;
import net.angle.omnicraft.random.OmniRandom;

/**
 *
 * @author angle
 */
public class CubeShape implements BlockShape {
    
    @Override
    public BlockTexture generateBlockTexture(TextureSource textureSource, OmniRandom random) {
       return new CubeTexture(textureSource.generateTexture(random), textureSource.generateTexture(random),
                              textureSource.generateTexture(random), textureSource.generateTexture(random), 
                              textureSource.generateTexture(random), textureSource.generateTexture(random));
    }
}
