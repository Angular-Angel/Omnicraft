/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world;

import com.samrj.devil.gl.DGL;
import com.samrj.devil.math.Vec2i;
import com.samrj.devil.math.Vec3;
import com.samrj.devil.math.Vec3i;
import net.angle.omnicraft.graphics.VertexManager;
import net.angle.omnicraft.world.blocks.Block;
import net.angle.omnicraft.world.blocks.Side;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

/**
 *
 * @author angle
 */
public class Chunk extends Positionable implements BlockContainer, SideContainer {
    
    public final Region region;
    
    public VertexManager vertexManager;
    
    public BlockChunk blockChunk;
    public SideChunk sideChunk;
    
    public Chunk(Region region, int x, int y, int z) {
        this(region, region.world.block_ids.get(0), region.world.side_ids.get(0), x, y, z);
    }

    public Chunk(Region region, Block block, Side side, int x, int y, int z) {
        super(x, y, z);
        this.region = region;
        
        vertexManager = new VertexManager();
        
        blockChunk = new ArrayBlockChunk(this, block, 0, 0, 0);
        sideChunk = new ArraySideChunk(this, side, 0, 0, 0);
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
            return region.getBlock(blockx + x * getEdgeLength(), blocky + y * getEdgeLength(), blockz + z * getEdgeLength());
        }
        return blockChunk.getBlock(blockx, blocky, blockz);
    }

    @Override
    public void setBlock(int blockx, int blocky, int blockz, Block block) {
        if (!containsCoordinates(blockx, blocky, blockz)) {
            region.setBlock(blockx + x * getEdgeLength(), blocky + y * getEdgeLength(), blockz + z * getEdgeLength(), block);
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
            if (!sideIsTransparent(face, x, y, z))
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
    
    public boolean blockIsEmpty(int blockx, int blocky, int blockz) {
        return blockIsEmpty(getBlock(blockx, blocky, blockz));
    }
    
    public boolean blockIsEmpty(Block block) {
        return block == null || block.id == 0;
    }
    
    public boolean sideIsEmpty(Block.BlockFace face, int voxelx, int voxely, int voxelz) {
        return sideIsEmpty(getSide(face, voxelx, voxely, voxelz));
    }
    
    public boolean sideIsEmpty(Side side) {
        return side == null || side.id == 0;
    }
    
    public boolean isEmpty(int blockx, int blocky, int blockz) {
        if (!blockIsEmpty(blockx, blocky, blockz))
            return false;
        for (Block.BlockFace face : Block.BlockFace.values()) {
            if (!sideIsEmpty(face, x, y, z))
                return false;
        }
        return true;
    }
    
    public boolean isEmpty() {
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
    
    public void streamOptimizedMesh() {
        if (vertexManager.loaded)
            return;
        if (isEmpty()) {
            vertexManager.loaded = true;
            vertexManager.drawing = false;
            return;
        }
        for (Block.BlockFace face : Block.BlockFace.values()) {
            optimizeMeshesForStream(face);
        }
        vertexManager.end();
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
    
    public void optimizeMeshesForStream(Block.BlockFace face) {
        Vec3i coord1 = face.getStartingPosition(blockChunk);
        for (int i = 0; i < getEdgeLength(); i++) {
            boolean[][] checked = new boolean[getEdgeLength()][getEdgeLength()];
            Vec3i coord2 = new Vec3i(coord1);
            for (int j = 0; j < getEdgeLength(); j++) {
                Vec3i coord3 = new Vec3i(coord2);
                for (int k = 0; k < getEdgeLength(); k++) {
                    if (checked[j][k] == false) {
                        Vec2i dimensions = greedyMeshExpansionForStream(face, coord3);
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
    
    public Vec2i greedyMeshExpansionForStream(Block.BlockFace face, Vec3i coord) {
        
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
        
        Vec2i dimensions = new Vec2i(width, height);
        
        Vec3i orientFace = face.orientFace(dimensions);
        
        float drawStartx = region.x * region.getEdgeLength() + x * getEdgeLength() + coord.x;
        float drawStarty = region.y * region.getEdgeLength() + y * getEdgeLength() + coord.y;
        float drawStartz = region.z * region.getEdgeLength() + z * getEdgeLength() + coord.z;
        
        Vec3 drawStart = face.getDrawStart(drawStartx, drawStarty, drawStartz);
        
        vertexManager.streamFlatVertices(block.id, side.id, drawStart.x, drawStart.y, drawStart.z, orientFace.x, orientFace.y, orientFace.z);
        
        return dimensions;
    }
}
