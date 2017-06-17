package com.example.npurwosumarto.sparkleanimation;


import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by 301968 on 6/15/2017.
 */

public class MyGLRenderer implements GLSurfaceView.Renderer {

    private Triangle mTriangle;
    private Line testLine;
    private ArrayList<Particle> particles;
    private ArrayList<Line> lines;

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        mTriangle = new Triangle();
        particles = new ArrayList<>();
        lines = new ArrayList<>();
    }

    //private float[] mTranslationMatrix = new float[16];
    int counter = 0;
    float x_offset = 0;
    float y_offset = 0;
    float y_movement = 0.05f;
    int window_width = 0;
    int window_height = 0;
    float window_ratio = 0f;
    final int PARTICLE_LIFETIME = 10;
    int touch_delay = 0;

    public void onDrawFrame(GL10 unused) {
        float[] scratch = new float[16];
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        Matrix.translateM(mMVPMatrix, 0, x_offset, y_offset, 0);

//        counter++;
//        if(counter % 5 == 0) {
//            y_offset += y_movement;
//        }
//
//        if(counter == 50){
//            y_movement *= -1;
//            counter = 0;
//        }

        // Draw triangle
        //mTriangle.draw(mMVPMatrix);

//        Line test = new Line();
//        test.setVertices(0.0f,  1.0f, 0.0f, // top
//                -0.5f, -0.8f, 0.0f);
//        addLine(test);

        for(int i = 0; i < particles.size(); i++){
            for(int j = i + 1; j < particles.size(); j++){
                Particle first_p = particles.get(i);
                Particle second_p = particles.get(j);
                if(getParticleDistance(first_p, second_p) < 1.0) {
                    Line l = new Line();
                    l.setVertices(first_p.getX(), first_p.getY(), 0, second_p.getX(), second_p.getY(), 0);
                    addLine(l);
                }
            }
        }
        counter++;
        if(touch_delay > 0) {
            touch_delay--;
        }
        //update particle movement
        if(counter == 2) {
            for (int k = 0; k < particles.size(); k++) {
                Particle current = particles.get(k);
                if (current.getLifetime() >= PARTICLE_LIFETIME) {
                    removeParticle(current);
                    k--;
                } else {
                    current.update();
                }
            }
            counter = 0;
        }

//        Log.d("mtag", "Number of particles" + particles.size());
//        Log.d("mtag", "Number of lines" + lines.size());

        for(int l = 0; l < lines.size(); l++){
            lines.get(l).draw(mMVPMatrix);
        }

        lines.clear();
    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        window_width = width;
        window_height = height;

        Log.d("mtag", "width: " + width + " height: " + height);

        window_ratio = (float) width / height;


        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -window_ratio, window_ratio, -1, 1, 3, 7);
    }

    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    public float getParticleDistance(Particle first_p, Particle second_p){
        float x1 = first_p.getX();
        float y1 = first_p.getY();
        float x2 = second_p.getX();
        float y2 = second_p.getY();
        float result = (float) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        //Log.d("mtag", "distance_result: " + result);
        return result;
    }

    public void addParticle(float x, float y){
        if(touch_delay == 0) {
            float p_width = ((x / (window_width / 2)) - 1) * window_ratio;
            Log.d("mtag", "w_width: " + window_width + " w_r: " + window_ratio);
            float p_height = ((y / (window_height / 2)) - 1) * -1;
            Log.d("mtag", "p_width: " + p_width + " p_height: " + p_height);
            Particle p = new Particle(p_width, p_height);
            particles.add(p);
            touch_delay = 5;
        }
    }

    public void removeParticle(Particle p){
        particles.remove(p);
    }

    public void addLine(Line l){
        lines.add(l);
    }

}
