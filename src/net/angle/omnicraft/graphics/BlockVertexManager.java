/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.graphics;

import com.samrj.devil.gl.DGL;
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
public abstract class BlockVertexManager {
    
    
    public Vec3 streamVPos;
    public Vec2 streamVTexCoord;
    public VertexBuilder.IntAttribute stream_block_palette_index;
    public VertexBuilder.IntAttribute stream_side_palette_index;
    public Vec3 streamVRandom;
    
    public boolean loaded;
    public boolean drawing;

    public VertexBuilder makeVertices() {
        return makeVertices(720, -1);
    }
    
    public abstract VertexBuilder makeVertices(int vertices, int indices);
    public abstract void uploadVertices();
    
    public abstract VertexBuilder getVertices();
    
    public void begin() {
        VertexBuilder vertices = makeVertices();
        
        streamVPos = vertices.vec3("in_pos");
        streamVTexCoord = vertices.vec2("in_tex_coord");
        stream_block_palette_index = vertices.aint("in_block_palette_index");
        stream_side_palette_index = vertices.aint("in_side_palette_index");
        streamVRandom = vertices.vec3("in_random");
        
        vertices.begin();
        loaded = true;
        drawing = true;
    }
    
    public void upload() {
        if (loaded && drawing)
            uploadVertices();
    }
    
    public void streamOptimizedMesh(Chunk chunk) {
        if (loaded)
            return;
        if (!chunk.isDrawable()) {
            loaded = true;
            drawing = false;
            return;
        }
        chunk.streamMeshes();
        upload();
    }
    
    public void streamFlatVertices(int block_id, int side_id, float startx, float starty, float startz, float xoff, float yoff, float zoff) {
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
        
        VertexBuilder vertices = getVertices();

        //add first trangle, starting at top left corner, then top right, then bottom right
        streamVPos.set(topLeft); streamVTexCoord.set(0.0f, 0.0f); stream_block_palette_index.x = block_id; 
        stream_side_palette_index.x = side_id; streamVRandom.set(topRight); vertices.vertex();
        
        streamVPos.set(topRight); streamVTexCoord.set(width, 0.0f); stream_block_palette_index.x = block_id; 
        stream_side_palette_index.x = side_id; streamVRandom.set(topRight); vertices.vertex();
        
        streamVPos.set(bottomRight); streamVTexCoord.set(width, height); stream_block_palette_index.x = block_id; 
        stream_side_palette_index.x = side_id; streamVRandom.set(topRight); vertices.vertex();

        //add second triangle, starting at top left corner, then bottom right, then bottom left
        streamVPos.set(topLeft); streamVTexCoord.set(0.0f, 0.0f); stream_block_palette_index.x = block_id; 
        stream_side_palette_index.x = side_id; streamVRandom.set(topRight); vertices.vertex();
        
        streamVPos.set(bottomRight); streamVTexCoord.set(width, height); stream_block_palette_index.x = block_id; 
        stream_side_palette_index.x = side_id; streamVRandom.set(topRight); vertices.vertex();
        
        streamVPos.set(bottomLeft); streamVTexCoord.set(0.0f, height); stream_block_palette_index.x = block_id; 
        stream_side_palette_index.x = side_id; streamVRandom.set(topRight); vertices.vertex();
    }
    
    public void clearStream() {
        if (loaded)
            DGL.delete(getVertices());
        loaded = false;
    }
    
    public void draw() {
        if (loaded && drawing)
            DGL.draw(getVertices(), GL_TRIANGLES);
    }
    
}
