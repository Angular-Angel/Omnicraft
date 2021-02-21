/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world;

import com.samrj.devil.math.Vec2i;
import com.samrj.devil.math.Vec3;
import com.samrj.devil.math.Vec3i;
import net.angle.omnicraft.client.DebugClient;
import net.angle.omnicraft.world.blocks.Block;
import net.angle.omnicraft.world.blocks.Block.BlockFace;

/**
 *
 * @author angle
 */
public abstract class Chunk implements BlockContainer {
    
    public final ChunkContainer container;
    //These describe this chunks size, and it's x, y, and z coordinates within it's region. 
    //These coords are specific to the chunk, with no regard for the wider world.
    public final int x, y, z;
    
    public Chunk(ChunkContainer container, int x, int y, int z) {
        this.container = container;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    @Override
    public int getEdgeLength() {
        return container.getEdgeLengthOfContainedChunks();
    }
    
    public void bufferBlocks(DebugClient client) {
        for (int blockx = 0; blockx < getEdgeLength(); blockx++) {
            for (int blocky = 0; blocky < getEdgeLength(); blocky++) {
                for (int blockz = 0; blockz < getEdgeLength(); blockz++) {
                    getBlock(blockx, blocky, blockz).bufferVertices(client, this, blockx, blocky, blockz);
                }
            }
        }
    }
    
    public void bufferOptimizedMesh(DebugClient client) {
        greedyMeshExpansion(client, BlockFace.front, new Vec3i());
    }
    
    public boolean checkMesh(Block block, BlockFace face, Vec3i coord, int width, int height) {
        
        //Goes down relative to the block face, not the chunks y coordinate
        Vec3i workingCoordy = new Vec3i(coord);
        for (int i = 0; i < width; i++) {
            
            //Goes across relative to the block face, not the chunks x coordinate
            Vec3i workingCoordx = new Vec3i(workingCoordy);
            for (int j = 0; j < height; j++) {
                if (getBlock(workingCoordx) != block || !block.faceIsVisible(face, this, workingCoordx))
                    return false;
                face.moveAcross(workingCoordx);
            }
            face.moveDown(workingCoordy);
        }
        
        return true;
    }
    
    public void greedyMeshExpansion(DebugClient client, BlockFace face, Vec3i coord) {
        
        Block block = getBlock(coord);
        
        boolean expandDown = true, expandAcross = true;
        
        int width = 1, height = 1;
        
        Vec3i workingCoordx = new Vec3i(coord);
        
        while (expandAcross) {
            face.moveAcross(workingCoordx);
            if (checkMesh(block, face, workingCoordx, 1, height)) 
                width++;
            else
                expandAcross = false;
        }
        
        Vec3i workingCoordy = new Vec3i(coord);
        
        while (expandDown) {
            face.moveAcross(workingCoordy);
            if (checkMesh(block, face, workingCoordy, width, 1)) 
                height++;
            else
                expandDown = false;
        }
        
        Vec3i orientFace = face.orientFace(new Vec2i(width, height));
        
        block.bufferFlatVertices(client, x, y, z, orientFace.x, orientFace.y, orientFace.z);
    }
}
