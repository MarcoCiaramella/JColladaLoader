package com.model3d.jcolladaloader;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import com.model3d.jcolladaloaderlib.model.AnimatedModel;
import com.model3d.jcolladaloaderlib.model.Object3DData;

import java.nio.FloatBuffer;

class MeshDae {

    private final Object3DData object3DData;
    private int texture = 0;

    protected MeshDae(Object3DData object3DData){
        this.object3DData = object3DData;
    }

    protected FloatBuffer getVertexBuffer(){
        return object3DData.getVertexBuffer();
    }

    protected FloatBuffer getNormalsBuffer(){
        return object3DData.getNormalsBuffer();
    }

    protected FloatBuffer getColorsBuffer(){
        return object3DData.getColorsBuffer();
    }

    protected FloatBuffer getTextureBuffer(){
        return object3DData.getTextureBuffer();
    }

    protected FloatBuffer getVertexWeights(){
        return ((AnimatedModel)object3DData).getVertexWeights();
    }

    protected FloatBuffer getJointIds(){
        return ((AnimatedModel)object3DData).getJointIds();
    }

    protected float[] getBindShapeMatrix(){
        return ((AnimatedModel)object3DData).getBindShapeMatrix();
    }

    protected float[] getModelMatrix(){
        return object3DData.getModelMatrix();
    }

    protected int getTexture(){
        return texture;
    }

    protected Bitmap getTextureBitmap() {
        return textureBitmap;
    }

    protected int getNumVertices(){
        return vertices.capacity()/3;
    }
}
