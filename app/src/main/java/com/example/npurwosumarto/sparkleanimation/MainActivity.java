package com.example.npurwosumarto.sparkleanimation;

import android.content.Context;
import android.opengl.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

public class MainActivity extends AppCompatActivity{

    private GLSurfaceView mGLView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        mGLView = new MyGLSurfaceView(this);
        setContentView(mGLView);
    }
}

class MyGLSurfaceView extends GLSurfaceView {

    private final MyGLRenderer mRenderer;

    public MyGLSurfaceView(Context context){
        super(context);

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

        mRenderer = new MyGLRenderer();

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer);
    }

    private int NUM_PARTICLES = 6;

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        final int pointerCount = e.getPointerCount();

        // get pointer index from the event object
        int pointerIndex = e.getActionIndex();

        float x = e.getX(pointerIndex);
        float y = e.getY(pointerIndex);

        switch (e.getActionMasked()) {

            case MotionEvent.ACTION_DOWN:
                if(y < 25){
                    mRenderer.changeMode();
                }
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.d("mtag", "x: " + x + " y: " + y);
                mRenderer.addParticle(x, y, 0, NUM_PARTICLES);
                mRenderer.setDelay(8);
                break;
            case MotionEvent.ACTION_MOVE:
                if(mRenderer.getDelay() > 0){
                    return true;
                }
                for (int p = 0; p < pointerCount; p++) {
                    float x1 = e.getX(p);
                    float y1 = e.getY(p);
                    Log.d("mtag", "x: " + x1 + " y: " + y1);
                    mRenderer.addParticle(x1, y1, 0, NUM_PARTICLES);
                }
                mRenderer.setDelay(8);
                break;
        }

        return true;
    }
}


