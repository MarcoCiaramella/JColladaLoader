package com.model3d.jcolladaloader;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import com.model3d.jcolladaloaderlib.ColladaLoader;
import com.model3d.jcolladaloaderlib.animation.Animator;
import com.model3d.jcolladaloaderlib.model.Object3DData;
import java.nio.Buffer;
import java.util.List;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MeshRenderer implements GLSurfaceView.Renderer {

    private MeshShader meshShader;
    private final float[] mvpMatrix = new float[16];
    private CameraPerspective cameraPerspective;
    private static final float[] CAMERA_EYE = {0,0,30f};
    private static final float[] CAMERA_CENTER = {0,0,0};
    private static final float[] CAMERA_UP = {0,1,0};
    private List<Object3DData> meshes;
    private final String meshFilename;
    private final Animator animator = new Animator();
    private final Context context;

    public MeshRenderer(Context context, String meshFilename) {
        this.context = context;
        this.meshFilename = meshFilename;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glDepthFunc(GLES20.GL_LESS);
        GLES20.glClearDepthf(1.0f);

        cameraPerspective = new CameraPerspective(CAMERA_EYE, CAMERA_CENTER, CAMERA_UP, 1f, 100f);
        meshes = new ColladaLoader().loadFromAsset(context, meshFilename);
        meshShader = new MeshShader(context);
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
            animator.update(mesh, false);
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
}
