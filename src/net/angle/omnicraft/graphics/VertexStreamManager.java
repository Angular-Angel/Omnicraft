/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.graphics;

import com.samrj.devil.gl.DGL;
import com.samrj.devil.gl.VertexBuilder;
import com.samrj.devil.gl.VertexStream;

/**
 *
 * @author angle
 */
public class VertexStreamManager extends VertexManager {
    
    VertexStream stream;

    @Override
    public VertexStream makeVertices() {
        stream = DGL.genVertexStream(720, -1);
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
