/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world;

import com.samrj.devil.math.Vec3i;

/**
 *
 * @author angle
 */
public interface BlockChunkContainer extends BlockContainer {

    /**
     * returns the number of blocks in an edge (length, width, or height) for any of the contained chunks.
     * @return
     */
    public Vec3i getCoordinates();
    
    public int getEdgeLengthOfContainedChunks();
    public Vec3i getChunkCoordinatesOfBlock(int blockx, int blocky, int blockz);
    
    public BlockChunk getChunk(int chunkx, int chunky, int chunkz);
    
    public BlockChunk getChunkOfBlock(int chunkx, int chunky, int chunkz);
    public void setChunkOfBlock(int blockx, int blocky, int blockz, BlockChunk chunk);
}
