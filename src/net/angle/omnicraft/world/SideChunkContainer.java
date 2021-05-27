/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world;

/**
 * This interface is mostly just an artifact from when I had Octrees implemented. It can probably be merged into Chunk now.
 * @author angle
 */
public interface SideChunkContainer extends SideContainer {
    
    public int getEdgeLengthOfContainedChunks();
    
}
