package com.model3d.jcolladaloader;

import java.util.ArrayList;

class MyGesture implements Gesture {

    private final ArrayList<MeshObj> meshes;

    public MyGesture(ArrayList<MeshObj> meshes){
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
            for (MeshObj mesh : meshes) {
                mesh.getRotation().y += velX;
            }
            //skyBox.getRotation().y += velX;
        }
        else if (Math.abs(velY) > Math.abs(velX)) {
            for (MeshObj mesh : meshes) {
                mesh.getRotation().x += velY;
            }
            //skyBox.getRotation().x += velY;
        }
    }

    @Override
    public void onScale(float scaleFactor) {
        for (MeshObj mesh : meshes) {
            float newScale = mesh.getScale() * Math.max(0.1f, Math.min(scaleFactor, 5.0f));
            mesh.setScale(newScale);
        }
    }
}
