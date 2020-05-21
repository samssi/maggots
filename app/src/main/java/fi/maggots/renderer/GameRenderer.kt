package fi.maggots.renderer

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import fi.maggots.objects3d.Triangle
import fi.maggots.view.Camera
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class GameRenderer(// vPMatrix is an abbreviation for "Model View Projection Matrix"
    private val context: Context
) : GLSurfaceView.Renderer {
    internal lateinit var mTriangle: Triangle
    internal val camera = Camera(FloatArray(16), 0,0f, 0f, -3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
    private val vPMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val rotationMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)

    override fun onDrawFrame(gl: GL10?) {
        val scratch = FloatArray(16)

        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        // Set the camera position
        Matrix.setLookAtM(viewMatrix, 0, camera.eyeX, camera.eyeY, camera.eyeZ, camera.centerX, camera.centerY, camera.centerZ, camera.upX, camera.upY, camera.upZ)

        // Calculate the projection and view transformation
        Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

        // Create a rotation transformation for the triangle
        //val time = SystemClock.uptimeMillis() % 4000L
        //val angle = 0.090f * time.toInt()
        Matrix.setRotateM(rotationMatrix, 0, mTriangle.angle, 0f, 0f, -1.0f)

        // Combine the rotation matrix with the projection and camera view
        // Note that the vPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        Matrix.multiplyMM(scratch, 0, vPMatrix, 0, rotationMatrix, 0)

        mTriangle.draw(scratch)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0,0, width, height)

        val aspectRatio: Float = width.toFloat() / height.toFloat()

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, 3f, 7f)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // Set background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        mTriangle = Triangle(context)
    }
}