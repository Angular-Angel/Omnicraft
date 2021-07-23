/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world;

import com.samrj.devil.math.Vec3i;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import net.angle.omnicraft.graphics.BlockBufferManager;
import net.angle.omnicraft.world.blocks.Block;
import net.angle.omnicraft.world.blocks.Side;

/**
 *
 * @author angle
 */
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class Chunk extends VoxelPositionable implements BlockContainer, SideContainer {
    
    public final World world;
    
    public BlockBufferManager vertexManager;
    
    public BlockChunk blockChunk;
    public SideChunk sideChunk;
    
    public Chunk(World world, int x, int y, int z) {
        this(world, world.block_ids.get(0), world.side_ids.get(0), x, y, z);
    }

    public Chunk(World world, Block block, Side side, int x, int y, int z) {
        super(x, y, z);
        this.world = world;
        
        vertexManager = new BlockBufferManager();
        
        blockChunk = new ArrayBlockChunk(this, block);
        sideChunk = new ArraySideChunk(this, side);
    }

    @Override
    public int getEdgeLength() {
        return World.BLOCK_EDGE_LENGTH_OF_CHUNK;
    }
    
    public void setBlockChunk(BlockChunk blockChunk) {
        this.blockChunk = blockChunk;
    }

    @Override
    public Block getBlock(int blockx, int blocky, int blockz) {
        if (!containsCoordinates(blockx, blocky, blockz)) {
            return world.getBlock(blockx + getXVoxelOffset(), blocky + getYVoxelOffset(), blockz + getZVoxelOffset());
        }
        return blockChunk.getBlock(blockx, blocky, blockz);
    }

    @Override
    public void setBlock(int blockx, int blocky, int blockz, Block block) {
        if (!containsCoordinates(blockx, blocky, blockz)) {
            world.setBlock(blockx + getXVoxelOffset(), blocky + getYVoxelOffset(), blockz + getZVoxelOffset(), block);
        }
        blockChunk.setBlock(blockx, blocky, blockz, block);
    }

    @Override
    public Side getSide(Block.BlockFace face, int sidex, int sidey, int sidez) {
        if (!containsCoordinates(sidex, sidey, sidez)) {
            return world.getSide(face, sidex + getXVoxelOffset(), sidey + getYVoxelOffset(), sidez + getZVoxelOffset());
        }
        return sideChunk.getSide(face, sidex, sidey, sidez);
    }

    @Override
    public void setSide(Block.BlockFace face, int sidex, int sidey, int sidez, Side side) {
        if (!containsCoordinates(sidex, sidey, sidez)) {
            world.setSide(face, sidex + getXVoxelOffset(), sidey + getYVoxelOffset(), sidez + getZVoxelOffset(), side);
        }
        sideChunk.setSide(face, sidex, sidey, sidez, side);
    }
    
    public void setAllSidesOnSurface(Block.BlockFace face, Side side) {
        Vec3i startingPosition = face.getStartingPosition(this);
        
        for (int i = 0; i < getEdgeLength(); i++) {
            Vec3i coord2 = new Vec3i(startingPosition);
            for (int j = 0; j < getEdgeLength(); j++) {
                Vec3i coord3 = new Vec3i(coord2);
                for (int k = 0; k < getEdgeLength(); k++) {
                    if (getBlock(coord3).isDrawable()) {
                        setSide(face, coord3, side);
                        break;
                    } else
                        face.moveIn(coord3);
                }
                face.moveAcross(coord2);
            }
            face.moveDown(startingPosition);
        }
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
