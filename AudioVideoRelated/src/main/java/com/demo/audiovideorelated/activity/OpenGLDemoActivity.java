package com.demo.audiovideorelated.activity;

import android.opengl.GLSurfaceView;
import android.os.Bundle;

import com.demo.audiovideorelated.widget.MyGLSurfaceView;

public class OpenGLDemoActivity extends BaseActivity {
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