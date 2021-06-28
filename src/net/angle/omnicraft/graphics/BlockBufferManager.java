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
import com.samrj.devil.math.Vec3;
import net.angle.omnicraft.world.Chunk;
import net.angle.omnicraft.world.World;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

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
        begin(720, -1);
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
        chunk.bufferMeshes();
        upload();
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
