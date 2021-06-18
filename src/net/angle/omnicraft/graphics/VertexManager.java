/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.graphics;

import com.samrj.devil.gl.DGL;
import com.samrj.devil.gl.VertexBuilder;
import com.samrj.devil.gl.VertexStream;
import com.samrj.devil.math.Vec2;
import com.samrj.devil.math.Vec3;

/**
 *
 * @author angle
 */
public class VertexManager {
    
    public VertexStream stream;
    public Vec3 streamVPos;
    public Vec2 streamVTexCoord;
    public VertexBuilder.IntAttribute stream_block_palette_index;
    public VertexBuilder.IntAttribute stream_side_palette_index;
    public Vec3 streamVRandom;
    
    public void begin() {
        stream = DGL.genVertexStream(720, -1);
        
        streamVPos = stream.vec3("in_pos");
        streamVTexCoord = stream.vec2("in_tex_coord");
        stream_block_palette_index = stream.aint("in_block_palette_index");
        stream_side_palette_index = stream.aint("in_side_palette_index");
        streamVRandom = stream.vec3("in_random");
        
        stream.begin();
    }
    
    public void streamFlatVertices(int block_id, int side_id, float startx, float starty, float startz, float xoff, float yoff, float zoff) {

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
        streamVPos.set(topLeft); streamVTexCoord.set(0.0f, 0.0f); stream_block_palette_index.x = block_id; 
        stream_side_palette_index.x = side_id; streamVRandom.set(topRight); stream.vertex();
        
        streamVPos.set(topRight); streamVTexCoord.set(width, 0.0f); stream_block_palette_index.x = block_id; 
        stream_side_palette_index.x = side_id; streamVRandom.set(topRight); stream.vertex();
        
        streamVPos.set(bottomRight); streamVTexCoord.set(width, height); stream_block_palette_index.x = block_id; 
        stream_side_palette_index.x = side_id; streamVRandom.set(topRight); stream.vertex();

        //add second triangle, starting at top left corner, then bottom right, then bottom left
        streamVPos.set(topLeft); streamVTexCoord.set(0.0f, 0.0f); stream_block_palette_index.x = block_id; 
        stream_side_palette_index.x = side_id; streamVRandom.set(topRight); stream.vertex();
        
        streamVPos.set(bottomRight); streamVTexCoord.set(width, height); stream_block_palette_index.x = block_id; 
        stream_side_palette_index.x = side_id; streamVRandom.set(topRight); stream.vertex();
        
        streamVPos.set(bottomLeft); streamVTexCoord.set(0.0f, height); stream_block_palette_index.x = block_id; 
        stream_side_palette_index.x = side_id; streamVRandom.set(topRight); stream.vertex();
    }
}
