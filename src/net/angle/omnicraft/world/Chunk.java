/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world;

import com.samrj.devil.math.Vec2;
import com.samrj.devil.math.Vec2i;
import com.samrj.devil.math.Vec3;
import com.samrj.devil.math.Vec3i;
import net.angle.omnicraft.graphics.BlockBufferManager;
import net.angle.omnicraft.world.blocks.Block;
import net.angle.omnicraft.world.blocks.Side;

/**
 *
 * @author angle
 */
public class Chunk extends VoxelPositionable implements BlockContainer, SideContainer {
    
    public final Region region;
    
    public BlockBufferManager vertexManager;
    
    public BlockChunk blockChunk;
    public SideChunk sideChunk;
    
    public Chunk(Region region, int x, int y, int z) {
        this(region, region.world.block_ids.get(0), region.world.side_ids.get(0), x, y, z);
    }

    public Chunk(Region region, Block block, Side side, int x, int y, int z) {
        super(x, y, z);
        this.region = region;
        
        vertexManager = new BlockBufferManager();
        
        blockChunk = new ArrayBlockChunk(this, block);
        sideChunk = new ArraySideChunk(this, side);
    }

    @Override
    public int getEdgeLength() {
        return region.getEdgeLengthOfContainedChunks();
    }
    
    public void setBlockChunk(BlockChunk blockChunk) {
        this.blockChunk = blockChunk;
    }

    @Override
    public Block getBlock(int blockx, int blocky, int blockz) {
        if (!containsCoordinates(blockx, blocky, blockz)) {
            return region.getBlock(blockx + getXVoxelOffset(), blocky + getYVoxelOffset(), blockz + getZVoxelOffset());
        }
        return blockChunk.getBlock(blockx, blocky, blockz);
    }

    @Override
    public void setBlock(int blockx, int blocky, int blockz, Block block) {
        if (!containsCoordinates(blockx, blocky, blockz)) {
            region.setBlock(blockx + getXVoxelOffset(), blocky + getYVoxelOffset(), blockz + getZVoxelOffset(), block);
        }
        blockChunk.setBlock(blockx, blocky, blockz, block);
    }

    @Override
    public Side getSide(Block.BlockFace face, int sidex, int sidey, int sidez) {
        if (!containsCoordinates(sidex, sidey, sidez)) {
            return region.getSide(face, sidex + getXVoxelOffset(), sidey + getYVoxelOffset(), sidez + getZVoxelOffset());
        }
        return sideChunk.getSide(face, sidex, sidey, sidez);
    }

    @Override
    public void setSide(Block.BlockFace face, int blockx, int blocky, int blockz, Side side) {
        sideChunk.setSide(face, blockx, blocky, blockz, side);
    }
    
    public boolean blockIsTransparent(int blockx, int blocky, int blockz) {
        return blockIsTransparent(getBlock(blockx, blocky, blockz));
    }
    
    public boolean blockIsTransparent(Block block) {
        return block == null || block.isTransparent();
    }
    
    public boolean sideIsTransparent(Block.BlockFace face, int voxelx, int voxely, int voxelz) {
        return sideIsTransparent(getSide(face, voxelx, voxely, voxelz));
    }
    
    public boolean sideIsTransparent(Side side) {
        return side == null || side.isTransparent();
    }
    
    public boolean isTransparent(int blockx, int blocky, int blockz) {
        if (!blockIsTransparent(blockx, blocky, blockz))
            return false;
        for (Block.BlockFace face : Block.BlockFace.values()) {
            if (!sideIsTransparent(face, getX(), getY(), getZ()))
                return false;
        }
        return true;
    }
    
    public boolean isTransparent() {
        for (int i = 0; i < getEdgeLength(); i++) {
            for (int j = 0; j < getEdgeLength(); j++) {
                for (int k = 0; k < getEdgeLength(); k++) {
                    if (!isTransparent(i, j, k))
                        return false;
                }
            }
        }
        return true;
    }
    
    public boolean blockIsDrawable(int blockx, int blocky, int blockz) {
        return blockIsDrawable(getBlock(blockx, blocky, blockz));
    }
    
    public boolean blockIsDrawable(Block block) {
        return block != null && block.isDrawable();
    }
    
    public boolean sideIsDrawable(Block.BlockFace face, int voxelx, int voxely, int voxelz) {
        return sideIsDrawable(getSide(face, voxelx, voxely, voxelz));
    }
    
    public boolean sideIsDrawable(Side side) {
        return side != null && side.isDrawable();
    }
    
    public boolean isDrawable(int blockx, int blocky, int blockz) {
        if (blockIsDrawable(blockx, blocky, blockz))
            return true;
        for (Block.BlockFace face : Block.BlockFace.values()) {
            if (sideIsDrawable(face, getX(), getY(), getZ()))
                return true;
        }
        return false;
    }
    
    public boolean isDrawable() {
        for (int i = 0; i < getEdgeLength(); i++) {
            for (int j = 0; j < getEdgeLength(); j++) {
                for (int k = 0; k < getEdgeLength(); k++) {
                    if (isDrawable(i, j, k))
                        return true;
                }
            }
        }
        return false;
    }
    
    public void buffer() {
        vertexManager.bufferOptimizedMesh(this);
    }
    
    public void bufferMeshes() {
        for (Block.BlockFace face : Block.BlockFace.values()) {
            optimizeMeshes(face);
        }
    }
    
    public boolean checkMesh(Block block, Side side, Block.BlockFace face, Vec3i coord, int width, int height) {
        //Goes down relative to the block face, not the chunks y coordinate
        Vec3i workingCoordy = new Vec3i(coord);
        for (int i = 0; i < height; i++) {
            
            //Goes across relative to the block face, not the chunks x coordinate
            Vec3i workingCoordx = new Vec3i(workingCoordy);
            for (int j = 0; j < width; j++) {
                if (getBlock(workingCoordx) != block || !block.faceIsVisible(face, blockChunk, workingCoordx) || getSide(face, workingCoordx) != side) {
                    return false;
                }
                face.moveAcross(workingCoordx);
            }
            face.moveDown(workingCoordy);
        }
        
        return true;
    }
    
    public void optimizeMeshes(Block.BlockFace face) {
        Vec3i coord1 = face.getStartingPosition(blockChunk);
        for (int i = 0; i < getEdgeLength(); i++) {
            boolean[][] checked = new boolean[getEdgeLength()][getEdgeLength()];
            Vec3i coord2 = new Vec3i(coord1);
            for (int j = 0; j < getEdgeLength(); j++) {
                Vec3i coord3 = new Vec3i(coord2);
                for (int k = 0; k < getEdgeLength(); k++) {
                    if (checked[j][k] == false) {
                        Vec2i dimensions = greedyMeshExpansion(face, coord3);
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
    
    public Vec2i greedyMeshExpansion(Block.BlockFace face, Vec3i coord) {
        
        Block block = getBlock(coord);
        Side side = getSide(face, coord);
        
        if (blockIsTransparent(block) || !block.faceIsVisible(face, blockChunk, coord))
            return new Vec2i(1, 1);
        
        boolean expandDown = true, expandAcross = true;
        
        int width = 1, height = 1;
        
        Vec3i workingCoord = new Vec3i(coord);
        
        while (expandAcross && face.continueAcross(workingCoord, blockChunk)) {
            face.moveAcross(workingCoord);
            if (checkMesh(block, side, face, workingCoord, 1, height)) {
                width++;
            } else
                expandAcross = false;
        }
        
        workingCoord = new Vec3i(coord);
        
        while (expandDown && face.continueDown(workingCoord, blockChunk)) {
            face.moveDown(workingCoord);
            if (checkMesh(block, side, face, workingCoord, width, 1)) {
                height++;
            } else
                expandDown = false;
        }
        
        float drawStartx = region.getXVoxelOffset() + getXVoxelOffset() + coord.x;
        float drawStarty = region.getYVoxelOffset() + getYVoxelOffset() + coord.y;
        float drawStartz = region.getZVoxelOffset() + getZVoxelOffset() + coord.z;
        
        Vec2 dimensions = new Vec2(width, height);
        
        vertexManager.BufferFace(block, side, face, dimensions, new Vec3(drawStartx, drawStarty, drawStartz));
        
        return new Vec2i(width, height);
    }
    
    public int axialDist(Chunk other) {
        if (other.region != this.region) {
            int xDist = Math.abs((other.getX() + other.region.getXChunkOffset()) 
                    - (getX() + region.getXChunkOffset()));
            int yDist = Math.abs((other.getY() + other.region.getYChunkOffset()) 
                    - (getY() + region.getYChunkOffset()));
            int zDist = Math.abs((other.getZ() + other.region.getZChunkOffset()) 
                    - (getZ() + region.getZChunkOffset()));
            return Math.max(xDist, Math.max(yDist, zDist));
        }
        else return super.axialDist(other);
    }

    @Override
    public int getXVoxelOffset() {
        return getX() * getEdgeLength();
    }

    @Override
    public int getYVoxelOffset() {
        return getY() * getEdgeLength();
    }

    @Override
    public int getZVoxelOffset() {
        return getZ() * getEdgeLength();
    }
}
