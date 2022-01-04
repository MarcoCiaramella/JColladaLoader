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
    private Bitmap textureBitmap;

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


    protected void doTransformation(float[] mMatrix) {
    }

    protected int getTexture(){
        return texture;
    }

    private void loadTexture(){
        if (textureBitmap == null) {
            return;
        }
        int[] texture = new int[1];
        GLES20.glGenTextures(1, texture, 0);
        this.texture = texture[0];
        if (this.texture != 0) {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, this.texture);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, textureBitmap, 0);
            textureBitmap.recycle();
        }
        else {
            throw new RuntimeException("Error loading texture.");
        }
    }

    protected Bitmap getTextureBitmap() {
        return textureBitmap;
    }

    protected MeshDae rotateX(float[] mMatrix){
        Matrix.rotateM(mMatrix, 0, rotation.x, 1, 0, 0);
        return this;
    }

    protected MeshDae rotateY(float[] mMatrix){
        Matrix.rotateM(mMatrix, 0, rotation.y, 0, 1, 0);
        return this;
    }

    protected MeshDae rotateZ(float[] mMatrix){
        Matrix.rotateM(mMatrix, 0, rotation.z, 0, 0, 1);
        return this;
    }

    protected MeshDae scale(float[] mMatrix){
        Matrix.scaleM(mMatrix, 0, scale, scale, scale);
        return this;
    }

    protected MeshDae translate(float[] mMatrix){
        Matrix.translateM(mMatrix, 0, position.x, position.y, position.z);
        return this;
    }

    protected int getNumVertices(){
        return vertices.capacity()/3;
    }
}
