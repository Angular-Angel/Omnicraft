/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.graphics;

import com.samrj.devil.gl.DGL;
import com.samrj.devil.gl.VertexStream;

/**
 *
 * @author angle
 */
public class VertexStreamManager extends BlockVertexManager {
    
    private VertexStream stream;
        
    public VertexStream makeVertices(int vertices, int indices) {
        stream = DGL.genVertexStream(vertices, indices);
        return stream;
    }

    @Override
    public void uploadVertices() {
        stream.upload();
    }

    @Override
    public VertexStream getVertices() {
        return stream;
    }
    
}
