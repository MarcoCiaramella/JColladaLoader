package com.model3d.jcolladaloader;

import android.content.Context;
import android.opengl.GLES20;


public final class MeshShader extends Shader {

    private float[] mMatrix;
    private float[] mvpMatrix;
    private int textureIndex;
    private final int aPosition;
    private final int aNormal;
    private final int aColor;
    private final int aTexCoords;
    private final int uMMatrix;
    private final int uMVPMatrix;
    private final int uTexture;
    private final int uIsTextured;
    private MeshObj meshObj;
    private Vector3f viewPos;
    private final int uViewPos;

    public MeshShader(Context context) {
        super(context, "vs_mesh.glsl", "fs_mesh.glsl");
        GLES20.glUseProgram(getProgram());
        aPosition = getAttrib("aPosition");
        aNormal = getAttrib("aNormal");
        aColor = getAttrib("aColor");
        aTexCoords = getAttrib("aTexCoords");
        uMMatrix = getUniform("uMMatrix");
        uMVPMatrix = getUniform("uMVPMatrix");
        uTexture = getUniform("uTexture");
        uIsTextured = getUniform("uIsTextured");
        uViewPos = getUniform("uViewPos");
    }

    @Override
    public void bindData() {
        GLES20.glUseProgram(getProgram());
        GLES20.glEnableVertexAttribArray(aPosition);
        GLES20.glEnableVertexAttribArray(aNormal);
        GLES20.glEnableVertexAttribArray(aColor);
        GLES20.glEnableVertexAttribArray(aTexCoords);
        GLES20.glVertexAttribPointer(aPosition, 3, GLES20.GL_FLOAT, false, 0, meshObj.getVertices());
        GLES20.glVertexAttribPointer(aNormal, 3, GLES20.GL_FLOAT, false, 0, meshObj.getNormals());
        GLES20.glVertexAttribPointer(aColor, 4, GLES20.GL_FLOAT, false, 0, meshObj.getColors());
        GLES20.glVertexAttribPointer(aTexCoords, 2, GLES20.GL_FLOAT, false, 0, meshObj.getTexCoords());
        GLES20.glUniformMatrix4fv(uMMatrix, 1, false, mMatrix, 0);
        GLES20.glUniformMatrix4fv(uMVPMatrix, 1, false, mvpMatrix, 0);
        GLES20.glUniform3f(uViewPos, viewPos.x, viewPos.y, viewPos.z);
        textureIndex = 0;
        bindTexture(uTexture, meshObj.getTexture());
        GLES20.glUniform1i(uIsTextured, meshObj.getTexture());
    }

    @Override
    public void unbindData() {
        GLES20.glDisableVertexAttribArray(aPosition);
        GLES20.glDisableVertexAttribArray(aNormal);
        GLES20.glDisableVertexAttribArray(aColor);
    }

    private void bindTexture(int uniform, int texture){
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + textureIndex);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);
        GLES20.glUniform1i(uniform, textureIndex++);
    }

    public MeshShader setMMatrix(float[] mMatrix){
        this.mMatrix = mMatrix;
        return this;
    }

    public MeshShader setMvpMatrix(float[] mvpMatrix){
        this.mvpMatrix = mvpMatrix;
        return this;
    }

    public MeshShader setMesh(MeshObj meshObj){
        this.meshObj = meshObj;
        return this;
    }

    public MeshShader setViewPos(Vector3f viewPos){
        this.viewPos = viewPos;
        return this;
    }

}
