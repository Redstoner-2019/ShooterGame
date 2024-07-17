package me.redstoner2019.player;

import me.redstoner2019.graphics.animation.AnimationProvider;
import me.redstoner2019.graphics.render.Renderer;
import me.redstoner2019.graphics.texture.TextureProvider;

import java.io.Serializable;

public class Player implements Serializable {
    private float x;
    private float y;
    private float vx;
    private float vy;
    private String name;

    public Player(String name, float vy, float vx, float y, float x) {
        this.name = name;
        this.vy = vy;
        this.vx = vx;
        this.y = y;
        this.x = x;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getVx() {
        return vx;
    }

    public void setVx(float vx) {
        this.vx = vx;
    }

    public float getVy() {
        return vy;
    }

    public void setVy(float vy) {
        this.vy = vy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void render(Renderer renderer){
        TextureProvider provider = TextureProvider.getInstance();
        AnimationProvider animationProvider = AnimationProvider.getInstance();

        if(vx == 0 && vy == 0){
            animationProvider.getAnimation("idle").render(renderer,x,y,0.075f,0.2f);
        } else {
            if(vx >= 0) animationProvider.getAnimation("run").render(renderer,x - (0.075f / 2),y,0.075f,0.2f);
            else animationProvider.getAnimation("run").render(renderer,x - (-0.075f / 2),y,-0.075f,0.2f);
        }
    }

    public void update(float deltaTime){
        vy-=0.0981f*deltaTime;

        if(y + getVy() * deltaTime < -1){
            y = -1;
            vy = 0;
        }

        if(vy == 0){
            vx*=0.995f;
        }
        if(Math.abs(vx) <= 0.08f) vx = 0;

        x+=getVx() * deltaTime * 0.01f;
        y+=getVy() * deltaTime * 0.01f;

        if(y < -1) y = -1;
    }
}
