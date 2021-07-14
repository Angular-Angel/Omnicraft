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
public class TextureBufferManager extends VertexManager {
    
    private VertexBuffer buffer;
    private Vec3 bufferVPos;
    private Vec2 bufferVTexCoord;

    @Override
    public void begin() {
        buffer = DGL.genVertexBuffer(4, 6);
        
        bufferVPos = buffer.vec3("in_pos");
        bufferVTexCoord = buffer.vec2("in_tex_coord");
        
        buffer.begin();
        super.begin();
    }
    
    public void bufferVertices(Vec3 topLeft, Vec3 bottomRight) {
        Vec3 topRight = new Vec3(bottomRight.x, topLeft.y, 0),
             bottomLeft = new Vec3(topLeft.x, bottomRight.y, 0);
        
        bufferVPos.set(topLeft); bufferVTexCoord.set(0, 1); buffer.vertex();
        
        bufferVPos.set(topRight); bufferVTexCoord.set(1, 1); buffer.vertex();
        
        bufferVPos.set(bottomRight); bufferVTexCoord.set(1, 0); buffer.vertex();
        
        bufferVPos.set(bottomLeft); bufferVTexCoord.set(0, 0); buffer.vertex();
        
        buffer.index(0);
        buffer.index(1);
        buffer.index(2);
        buffer.index(0);
        buffer.index(2);
        buffer.index(3);
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
