package com.model3d.jcolladaloader;

import android.content.Context;
import android.opengl.GLES20;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;


public abstract class Shader {

    private final int program;

    public Shader(Context context, String vs, String fs){
        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, compile(context, GLES20.GL_VERTEX_SHADER, vs));
        GLES20.glAttachShader(program, compile(context, GLES20.GL_FRAGMENT_SHADER, fs));
        GLES20.glLinkProgram(program);
        int[] params = new int[1];
        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, params, 0);
        if (params[0] == GLES20.GL_FALSE){
            throw new RuntimeException("Program linking failed:\n"+GLES20.glGetProgramInfoLog(program));
        }
    }

    public abstract void bindData();
    public abstract void unbindData();

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
