package com.model3d.jcolladaloader;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.VelocityTracker;
import android.view.View;

import com.model3d.jcolladaloaderlib.ColladaLoader;
import com.model3d.jcolladaloaderlib.model.Object3DData;

import java.nio.Buffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MeshView extends GLSurfaceView implements GLSurfaceView.Renderer, View.OnTouchListener, ScaleGestureDetector.OnScaleGestureListener  {

    private MeshShader meshShader;
    private final float[] mvpMatrix = new float[16];
    private CameraPerspective cameraPerspective;
    private static final float[] CAMERA_EYE = {0,0,30f};
    private static final float[] CAMERA_CENTER = {0,0,0};
    private static final float[] CAMERA_UP = {0,1,0};
    private Gesture gesture;
    private boolean gestureProcessed = false;
    private final ScaleGestureDetector scaleDetector;
    private List<Object3DData> meshes;
    private final String meshFilename;

    public MeshView(Context context, String meshFilename) {
        super(context);
        this.meshFilename = meshFilename;
        setSystemUiVisibility(SYSTEM_UI_FLAG_IMMERSIVE | SYSTEM_UI_FLAG_FULLSCREEN);
        setEGLContextClientVersion(2);
        setRenderer(this);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        scaleDetector = new ScaleGestureDetector(context, this);
        setOnTouchListener(this);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glDepthFunc(GLES20.GL_LESS);
        GLES20.glClearDepthf(1.0f);

        cameraPerspective = new CameraPerspective(CAMERA_EYE, CAMERA_CENTER, CAMERA_UP, 1f, 100f);
        meshes = new ColladaLoader().loadFromAsset(getContext(), meshFilename);
        gesture = new MyGesture(meshes);
        meshShader = new MeshShader(getContext());
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        cameraPerspective.setWidth(width).setHeight(height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClearColor(0, 0, 0, 1);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        cameraPerspective.loadVpMatrix();

        meshShader.setViewPos(cameraPerspective.getEye());
        for (Object3DData mesh : meshes) {
            Matrix.multiplyMM(mvpMatrix, 0, cameraPerspective.getVpMatrix(), 0, mesh.getModelMatrix(), 0);
            meshShader.setMesh(mesh);
            meshShader.setMvpMatrix(mvpMatrix);
            meshShader.bindData();
            for (int i = 0; i < mesh.getElements().size(); i++) {
                Buffer indices = mesh.getElements().get(i).getIndexBuffer().position(0);
                GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.capacity(), GLES20.GL_UNSIGNED_INT, indices);
            }
            meshShader.unbindData();
        }
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        if (gesture != null) {
            gesture.onScale(detector.getScaleFactor());
            gestureProcessed = true;
        }
        invalidate();
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        gestureProcessed = true;
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        gestureProcessed = true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (gesture != null) {
            if (!checkMotionEvent(event)){
                return true;
            }
            v.performClick();
            int pointerIndex = event.getActionIndex();
            int action = event.getActionMasked();
            int pointerId = event.getPointerId(pointerIndex);
            float posX = event.getX(pointerId);
            float posY = getHeight() - event.getY(pointerId);
            scaleDetector.onTouchEvent(event);
            if (gestureProcessed){
                gestureProcessed = false;
                return true;
            }
            switch (action) {
                case MotionEvent.ACTION_MOVE: {
                    VelocityTracker vt = VelocityTracker.obtain();
                    vt.addMovement(event);
                    vt.computeCurrentVelocity(1);
                    float velX = vt.getXVelocity();
                    float velY = vt.getYVelocity();
                    gesture.onMove(posX, posY, velX, velY);
                    vt.recycle();
                }
                break;
                case MotionEvent.ACTION_DOWN: {
                    gesture.onClick(posX, posY);
                }
                break;
                case MotionEvent.ACTION_UP: {
                    gesture.onRelease(posX, posY);
                }
                break;
                default: {
                    return v.onTouchEvent(event);
                }
            }
        }
        return true;
    }

    private boolean checkMotionEvent(MotionEvent event){
        int numPointer = event.getPointerCount();
        int pointerIndex = event.getActionIndex();
        if (pointerIndex >= numPointer){
            return false;
        }
        int pointerId = event.getPointerId(pointerIndex);
        return pointerId < numPointer;
    }
}
