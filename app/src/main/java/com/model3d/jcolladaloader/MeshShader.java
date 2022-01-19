package com.model3d.jcolladaloader;

import android.content.Context;
import android.opengl.GLES20;

import com.model3d.jcolladaloaderlib.model.AnimatedModel;
import com.model3d.jcolladaloaderlib.model.Object3DData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


final class MeshShader {

    private float[] mvpMatrix;
    private int textureIndex;
    private final int aPosition;
    private final int aNormal;
    private final int aTexCoords;
    private final int uMMatrix;
    private final int uMVPMatrix;
    private final int uTexture;
    private Object3DData mesh;
    private float[] viewPos;
    private final int uViewPos;
    private final int program;
    private final int aJointIndices;
    private final int aWeights;
    private final int uBindShapeMatrix;
    private final List<Integer> uJointTransforms;

    protected MeshShader(Context context) {
        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, compile(context, GLES20.GL_VERTEX_SHADER, "vs_mesh.glsl"));
        GLES20.glAttachShader(program, compile(context, GLES20.GL_FRAGMENT_SHADER, "fs_mesh.glsl"));
        GLES20.glLinkProgram(program);
        int[] params = new int[1];
        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, params, 0);
        if (params[0] == GLES20.GL_FALSE){
            throw new RuntimeException("Program linking failed:\n"+GLES20.glGetProgramInfoLog(program));
        }
        GLES20.glUseProgram(getProgram());
        aPosition = getAttrib("aPosition");
        aNormal = getAttrib("aNormal");
        aTexCoords = getAttrib("aTexCoords");
        uMMatrix = getUniform("uMMatrix");
        uMVPMatrix = getUniform("uMVPMatrix");
        uTexture = getUniform("uTexture");
        uViewPos = getUniform("uViewPos");
        aJointIndices = getAttrib("aJointIndices");
        aWeights = getAttrib("aWeights");
        uBindShapeMatrix = getUniform("uBindShapeMatrix");
        uJointTransforms = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            uJointTransforms.add(getUniform("uJointTransforms["+i+"]"));
        }
    }

    protected void bindData() {
        GLES20.glUseProgram(getProgram());
        GLES20.glEnableVertexAttribArray(aPosition);
        GLES20.glEnableVertexAttribArray(aNormal);
        GLES20.glEnableVertexAttribArray(aTexCoords);
        GLES20.glEnableVertexAttribArray(aWeights);
        GLES20.glEnableVertexAttribArray(aJointIndices);
        GLES20.glVertexAttribPointer(aPosition, 3, GLES20.GL_FLOAT, false, 0, mesh.getVertexBuffer().position(0));
        GLES20.glVertexAttribPointer(aNormal, 3, GLES20.GL_FLOAT, false, 0, mesh.getNormalsBuffer().position(0));
        GLES20.glVertexAttribPointer(aTexCoords, 2, GLES20.GL_FLOAT, false, 0, mesh.getTextureBuffer().position(0));
        GLES20.glUniformMatrix4fv(uMMatrix, 1, false, mesh.getModelMatrix(), 0);
        GLES20.glUniformMatrix4fv(uMVPMatrix, 1, false, mvpMatrix, 0);
        GLES20.glUniform3f(uViewPos, viewPos[0], viewPos[1], viewPos[2]);
        textureIndex = 0;
        bindTexture(uTexture, mesh.getMaterial().getTextureId());
        GLES20.glVertexAttribPointer(aWeights, 3, GLES20.GL_FLOAT, false, 0, ((AnimatedModel) mesh).getVertexWeights().position(0));
        GLES20.glVertexAttribPointer(aJointIndices, 3, GLES20.GL_FLOAT, false, 0, ((AnimatedModel) mesh).getJointIds().position(0));
        GLES20.glUniformMatrix4fv(uBindShapeMatrix, 1, false, ((AnimatedModel) mesh).getBindShapeMatrix(), 0);
        for (int i = 0; i < ((AnimatedModel) mesh).getJointTransforms().length; i++) {
            GLES20.glUniformMatrix4fv(uJointTransforms.get(i), 1, false, ((AnimatedModel) mesh).getJointTransforms()[i], 0);
        }
    }

    protected void unbindData() {
        GLES20.glDisableVertexAttribArray(aPosition);
        GLES20.glDisableVertexAttribArray(aNormal);
        GLES20.glDisableVertexAttribArray(aWeights);
        GLES20.glDisableVertexAttribArray(aJointIndices);
    }

    private void bindTexture(int uniform, int texture){
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + textureIndex);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);
        GLES20.glUniform1i(uniform, textureIndex++);
    }

    protected MeshShader setMvpMatrix(float[] mvpMatrix){
        this.mvpMatrix = mvpMatrix;
        return this;
    }

    protected MeshShader setMesh(Object3DData mesh){
        this.mesh = mesh;
        return this;
    }

    protected MeshShader setViewPos(float[] viewPos){
        this.viewPos = viewPos;
        return this;
    }

    private int compile(Context context, int type, String filename){
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, read(context, filename));
        GLES20.glCompileShader(shader);
        int[] params = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, params, 0);
        if (params[0] == GLES20.GL_FALSE){
            throw new RuntimeException("Compilation of shader '" + filename + "' failed:\n"+GLES20.glGetShaderInfoLog(shader));
        }
        return shader;
    }

    public int getProgram(){
        return program;
    }

    protected int getAttrib(String name){
        int location = GLES20.glGetAttribLocation(program,name);
        checkLocation(location, name);
        return location;
    }

    protected int getUniform(String name){
        int location = GLES20.glGetUniformLocation(program,name);
        checkLocation(location, name);
        return location;
    }

    private static String read(Context context, String filename){

        StringBuilder buf = new StringBuilder();
        InputStream text;
        BufferedReader in;
        String str;

        try {
            text = context.getAssets().open(filename);
            in = new BufferedReader(new InputStreamReader(text, StandardCharsets.UTF_8));
            while ( (str = in.readLine()) != null ) {
                str += '\n';
                buf.append(str);
            }
            in.close();

            return buf.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        throw new RuntimeException("Error loading file "+filename+".");

    }

    private static void checkLocation(int location, String label) {
        if (location < 0) {
            throw new RuntimeException("Unable to locate '" + label + "' in program");
        }
    }

}
