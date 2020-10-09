/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.client;

import com.samrj.devil.game.Game;
import com.samrj.devil.game.Mouse;
import com.samrj.devil.graphics.Camera3D;
import com.samrj.devil.graphics.Camera3DController;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;

/**
 *
 * @author angle
 */
public class Player {
    private float x, y, z;
    
    private float SPEED = 1f;
    
    private Camera3D camera;
    private Camera3DController cameraController;
    float prevMouseX;
    float prevMouseY;
    
    private class CursorCallback implements Mouse.CursorCallback {

        @Override
        public void accept(float x, float y) {
            float dx = x - prevMouseX;
            float dy = y - prevMouseY;
            cameraController.mouseDelta(dx, dy);
            prevMouseX = x; prevMouseY = y;
        }
        
    }
    
    public Player(Camera3D camera) {
        this.camera = camera;
        this.cameraController = new Camera3DController(camera);
        x = 0;
        y = 1;
        z = 2;
        Game.getMouse().setPos(100, 100);
        Game.onMouseMoved(new CursorCallback());
    }
    
    public void update(float dt) {
        if (Game.getKeyboard().isKeyDown(GLFW_KEY_W)){
                z -= SPEED * dt;
            }
            
            if (Game.getKeyboard().isKeyDown(GLFW_KEY_A)){
                x -= SPEED * dt;
            }
            
            if (Game.getKeyboard().isKeyDown(GLFW_KEY_S)){
                z += SPEED * dt;
            }
            
            if (Game.getKeyboard().isKeyDown(GLFW_KEY_D)){
                x += SPEED * dt;
            }
            
            if (Game.getKeyboard().isKeyDown(GLFW_KEY_LEFT_SHIFT)){
                y -= SPEED * dt;
            }
            
            if (Game.getKeyboard().isKeyDown(GLFW_KEY_SPACE)){
                y += SPEED * dt;
            }
            
            cameraController.target.set(x, y, z);
            cameraController.update();
    }
}
