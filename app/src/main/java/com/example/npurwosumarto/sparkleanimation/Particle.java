package com.example.npurwosumarto.sparkleanimation;

import java.util.Random;

/**
 * Created by Katsuo on 6/16/2017.
 */

public class Particle {

    public float xpos;
    public float ypos;
    public float x_velocity;
    public float y_velocity;
    public int lifetime;

    public Particle(float startx, float starty){
        xpos = startx;
        ypos = starty;
        Random random = new Random();
        x_velocity = ((float)random.nextInt(201) - 100)/10000;
        y_velocity = ((float)random.nextInt(201) - 100)/10000;
    }

    public void update(){
        xpos += x_velocity;
        ypos += y_velocity;
        lifetime++;
    }

    public float getX(){
        return xpos;
    }

    public float getY(){
        return ypos;
    }

    public int getLifetime(){
        return lifetime;
    }

}
