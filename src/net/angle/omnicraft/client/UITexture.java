/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.client;

import com.samrj.devil.gl.DGL;
import com.samrj.devil.gl.ShaderProgram;
import com.samrj.devil.gl.Texture2D;
import com.samrj.devil.gui.DUI;
import com.samrj.devil.gui.DUIDrawer;
import com.samrj.devil.gui.Form;
import com.samrj.devil.math.Mat3;
import com.samrj.devil.math.Vec2;
import com.samrj.devil.math.Vec3;
import net.angle.omnicraft.graphics.TextureBufferManager;
import static org.lwjgl.opengl.GL13C.GL_TEXTURE2;

/**
 *
 * @author angle
 */
public class UITexture extends Form {
    
    private final Texture2D texture;
    private final TextureBufferManager buffer;
    private final ShaderProgram shader;
    private Mat3 textureMatrix;
    
    public UITexture(Texture2D texture, ShaderProgram shader) {
        super();
        this.texture = texture;
        this.shader = shader;
        this.buffer = new TextureBufferManager();
        Vec2 viewport = DUI.viewport();
        textureMatrix = Mat3.orthographic(0, viewport.x, 0, viewport.y);
    }
    
    @Override
    protected void updateSize()
    {
        width = texture.getWidth();
        height = texture.getHeight();
        
        Vec2 viewport = DUI.viewport();
        textureMatrix = Mat3.orthographic(0, viewport.x, 0, viewport.y);
        
        buffer.clearVertices();
        
        buffer.begin();
        Vec2 pos = getPos();
        buffer.bufferVertices(new Vec3(pos.x - viewport.x/2, pos.y + height - viewport.y/2, 0), 
                              new Vec3(pos.x + width - viewport.x/2, pos.y - viewport.y/2, 0));
        buffer.upload();
    }
    
    @Override
    protected void render(DUIDrawer drawer) {
        ShaderProgram currentProgram = DGL.currentProgram();
        DGL.useProgram(shader);
        texture.bind();
        texture.bind(GL_TEXTURE2);
        shader.uniform1i("u_texture", 2);
        shader.uniformMat3("u_matrix", textureMatrix);
        buffer.draw();
        texture.unbind();
        DGL.useProgram(currentProgram);
    }
    
    public void destroy() {
        buffer.clearVertices();
    }
    
}
