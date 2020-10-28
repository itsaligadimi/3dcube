package com.agadimi.a3dcube.glversion;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.agadimi.a3dcube.R;

import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import timber.log.Timber;

public class GlRenderer implements GLSurfaceView.Renderer
{
    private final String vertexShaderCode =
            "uniform mat4 projection;" +
                    "uniform mat4 modelView;" +
                    "attribute vec4 vertexPosition;" +
                    "void main()" +
                    "{" +
                    " gl_Position = projection * modelView * vertexPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "void main() {" +
                    "  gl_FragColor = vec4(0.011, 0.866, 0.839, 1.0);" +
                    "}";

    private Context context;
    private int model;
    private List<MeshArray> meshArrays;
    public volatile float xAngle, yAngle, scale = 1;

    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private float[] xRotationMatrix = new float[16];
    private float[] yRotationMatrix = new float[16];
    private float[] rotationMatrix = new float[16];
    private float[] scaleMatrix = new float[16];
    private float[] scaleRotationMatrix = new float[16];
    private final int vertexStride = 3 * 4; // each vertext is made of 3 float 4 bytes per vertex

    private int mProgram;
    private int projectMatHandle;
    private int modelViewMatHandle;
    private int positionHandle;


    public GlRenderer(Context context)
    {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig)
    {
        GLES20.glClearColor(0.211f, 0.211f, 0.211f, 0);

        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height)
    {
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 1, 10);
    }

    @Override
    public void onDrawFrame(GL10 gl10)
    {
        if (meshArrays == null || meshArrays.isEmpty())
        {
            return;
        }

        float[] scratch = new float[16];
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(viewMatrix, 0, 0, 0, -5, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
//        Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

        // Create a rotation transformation for the triangle
//        long time = SystemClock.uptimeMillis() % 4000L;
//        float angle = 0.090f * ((int) time);
//        Timber.d("x: %f, y: %f", xAngle, yAngle);
        Matrix.setRotateM(xRotationMatrix, 0, xAngle, 0, 1, 0);
        Matrix.setRotateM(yRotationMatrix, 0, yAngle, -1, 0, 0);
        Matrix.multiplyMM(rotationMatrix, 0, xRotationMatrix, 0, yRotationMatrix, 0);

//        Matrix.scaleM(scaleMatrix, 0, scale, scale, scale);
//        Matrix.multiplyMM(scaleRotationMatrix, 0, scaleMatrix, 0, rotationMatrix, 0);


        // Combine the rotation matrix with the projection and camera view
        // Note that the vPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        Matrix.multiplyMM(scratch, 0, viewMatrix, 0, rotationMatrix, 0);


        GLES20.glUseProgram(mProgram);

        //get handles
        positionHandle = GLES20.glGetAttribLocation(mProgram, "vertexPosition");
        projectMatHandle = GLES20.glGetUniformLocation(mProgram, "projection");
        modelViewMatHandle = GLES20.glGetUniformLocation(mProgram, "modelView");


        GLES20.glEnableVertexAttribArray(positionHandle);

        GLES20.glUniformMatrix4fv(projectMatHandle, 1, false, projectionMatrix, 0);
        GLES20.glUniformMatrix4fv(modelViewMatHandle, 1, false, scratch, 0);

        //draw each face separately
        for (int i = 0; i < meshArrays.size(); i++)
        {
            GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, vertexStride, meshArrays.get(i).getBuffer());
            GLES20.glDrawArrays(GLES20.GL_LINE_LOOP, 0, meshArrays.get(i).getVertices());
        }

        GLES20.glDisableVertexAttribArray(positionHandle);
    }

    public int loadShader(int type, String shaderCode)
    {
        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    /**
     * Utility method for debugging OpenGL calls. Provide the name of the call
     * just after making it:
     *
     * <pre>
     * mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
     * MyGLRenderer.checkGlError("glGetUniformLocation");</pre>
     * <p>
     * If the operation is not successful, the check throws an error.
     *
     * @param glOperation - Name of the OpenGL call to check.
     */
    public static void checkGlError(String glOperation)
    {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR)
        {
            Timber.e(glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }

    public float getxAngle()
    {
        return xAngle;
    }

    public void setxAngle(float xAngle)
    {
        if (xAngle > 45)
        {
            this.xAngle = 45;
        }
        else if (xAngle < -45)
        {
            this.xAngle = -45;
        }
        else
        {
            this.xAngle = xAngle;
        }
    }

    public float getyAngle()
    {
        return yAngle;
    }

    public void setyAngle(float yAngle)
    {
        if (yAngle > 30)
        {
            this.yAngle = 30;
        }
        else if (yAngle < -30)
        {
            this.yAngle = -30;
        }
        else
        {
            this.yAngle = yAngle;
        }
    }

    public void setScale(float scale)
    {
//        this.scale = (float) Math.max(0.5, scale);
    }

    public void setModel(int model)
    {
        this.model = model;
        meshArrays = ObjectLoader.loadModelMeshFromStream(context.getResources().openRawResource(model));
    }
}
