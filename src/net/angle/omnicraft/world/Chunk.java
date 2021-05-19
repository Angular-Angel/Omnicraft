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
import net.angle.omnicraft.world.blocks.Side;

/**
 *
 * @author angle
 */
public class Chunk extends Positionable implements BlockChunkContainer, SideChunkContainer {
    
    public final ChunkContainer container;
    
    public BlockChunk blockChunk;
    public SideChunk sideChunk;

    public Chunk(ChunkContainer container, Block block, Side side, int x, int y, int z) {
        super(x, y, z);
        this.container = container;
        
        blockChunk = new ArrayBlockChunk(this, block, 0, 0, 0);
        sideChunk = new ArraySideChunk(this, side, 0, 0, 0);
    }

    @Override
    public int getEdgeLength() {
        return container.getEdgeLengthOfContainedChunks();
    }
    
    public void setBlockChunk(BlockChunk blockChunk) {
        this.blockChunk = blockChunk;
    }

    @Override
    public Block getBlock(int blockx, int blocky, int blockz) {
        if (!containsCoordinates(blockx, blocky, blockz)) {
            return container.getBlock(blockx + x, blocky + y, blockz + z);
        }
        return blockChunk.getBlock(blockx, blocky, blockz);
    }

    @Override
    public void setBlock(int blockx, int blocky, int blockz, Block block) {
        if (!containsCoordinates(blockx, blocky, blockz)) {
            container.setBlock(blockx + x, blocky + y, blockz + z, block);
        }
        blockChunk.setBlock(blockx, blocky, blockz, block);
    }

    @Override
    public Side getSide(Block.BlockFace face, int blockx, int blocky, int blockz) {
        return sideChunk.getSide(face, blockx, blocky, blockz);
    }

    @Override
    public void setSide(Block.BlockFace face, int blockx, int blocky, int blockz, Side side) {
        sideChunk.setSide(face, blockx, blocky, blockz, side);
    }
    
    public void bufferOptimizedMesh(DebugClient client) {
        for (Block.BlockFace face : Block.BlockFace.values()) {
            optimizeMeshes(client, face);
        }
    }
    
    public boolean checkMesh(Block block, Block.BlockFace face, Vec3i coord, int width, int height) {
        
        //Goes down relative to the block face, not the chunks y coordinate
        Vec3i workingCoordy = new Vec3i(coord);
        for (int i = 0; i < height; i++) {
            
            //Goes across relative to the block face, not the chunks x coordinate
            Vec3i workingCoordx = new Vec3i(workingCoordy);
            for (int j = 0; j < width; j++) {
                if (getBlock(workingCoordx) != block || !block.faceIsVisible(face, blockChunk, workingCoordx)) {
                    return false;
                }
                face.moveAcross(workingCoordx);
            }
            face.moveDown(workingCoordy);
        }
        
        return true;
    }
    
    public void optimizeMeshes(DebugClient client, Block.BlockFace face) {
        Vec3i coord1 = face.getStartingPosition(blockChunk);
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
    
    public Vec2i greedyMeshExpansion(DebugClient client, Block.BlockFace face, Vec3i coord) {
        
        Block block = getBlock(coord);
        
        if (block == null || block.isTransparent() || !block.faceIsVisible(face, blockChunk, coord))
            return new Vec2i(1, 1);
        
        boolean expandDown = true, expandAcross = true;
        
        int width = 1, height = 1;
        
        Vec3i workingCoord = new Vec3i(coord);
        
        while (expandAcross && face.continueAcross(workingCoord, blockChunk)) {
            face.moveAcross(workingCoord);
            if (checkMesh(block, face, workingCoord, 1, height)) {
                width++;
            } else
                expandAcross = false;
        }
        
        workingCoord = new Vec3i(coord);
        
        while (expandDown && face.continueDown(workingCoord, blockChunk)) {
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

    @Override
    public int getEdgeLengthOfContainedChunks() {
        return container.getEdgeLengthOfContainedChunks();
    }

    @Override
    public Vec3i getChunkCoordinatesOfBlock(int blockx, int blocky, int blockz) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BlockChunk getChunk(int chunkx, int chunky, int chunkz) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BlockChunk getChunkOfBlock(int chunkx, int chunky, int chunkz) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setChunkOfBlock(int blockx, int blocky, int blockz, BlockChunk chunk) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
