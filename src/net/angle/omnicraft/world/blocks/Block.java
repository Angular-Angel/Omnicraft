/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world.blocks;

import net.angle.omnicraft.textures.BlockTexture;
import net.angle.omnicraft.world.Chunk;

/**
 *
 * @author angle
 * @license https://gitlab.com/AngularAngel/omnicraft/-/blob/master/LICENSE
 */
public abstract class Block {
    
    public final BlockTexture texture;
    public final String name;
    
    public Block(String name, BlockTexture texture) {
        this.name = name;
        this.texture = texture;
    }
    
    public void draw(Chunk chunk, int blockx, int blocky, int blockz) {
        texture.draw(chunk, blockx, blocky, blockz);
    }
    
    public void delete() {
        this.texture.delete();
    }
    
    public abstract boolean isTransparent();
}
