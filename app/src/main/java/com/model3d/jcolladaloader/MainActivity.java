package com.model3d.jcolladaloader;

import androidx.appcompat.app.AppCompatActivity;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException ignored){}

        GLSurfaceView glSurfaceView = new GLSurfaceView(this);
        MeshRenderer meshRenderer = new MeshRenderer(this, "Tree.dae");
        glSurfaceView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_FULLSCREEN);
        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setRenderer(meshRenderer);
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        setContentView(glSurfaceView);
    }
}