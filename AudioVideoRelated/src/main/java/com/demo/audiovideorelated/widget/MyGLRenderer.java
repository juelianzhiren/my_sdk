package com.demo.audiovideorelated.widget;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyGLRenderer implements GLSurfaceView.Renderer {
    private static final String TAG = "noahedu.MyGLRenderer";

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        Log.v(TAG, "onSurfaceCreated");
        GLES20.glClearColor(1f, 1f, 1f, 0.5f);
    }

    public void onDrawFrame(GL10 unused) {
        // Redraw background color
        Log.v(TAG, "onDrawFrame");
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        Log.v(TAG, "onSurfaceChanged, width = " + width + ", height = " + height);
        GLES20.glViewport(0, 0, width, height);
    }
}