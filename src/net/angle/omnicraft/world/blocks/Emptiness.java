/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world.blocks;

import net.angle.omnicraft.client.DebugClient;
import net.angle.omnicraft.world.Chunk;

/**
 *
 * @author angle
 */
public class Emptiness extends Block {

    public Emptiness() {
        super("Emptiness");
    }

    @Override
    public boolean isTransparent() {
        return true;
    }
    
    @Override
    public void bufferVertices(DebugClient client, Chunk chunk, int blockx, int blocky, int blockz) {
        
    }
    
}
