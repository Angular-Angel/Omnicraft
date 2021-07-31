/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.graphics;

import com.samrj.devil.gl.DGL;
import com.samrj.devil.gl.VertexBuffer;
import com.samrj.devil.math.Vec2;
import com.samrj.devil.math.Vec3;
import static org.lwjgl.opengl.GL11C.GL_TRIANGLES;

/**
 *
 * @author angle
 */
public class VertexBufferManager extends VertexManager {
    
    protected VertexBuffer buffer;
    
    protected Vec3 bufferVPos;
    protected Vec2 bufferVTexCoord;

    protected void beginVariables(int vertices, int indices) {
        buffer = DGL.genVertexBuffer(vertices, indices);
        
        bufferVPos = buffer.vec3("in_pos");
        bufferVTexCoord = buffer.vec2("in_tex_coord");
    }
    
    public void begin(int vertices, int indices) {
        beginVariables(vertices, indices);
        
        buffer.begin();
        super.begin();
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
