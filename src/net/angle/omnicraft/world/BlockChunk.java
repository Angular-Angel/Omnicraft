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
public abstract class BlockChunk implements BlockContainer {
    
    public final ChunkContainer container;
    //These describe this chunks size, and it's x, y, and z coordinates within it's region. 
    //These coords are specific to the chunk, with no regard for the wider world.
    public final int x, y, z;
    
    public BlockChunk(ChunkContainer container, int x, int y, int z) {
        this.container = container;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    @Override
    public int getEdgeLength() {
        return container.getEdgeLengthOfContainedChunks();
    }
    
    public void bufferOptimizedMesh(DebugClient client) {
        for (BlockFace face : BlockFace.values()) {
            optimizeMeshes(client, face);
        }
    }
    
    public boolean checkMesh(Block block, BlockFace face, Vec3i coord, int width, int height) {
        
        //Goes down relative to the block face, not the chunks y coordinate
        Vec3i workingCoordy = new Vec3i(coord);
        for (int i = 0; i < height; i++) {
            
            //Goes across relative to the block face, not the chunks x coordinate
            Vec3i workingCoordx = new Vec3i(workingCoordy);
            for (int j = 0; j < width; j++) {
                if (getBlock(workingCoordx) != block || !block.faceIsVisible(face, this, workingCoordx)) {
                    return false;
                }
                face.moveAcross(workingCoordx);
            }
            face.moveDown(workingCoordy);
        }
        
        return true;
    }
    
    public void optimizeMeshes(DebugClient client, BlockFace face) {
        Vec3i coord1 = face.getStartingPosition(this);
        for (int i = 0; i < getEdgeLength(); i++) {
            boolean[][] checked = new boolean[getEdgeLength()][getEdgeLength()];
            Vec3i coord2 = new Vec3i(coord1);
            for (int j = 0; j < getEdgeLength(); j++) {
                Vec3i coord3 = new Vec3i(coord2);
                for (int k = 0; k < getEdgeLength(); k++) {
                    if (checked[j][k] == false) {
                        Vec2i dimensions = greedyMeshExpansion(client, face, coord3);
                        for (int l = 0; l < dimensions.y; l++) {
                            for (int m = 0; m < dimensions.x; m++) {
                                checked[j+l][k+m] = true;
                            }
                        }
                    }
                    face.moveAcross(coord3);
                }
                face.moveDown(coord2);
            }
            face.moveIn(coord1);
        }
    }
    
    public Vec2i greedyMeshExpansion(DebugClient client, BlockFace face, Vec3i coord) {
        
        Block block = getBlock(coord);
        
        if (block == null || block.isTransparent() || !block.faceIsVisible(face, this, coord))
            return new Vec2i(1, 1);
        
        boolean expandDown = true, expandAcross = true;
        
        int width = 1, height = 1;
        
        Vec3i workingCoord = new Vec3i(coord);
        
        while (expandAcross && face.continueAcross(workingCoord, this)) {
            face.moveAcross(workingCoord);
            if (checkMesh(block, face, workingCoord, 1, height)) {
                width++;
            } else
                expandAcross = false;
        }
        
        workingCoord = new Vec3i(coord);
        
        while (expandDown && face.continueDown(workingCoord, this)) {
            face.moveDown(workingCoord);
            if (checkMesh(block, face, workingCoord, width, 1)) {
                height++;
            } else
                expandDown = false;
        }
        
        Vec2i dimensions = new Vec2i(width, height);
        
        Vec3i orientFace = face.orientFace(dimensions);
        
        Vec3 drawStart = face.getDrawStart(x + coord.x, y + coord.y, z + coord.z);
        
        block.bufferFlatVertices(client, drawStart.x, drawStart.y, drawStart.z, orientFace.x, orientFace.y, orientFace.z);
        
        return dimensions;
    }
}
