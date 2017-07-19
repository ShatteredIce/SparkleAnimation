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
    private ArrayList<Particle> allparticles;
    private ArrayList<ArrayList<Particle>> newparticles_list;
    private ArrayList<Triangle> triangles;
    private ArrayList<Line> lines;

    boolean useLines = true;

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        mTriangle = new Triangle();
        allparticles = new ArrayList<>();
        newparticles_list = new ArrayList<>();
        triangles = new ArrayList<>();
        lines = new ArrayList<>();
//        Line test = new Line();
//        test.p1 = new Particle(0.0f, 0.0f); // top
//        test.p2 = new Particle(0.0f, 0.0f);
//        addLine(test);
    }

    //private float[] mTranslationMatrix = new float[16];
    int counter = 0;
    float x_offset = 0;
    float y_offset = 0;
    float y_movement = 0.05f;
    int window_width = 0;
    int window_height = 0;
    float window_ratio = 0f;
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


        counter++;
        if(touch_delay > 0) {
            touch_delay--;
        }

        if(useLines) {
            //add new lines
            if (newparticles_list.size() != 0) {
                for (int l = 0; l < newparticles_list.size(); l++) {
                    ArrayList<Particle> current = newparticles_list.get(l);
                    for (int i = 0; i < current.size(); i++) {
                        for (int j = i + 1; j < current.size(); j++) {
                            Line n = new Line();
                            n.p1 = current.get(i);
                            n.p2 = current.get(j);
                            lines.add(n);
                        }
                    }
                    current.clear();
                    newparticles_list.remove(current);
                    l--;
                }
            }
        }
        else {
            //add new triangles
            if (newparticles_list.size() != 0) {
                for (int l = 0; l < newparticles_list.size(); l++) {
                    ArrayList<Particle> current = newparticles_list.get(l);
                    for (int i = 0; i < current.size(); i++) {
                        for (int j = i + 1; j < current.size(); j++) {
                            for (int k = j + 1; k < current.size(); k++) {
                                Triangle t = new Triangle();
                                t.p1 = current.get(i);
                                t.p2 = current.get(j);
                                t.p3 = current.get(k);
                                triangles.add(t);
                            }
                        }
                    }
                    current.clear();
                    newparticles_list.remove(current);
                    l--;
                }
            }
        }

        if(counter == 1) {
            //update particle movement
            for(int p = 0; p < allparticles.size(); p++){
                Particle current = allparticles.get(p);
                if(current.getLifetime() < current.getMaxLifetime()){
                    current.update();
                }
                else{
                    allparticles.remove(current);
                    p--;
                }
            }
            if(useLines) {
                //update line movement
                for (int l = 0; l < lines.size(); l++) {
                    Line current = lines.get(l);
                    if (current.p1.getLifetime() < current.p1.getMaxLifetime()) {
                        current.update();
                    } else {
                        lines.remove(current);
                        l--;
                    }
                }
            }
            else {
                //update triangle movement
                for (int l = 0; l < triangles.size(); l++) {
                    Triangle current = triangles.get(l);
                    if (current.p1.getLifetime() < current.p1.getMaxLifetime()) {
                        current.update();
                    } else {
                        triangles.remove(current);
                        l--;
                    }
                }
            }
            counter = 0;
        }


//        Log.d("mtag", "Number of particles" + allparticles.size());
//        Log.d("mtag", "Number of lines" + lines.size());

        for(int l = 0; l < lines.size(); l++){
            lines.get(l).draw(mMVPMatrix);
        }

        for(int t = 0; t < triangles.size(); t++){
            triangles.get(t).draw(mMVPMatrix);
        }

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

    public void addParticle(float x, float y, int lifetime, int num){
        ArrayList<Particle> newparticles = new ArrayList<>();
        float p_width = ((x / (window_width / 2)) - 1) * window_ratio;
        Log.d("mtag", "w_width: " + window_width + " w_r: " + window_ratio);
        float p_height = ((y / (window_height / 2)) - 1) * -1;
        Log.d("mtag", "p_width: " + p_width + " p_height: " + p_height);
        for(int i = 0; i < num; i++) {
            Particle p = new Particle(p_width, p_height, lifetime);
            allparticles.add(p);
            newparticles.add(p);
            newparticles_list.add(newparticles);
        }

    }

    public int getDelay(){
        return touch_delay;
    }

    public void setDelay(int delay){
        touch_delay = delay;
    }

    public void changeMode(){
        useLines = !useLines;
    }

    public void addLine(Line l){
        lines.add(l);
    }

}
