/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.graphics;

import com.samrj.devil.gl.DGL;
import com.samrj.devil.gl.VertexBuffer;
import com.samrj.devil.gl.VertexBuilder;
import com.samrj.devil.math.Vec2;
import com.samrj.devil.math.Vec2i;
import com.samrj.devil.math.Vec3;
import com.samrj.devil.math.Vec3i;
import net.angle.omnicraft.world.Chunk;
import net.angle.omnicraft.world.World;
import net.angle.omnicraft.world.blocks.Block;
import net.angle.omnicraft.world.blocks.Side;
import static org.lwjgl.opengl.GL11C.GL_TRIANGLES;

/**
 *
 * @author angle
 */
public class BlockBufferManager extends VertexManager {
    
    private VertexBuffer buffer;
    
    private Vec3 bufferVPos;
    private Vec2 bufferVTexCoord;
    private VertexBuilder.IntAttribute buffer_block_palette_index;
    private VertexBuilder.IntAttribute buffer_side_palette_index;
    private Vec3 bufferVRandom;

    @Override
    public void begin() {
        begin(2880, -1);
    }
    
    public void begin(int vertices, int indices) {
        buffer = DGL.genVertexBuffer(vertices, indices);
        
        bufferVPos = buffer.vec3("in_pos");
        bufferVTexCoord = buffer.vec2("in_tex_coord");
        buffer_block_palette_index = buffer.aint("in_block_palette_index");
        buffer_side_palette_index = buffer.aint("in_side_palette_index");
        bufferVRandom = buffer.vec3("in_random");
        
        buffer.begin();
        super.begin();
    }
    
    public void bufferOptimizedMesh(Chunk chunk) {
        if (loaded)
            return;
        if (!chunk.isDrawable()) {
            loaded = true;
            drawing = false;
            return;
        }
        bufferMeshes(chunk);
        upload();
    }
    
    public void bufferMeshes(Chunk chunk) {
        for (Block.BlockFace face : Block.BlockFace.values()) {
            optimizeMeshes(face, chunk);
        }
    }
    
    public void optimizeMeshes(Block.BlockFace face, Chunk chunk) {
        Vec3i coord1 = face.getStartingPosition(chunk);
        int edgeLength = chunk.getEdgeLength();
        for (int i = 0; i < chunk.getEdgeLength(); i++) {
            boolean[][] meshed = new boolean[edgeLength][edgeLength];
            
            Vec3i coord2 = new Vec3i(coord1);
            
            for (int j = 0; j < edgeLength; j++) {
                Vec3i coord3 = new Vec3i(coord2);
                
                for (int k = 0; k < edgeLength; k++) {
                    if (meshed[j][k] == false) {
                        Vec2i dimensions = greedyMeshExpansion(face, chunk, coord3, meshed, k, j);
                        
                        for (int l = 0; l < dimensions.y; l++) {
                            for (int m = 0; m < dimensions.x; m++) {
                                meshed[j+l][k+m] = true;
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
    
    public Vec2i greedyMeshExpansion(Block.BlockFace face, Chunk chunk, Vec3i coord, boolean[][] meshed, int meshx, int meshy) {
        
        Block block = chunk.getBlock(coord);
        Side side = chunk.getSide(face, coord);
        
        if (chunk.blockIsTransparent(block) || !block.faceIsVisible(face, chunk, coord))
            return new Vec2i(1, 1);
        
        boolean expandDown = true, expandAcross = true;
        
        int width = 1, height = 1;
        
        Vec3i workingCoord = new Vec3i(coord);
        
        while (expandAcross && face.continueAcross(workingCoord, chunk)) {
            face.moveAcross(workingCoord);
            if (!meshed[meshy + height - 1][meshx + width] && checkMesh(chunk, block, side, face, workingCoord, 1, height)) {
                width++;
            } else
                expandAcross = false;
        }
        
        workingCoord = new Vec3i(coord);
        
        while (expandDown && face.continueDown(workingCoord, chunk)) {
            face.moveDown(workingCoord);
            if (!meshed[meshy + height][meshx + width - 1] && checkMesh(chunk, block, side, face, workingCoord, width, 1)) {
                height++;
            } else
                expandDown = false;
        }
        
        float drawStartx = chunk.getXVoxelOffset() + coord.x;
        float drawStarty = chunk.getYVoxelOffset() + coord.y;
        float drawStartz = chunk.getZVoxelOffset() + coord.z;
        
        Vec2 dimensions = new Vec2(width, height);
        
        bufferFace(block, side, face, dimensions, new Vec3(drawStartx, drawStarty, drawStartz));
        
        return new Vec2i(width, height);
    }
    
    public boolean checkMesh(Chunk chunk, Block block, Side side, Block.BlockFace face, Vec3i coord, int width, int height) {
        //Goes down relative to the block face, not the chunks y coordinate
        Vec3i workingCoordy = new Vec3i(coord);
        for (int i = 0; i < height; i++) {
            
            //Goes across relative to the block face, not the chunks x coordinate
            Vec3i workingCoordx = new Vec3i(workingCoordy);
            for (int j = 0; j < width; j++) {
                if (chunk.getBlock(workingCoordx) != block || !block.faceIsVisible(face, chunk, workingCoordx) || chunk.getSide(face, workingCoordx) != side) {
                    return false;
                }
                face.moveAcross(workingCoordx);
            }
            face.moveDown(workingCoordy);
        }
        
        return true;
    }
    
    public void bufferFlatVertices(int block_id, int side_id, float startx, float starty, float startz, float xoff, float yoff, float zoff) {
        if (!loaded) begin();

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
        
        topLeft.mult(World.EDGE_LENGTH_OF_BLOCK);
        topRight.mult(World.EDGE_LENGTH_OF_BLOCK);
        bottomLeft.mult(World.EDGE_LENGTH_OF_BLOCK);
        bottomRight.mult(World.EDGE_LENGTH_OF_BLOCK);

        //add first trangle, starting at top left corner, then top right, then bottom right
        bufferVPos.set(topLeft); bufferVTexCoord.set(0.0f, 0.0f); buffer_block_palette_index.x = block_id; 
        buffer_side_palette_index.x = side_id; bufferVRandom.set(topRight); buffer.vertex();
        
        bufferVPos.set(topRight); bufferVTexCoord.set(width, 0.0f); buffer_block_palette_index.x = block_id; 
        buffer_side_palette_index.x = side_id; bufferVRandom.set(topRight); buffer.vertex();
        
        bufferVPos.set(bottomRight); bufferVTexCoord.set(width, height); buffer_block_palette_index.x = block_id; 
        buffer_side_palette_index.x = side_id; bufferVRandom.set(topRight); buffer.vertex();

        //add second triangle, starting at top left corner, then bottom right, then bottom left
        bufferVPos.set(topLeft); bufferVTexCoord.set(0.0f, 0.0f); buffer_block_palette_index.x = block_id; 
        buffer_side_palette_index.x = side_id; bufferVRandom.set(topRight); buffer.vertex();
        
        bufferVPos.set(bottomRight); bufferVTexCoord.set(width, height); buffer_block_palette_index.x = block_id; 
        buffer_side_palette_index.x = side_id; bufferVRandom.set(topRight); buffer.vertex();
        
        bufferVPos.set(bottomLeft); bufferVTexCoord.set(0.0f, height); buffer_block_palette_index.x = block_id; 
        buffer_side_palette_index.x = side_id; bufferVRandom.set(topRight); buffer.vertex();
    }
    
    public void bufferFace(Block block, Side side, Block.BlockFace face, Vec2 dimensions, Vec3 drawStart) {
        Vec3 orientFace = face.orientFace(dimensions);
        
        drawStart = face.getDrawStart(drawStart);
        
        bufferFlatVertices(block.id, side.id, drawStart.x, drawStart.y, drawStart.z, orientFace.x, orientFace.y, orientFace.z);
    }

    @Override
    protected void uploadVertices() {
        buffer.end();
    }

    @Override
    protected void drawVertices() {
        DGL.draw(buffer, GL_TRIANGLES);
    }

    @Override
    protected void deleteVertices() {
        DGL.delete(buffer);
    }
}
