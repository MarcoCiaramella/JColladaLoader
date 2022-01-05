package com.model3d.jcolladaloader;

import com.model3d.jcolladaloaderlib.model.Object3DData;

import java.util.List;

class MyGesture implements Gesture {

    private final List<Object3DData> meshes;

    public MyGesture(List<Object3DData> meshes){
        this.meshes = meshes;
    }

    @Override
    public void onClick(float x, float y) {

    }

    @Override
    public void onRelease(float x, float y) {

    }

    @Override
    public void onMove(float x, float y, float velX, float velY) {
        if (Math.abs(velX) > Math.abs(velY)) {
            for (Object3DData mesh : meshes) {
                float[] rotation = mesh.getRotation();
                rotation[1] += velX;
                mesh.setRotation(rotation);
            }
        }
        else if (Math.abs(velY) > Math.abs(velX)) {
            for (Object3DData mesh : meshes) {
                float[] rotation = mesh.getRotation();
                rotation[0] += velY;
                mesh.setRotation(rotation);
            }
        }
    }

    @Override
    public void onScale(float scaleFactor) {
        for (Object3DData mesh : meshes) {
            float[] scale = mesh.getScale();
            scale[0] *= Math.max(0.1f, Math.min(scaleFactor, 5.0f));
            scale[1] *= Math.max(0.1f, Math.min(scaleFactor, 5.0f));
            scale[2] *= Math.max(0.1f, Math.min(scaleFactor, 5.0f));
            mesh.setScale(scale);
        }
    }
}
