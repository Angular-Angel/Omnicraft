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
public interface ChunkContainer {
    public Vec3i getChunkCoordinatesOfBlock(int blockx, int blocky, int blockz);
    public Chunk getChunk(int chunkx, int chunky, int chunkz);
    public Chunk getChunkOfBlock(int chunkx, int chunky, int chunkz);
    public void setChunk(int chunkx, int chunky, int chunkz, Chunk chunk);
    public void setChunkOfBlock(int blockx, int blocky, int blockz, Chunk chunk);
}
