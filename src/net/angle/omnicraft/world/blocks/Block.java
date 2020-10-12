/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world.blocks;

import net.angle.omnicraft.pixel.BlockTexture;

/**
 *
 * @author angle
 * @license https://gitlab.com/AngularAngel/omnicraft/-/blob/master/LICENSE
 */
public abstract class Block {
    
    public final BlockTexture texture;
    
    public Block(BlockTexture texture) {
        this.texture = texture;
    }
    
    public void draw() {
        texture.draw();
    }
    
    public void delete() {
        this.texture.delete();
    }
}
