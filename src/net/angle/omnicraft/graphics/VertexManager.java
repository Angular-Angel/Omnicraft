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
    
    public final VertexStream stream;
    public final Vec3 streamVPos;
    public final Vec2 streamVTexCoord;
    public final VertexBuilder.IntAttribute stream_block_palette_index;
    public final VertexBuilder.IntAttribute stream_side_palette_index;
    public final Vec3 streamVRandom;
    
    public VertexManager() {
        stream = DGL.genVertexStream(720, -1);
        
        streamVPos = stream.vec3("in_pos");
        streamVTexCoord = stream.vec2("in_tex_coord");
        stream_block_palette_index = stream.aint("in_block_palette_index");
        stream_side_palette_index = stream.aint("in_side_palette_index");
        streamVRandom = stream.vec3("in_random");
        
        stream.begin();
    }
}
