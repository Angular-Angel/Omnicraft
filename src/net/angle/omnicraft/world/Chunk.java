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
    
    public final Region region;
    
    public BlockChunk blockChunk;
    public SideChunk sideChunk;

    public Chunk(Region region, Block block, Side side, int x, int y, int z) {
        super(x, y, z);
        this.region = region;
        
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
            return region.getBlock(blockx + x, blocky + y, blockz + z);
        }
        return blockChunk.getBlock(blockx, blocky, blockz);
    }

    @Override
    public void setBlock(int blockx, int blocky, int blockz, Block block) {
        if (!containsCoordinates(blockx, blocky, blockz)) {
            region.setBlock(blockx + x, blocky + y, blockz + z, block);
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
        Side side = getSide(face, coord);
        
        if (block == null || block.isTransparent() || !block.faceIsVisible(face, blockChunk, coord))
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
        
        Vec3 drawStart = face.getDrawStart(x + coord.x, y + coord.y, z + coord.z);
        
        bufferFlatVertices(client, block.id, side.id, drawStart.x, drawStart.y, drawStart.z, orientFace.x, orientFace.y, orientFace.z);
        
        return dimensions;
    }
    
    public void bufferFlatVertices(DebugClient client, int block_id, int side_id, float startx, float starty, float startz, float xoff, float yoff, float zoff) {

        //Build a square out of two triangles.

        Vec3 topLeft, topRight, bottomLeft, bottomRight;
        
        int width, height;

        topLeft = new Vec3(0, 0, 0);

        if (xoff == 0) {
            topRight = new Vec3(0, 0, zoff);
            width = (int) zoff;
        } else {
            topRight = new Vec3(xoff, 0, 0);
            width = (int) xoff;
        }

        bottomRight = new Vec3(xoff, yoff, zoff);

        if (yoff == 0) {
            bottomLeft = new Vec3(0, 0, zoff);
            height = (int) zoff;
        } else{
            bottomLeft = new Vec3(0, yoff, 0);
            height = (int) yoff;
        }
        
        //adjust positions for where our starts are.
        topLeft.add(new Vec3(startx, starty, startz));
        topRight.add(new Vec3(startx, starty, startz));
        bottomLeft.add(new Vec3(startx, starty, startz));
        bottomRight.add(new Vec3(startx, starty, startz));

        //add first trangle, starting at top left corner, then top right, then bottom right
        client.vPos.set(topLeft); client.vTexCoord.set(0.0f, 0.0f); client.block_palette_index.x = block_id; 
        client.side_palette_index.x = side_id; client.vRandom.set(topRight); client.buffer.vertex();
        
        client.vPos.set(topRight); client.vTexCoord.set(width, 0.0f); client.block_palette_index.x = block_id; 
        client.side_palette_index.x = side_id; client.vRandom.set(topRight); client.buffer.vertex();
        
        client.vPos.set(bottomRight); client.vTexCoord.set(width, height); client.block_palette_index.x = block_id; 
        client.side_palette_index.x = side_id; client.vRandom.set(topRight); client.buffer.vertex();

        //add second triangle, starting at top left corner, then bottom right, then bottom left
        client.vPos.set(topLeft); client.vTexCoord.set(0.0f, 0.0f); client.block_palette_index.x = block_id; 
        client.side_palette_index.x = side_id; client.vRandom.set(topRight); client.buffer.vertex();
        
        client.vPos.set(bottomRight); client.vTexCoord.set(width, height); client.block_palette_index.x = block_id; 
        client.side_palette_index.x = side_id; client.vRandom.set(topRight); client.buffer.vertex();
        
        client.vPos.set(bottomLeft); client.vTexCoord.set(0.0f, height); client.block_palette_index.x = block_id; 
        client.side_palette_index.x = side_id; client.vRandom.set(topRight); client.buffer.vertex();
    }

    @Override
    public int getEdgeLengthOfContainedChunks() {
        return region.getEdgeLengthOfContainedChunks();
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
