/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.graphics;

import com.samrj.devil.gl.DGL;
import com.samrj.devil.gl.VertexBuffer;

/**
 *
 * @author angle
 */
public class VertexBufferManager extends VertexManager {
    
    private VertexBuffer buffer;

    @Override
    public VertexBuffer makeVertices(int vertices, int indices) {
        buffer = DGL.genVertexBuffer(vertices, indices);
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
