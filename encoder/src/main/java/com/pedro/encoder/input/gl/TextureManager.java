package com.pedro.encoder.input.gl;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.Surface;
import com.pedro.encoder.R;
import com.pedro.encoder.utils.GlUtil;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by pedro on 9/09/17.
 */

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class TextureManager {

  public final static String TAG = "TextureManager";

  private Context context;

  private static final int FLOAT_SIZE_BYTES = 4;
  private static final int TRIANGLE_VERTICES_DATA_STRIDE_BYTES = 5 * FLOAT_SIZE_BYTES;
  private static final int TRIANGLE_VERTICES_DATA_POS_OFFSET = 0;
  private static final int TRIANGLE_VERTICES_DATA_UV_OFFSET = 3;

  //rotation matrix
  private final float[] triangleVerticesData = {
      // X, Y, Z, U, V
      -1.0f, -1.0f, 0, 0.f, 0.f, 1.0f, -1.0f, 0, 1.f, 0.f, -1.0f, 1.0f, 0, 0.f, 1.f, 1.0f, 1.0f, 0,
      1.f, 1.f,
  };

  private FloatBuffer triangleVertices;

  private String VERTEX_SHADER;
  private String FRAGMENT_SHADER;

  private float[] mMVPMatrix = new float[16];
  private float[] mSTMatrix = new float[16];

  private int program;
  private int textureID = -12345;
  private int uMVPMatrixHandle;
  private int uSTMatrixHandle;
  private int aPositionHandle;
  private int aTextureHandle;

  private SurfaceTexture surfaceTexture;
  private Surface surface;

  public TextureManager(Context context) {
    this.context = context;
    triangleVertices = ByteBuffer.allocateDirect(triangleVerticesData.length * FLOAT_SIZE_BYTES)
        .order(ByteOrder.nativeOrder())
        .asFloatBuffer();
    triangleVertices.put(triangleVerticesData).position(0);
    Matrix.setIdentityM(mSTMatrix, 0);
  }

  public int getTextureId() {
    return textureID;
  }

  public SurfaceTexture getSurfaceTexture() {
    return surfaceTexture;
  }

  public Surface getSurface() {
    return surface;
  }

  public void updateFrame() {
    surfaceTexture.updateTexImage();
  }

  public void drawFrame() {
    GlUtil.checkGlError("onDrawFrame start");
    surfaceTexture.getTransformMatrix(mSTMatrix);

    GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

    GLES20.glUseProgram(program);
    GlUtil.checkGlError("glUseProgram");

    triangleVertices.position(TRIANGLE_VERTICES_DATA_POS_OFFSET);
    GLES20.glVertexAttribPointer(aPositionHandle, 3, GLES20.GL_FLOAT, false,
        TRIANGLE_VERTICES_DATA_STRIDE_BYTES, triangleVertices);
    GlUtil.checkGlError("glVertexAttribPointer maPosition");
    GLES20.glEnableVertexAttribArray(aPositionHandle);
    GlUtil.checkGlError("glEnableVertexAttribArray aPositionHandle");

    triangleVertices.position(TRIANGLE_VERTICES_DATA_UV_OFFSET);
    GLES20.glVertexAttribPointer(aTextureHandle, 2, GLES20.GL_FLOAT, false,
        TRIANGLE_VERTICES_DATA_STRIDE_BYTES, triangleVertices);
    GlUtil.checkGlError("glVertexAttribPointer aTextureHandle");
    GLES20.glEnableVertexAttribArray(aTextureHandle);
    GlUtil.checkGlError("glEnableVertexAttribArray aTextureHandle");

    Matrix.setIdentityM(mMVPMatrix, 0);
    GLES20.glUniformMatrix4fv(uMVPMatrixHandle, 1, false, mMVPMatrix, 0);
    GLES20.glUniformMatrix4fv(uSTMatrixHandle, 1, false, mSTMatrix, 0);

    GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
    GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureID);
    GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    GlUtil.checkGlError("glDrawArrays");
  }

  /**
   * Initializes GL state.  Call this after the EGL surface has been created and made current.
   */
  public SurfaceTexture createTexture() {
    GlUtil.checkGlError("create handlers start");
    VERTEX_SHADER = GlUtil.getStringFromRaw(context, R.raw.simple_vertex);
    FRAGMENT_SHADER = GlUtil.getStringFromRaw(context, R.raw.simple_fragment);
    program = GlUtil.createProgram(VERTEX_SHADER, FRAGMENT_SHADER);
    aPositionHandle = GLES20.glGetAttribLocation(program, "aPosition");
    aTextureHandle = GLES20.glGetAttribLocation(program, "aTextureCoord");
    uMVPMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix");
    uSTMatrixHandle = GLES20.glGetUniformLocation(program, "uSTMatrix");
    GlUtil.checkGlError("create handlers end");
    int[] textures = new int[1];
    GLES20.glGenTextures(1, textures, 0);

    textureID = textures[0];
    GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureID);
    GlUtil.checkGlError("glBindTexture textureID");

    GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MIN_FILTER,
        GLES20.GL_NEAREST);
    GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MAG_FILTER,
        GLES20.GL_LINEAR);
    GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_S,
        GLES20.GL_CLAMP_TO_EDGE);
    GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_T,
        GLES20.GL_CLAMP_TO_EDGE);
    GlUtil.checkGlError("glTexParameter");

    surfaceTexture = new SurfaceTexture(textureID);
    surface = new Surface(surfaceTexture);
    return surfaceTexture;
  }

  public void release() {
    surfaceTexture = null;
  }

  /**
   * Replaces the fragment shader.  Pass in null to reset to default.
   */
  public void changeFragmentShader(String fragmentShader) {
    if (fragmentShader == null) {
      fragmentShader = FRAGMENT_SHADER;
    }
    GLES20.glDeleteProgram(program);
    program = GlUtil.createProgram(VERTEX_SHADER, fragmentShader);
    if (program == 0) {
      throw new RuntimeException("failed creating program");
    }
  }
}