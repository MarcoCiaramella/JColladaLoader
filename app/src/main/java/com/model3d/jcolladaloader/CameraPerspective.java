package com.model3d.jcolladaloader;

import android.opengl.Matrix;


final class CameraPerspective {

    private final float[] vpMatrix;
    protected float[] projectionMatrix;
    private final float[] viewMatrix;
    protected float[] eye;
    protected float[] center;
    protected float[] up;
    protected final float near;
    protected final float far;
    protected int width;
    protected int height;

    protected CameraPerspective(float[] eye, float[] center, float[] up, float near, float far) {
        vpMatrix = new float[16];
        projectionMatrix = new float[16];
        viewMatrix = new float[16];
        this.eye = eye;
        this.center = center;
        this.up = up;
        this.near = near;
        this.far = far;
    }

    protected void loadVpMatrix() {
        float ratio = (float)width/(float)height;
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, near, far);
        createVpMatrix();
    }

    protected void createVpMatrix(){
        // Set the camera position (View matrix)
        Matrix.setLookAtM(viewMatrix, 0, eye.x, eye.y, eye.z, center.x, center.y, center.z, up.x, up.y, up.z);
        // Calculate the projection and view transformation
        Matrix.multiplyMM(vpMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
    }

    protected float[] getEye(){
        return eye;
    }

    protected float[] getUp(){
        return up;
    }

    protected float[] getCenter(){
        return center;
    }

    protected float getNear(){
        return near;
    }

    protected float getFar(){
        return far;
    }

    protected float[] getViewMatrix(){
        return viewMatrix;
    }

    protected float[] getVpMatrix(){
        return vpMatrix;
    }

    protected CameraPerspective setWidth(int width){
        this.width = width;
        return this;
    }

    protected CameraPerspective setHeight(int height){
        this.height = height;
        return this;
    }
}
