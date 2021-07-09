/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.client;

import com.samrj.devil.gl.DGL;
import com.samrj.devil.gl.ShaderProgram;
import com.samrj.devil.gl.Texture2D;
import com.samrj.devil.gui.DUIDrawer;
import com.samrj.devil.gui.Form;
import com.samrj.devil.math.Mat4;
import com.samrj.devil.math.Vec2;
import com.samrj.devil.math.Vec3;
import net.angle.omnicraft.graphics.TextureBufferManager;
import static org.lwjgl.opengl.GL13.GL_TEXTURE2;

/**
 *
 * @author angle
 */
public class UITexture extends Form {
    
    private Texture2D texture;
    private TextureBufferManager buffer;
    private ShaderProgram shader;
    private Mat4 textureMatrix;
    
    public UITexture(Texture2D texture, ShaderProgram shader, Mat4 textureMatrix) {
        super();
        this.texture = texture;
        this.shader = shader;
        this.buffer = new TextureBufferManager();
        this.textureMatrix = textureMatrix;
        
        buffer.begin();
        Vec2 pos = getPos();
        Vec2 size = getSize();
        buffer.bufferVertices(new Vec3(pos.x, pos.y + size.y, 0), new Vec3(pos.x + size.x, pos.y, 0));
        buffer.upload();
    }
    
    @Override
    protected void updateSize()
    {
        width = texture.getWidth();
        height = texture.getHeight();
    }

    @Override
    protected void render(DUIDrawer drawer) {
        texture.bind(GL_TEXTURE2);
        DGL.useProgram(shader);
        shader.uniform1i("u_texture", 2);
        shader.uniformMat4("u_projection_matrix", textureMatrix);
        buffer.draw();
        texture.unbind();
    }
    
    public void destroy() {
        buffer.clearVertices();
    }
    
}
