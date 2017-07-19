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
    public static int MAX_LIFTIME = 25;
    static Random random = new Random();

    public Particle(float startx, float starty){
        xpos = startx;
        ypos = starty;
        x_velocity = ((float)random.nextInt(201) - 100)/10000;
        y_velocity = ((float)random.nextInt(201) - 100)/10000;
        lifetime = 0;
    }

    public Particle(float startx, float starty, int newlifetime){
        this(startx, starty);
        lifetime = newlifetime;

    }

    public void update(){
        xpos += x_velocity;
        ypos += y_velocity;
        x_velocity *= 0.95;
        y_velocity *= 0.95;
        lifetime++;
    }

    public float getX(){
        return xpos;
    }

    public float getY(){
        return ypos;
    }

    public int getMaxLifetime(){
        return MAX_LIFTIME;
    }

    public int getLifetime(){
        return lifetime;
    }

}
