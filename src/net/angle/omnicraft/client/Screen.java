/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.client;

/**
 *
 * @author angle
 */
public abstract class Screen {
    protected final DebugClient client;
    
    public Screen(DebugClient client) {
        this.client = client;
    }
    
    public abstract void mouseMoved(float x, float y);
    
    public abstract void key(int key, int action, int mods);
    
    public abstract void resize(int width, int height);
    
    public abstract void step(float dt);
    
    public abstract void render();
    
    public abstract void destroy(Boolean crashed);
}
