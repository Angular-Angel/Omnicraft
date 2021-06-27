/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.graphics;

import com.samrj.devil.gl.DGL;
import com.samrj.devil.gl.VertexBuilder;
import com.samrj.devil.gl.VertexBuffer;

/**
 *
 * @author angle
 */
public class VertexBufferManager extends VertexManager {
    
    VertexBuffer buffer;

    @Override
    public VertexBuffer makeVertices() {
        buffer = DGL.genVertexBuffer(720, -1);
        return buffer;
    }

    @Override
    public void uploadVertices() {
        buffer.end();
    }

    @Override
    public VertexBuffer getVertices() {
        return buffer;
    }
}
