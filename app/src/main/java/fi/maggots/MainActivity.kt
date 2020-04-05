package fi.maggots

import android.opengl.GLSurfaceView
import android.opengl.GLU
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


internal class Cube {
    private val mVertexBuffer: FloatBuffer
    private val mColorBuffer: FloatBuffer
    private val mIndexBuffer: ByteBuffer

    private val vertices = floatArrayOf(
        -1.0f, -1.0f, -1.0f,
        1.0f, -1.0f, -1.0f,
        1.0f, 1.0f, -1.0f,
        -1.0f, 1.0f, -1.0f,
        -1.0f, -1.0f, 1.0f,
        1.0f, -1.0f, 1.0f,
        1.0f, 1.0f, 1.0f,
        -1.0f, 1.0f, 1.0f
    )
    private val colors = floatArrayOf(
        0.0f, 1.0f, 0.0f, 1.0f,
        0.0f, 1.0f, 0.0f, 1.0f,
        1.0f, 0.5f, 0.0f, 1.0f,
        1.0f, 0.5f, 0.0f, 1.0f,
        1.0f, 0.0f, 0.0f, 1.0f,
        1.0f, 0.0f, 0.0f, 1.0f,
        0.0f, 0.0f, 1.0f, 1.0f,
        1.0f, 0.0f, 1.0f, 1.0f
    )
    private val indices = byteArrayOf(
        0, 4, 5, 0, 5, 1,
        1, 5, 6, 1, 6, 2,
        2, 6, 7, 2, 7, 3,
        3, 7, 4, 3, 4, 0,
        4, 7, 6, 4, 6, 5,
        3, 0, 1, 3, 1, 2
    )

    fun draw(gl: GL10) {
        gl.glFrontFace(GL10.GL_CW)
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer)
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, mColorBuffer)
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY)
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY)
        gl.glDrawElements(
            GL10.GL_TRIANGLES, 36, GL10.GL_UNSIGNED_BYTE,
            mIndexBuffer
        )
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY)
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY)
    }

    init {
        var byteBuf: ByteBuffer = ByteBuffer.allocateDirect(vertices.size * 4)
        byteBuf.order(ByteOrder.nativeOrder())
        mVertexBuffer = byteBuf.asFloatBuffer()
        mVertexBuffer.put(vertices)
        mVertexBuffer.position(0)
        byteBuf = ByteBuffer.allocateDirect(colors.size * 4)
        byteBuf.order(ByteOrder.nativeOrder())
        mColorBuffer = byteBuf.asFloatBuffer()
        mColorBuffer.put(colors)
        mColorBuffer.position(0)
        mIndexBuffer = ByteBuffer.allocateDirect(indices.size)
        mIndexBuffer.put(indices)
        mIndexBuffer.position(0)
    }
}


class GameRenderer : GLSurfaceView.Renderer {
    private val cube = Cube()
    private var rotation : Float = 0f;

    override fun onDrawFrame(gl: GL10?) {
        gl?.glClear(GL10.GL_COLOR_BUFFER_BIT or GL10.GL_DEPTH_BUFFER_BIT)
        gl?.glLoadIdentity();
        gl?.glTranslatef(0.0f, 0.0f, -10.0f);
        gl?.glRotatef(rotation, 1.0f, 1.0f, 1.0f);
        cube.draw(gl!!);
        gl.glLoadIdentity();
        rotation -= 0.55f;
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        // Sets the current view port to the new size.
        // Sets the current view port to the new size.
        gl?.glViewport(0, 0, width, height)
        // Select the projection matrix
        // Select the projection matrix
        gl?.glMatrixMode(GL10.GL_PROJECTION)
        // Reset the projection matrix
        // Reset the projection matrix
        gl?.glLoadIdentity()
        // Calculate the aspect ratio of the window
        // Calculate the aspect ratio of the window
        GLU.gluPerspective(gl, 45.0f, width.toFloat() / height.toFloat(), 0.1f, 100.0f)
        // Select the modelview matrix
        // Select the modelview matrix
        gl?.glMatrixMode(GL10.GL_MODELVIEW)
        // Reset the modelview matrix
        // Reset the modelview matrix
        gl?.glLoadIdentity()
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        gl?.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
        // Depth buffer setup.
        gl?.glClearDepthf(1.0f);
        // Enables depth testing.
        gl?.glEnable(GL10.GL_DEPTH_TEST);
        // The type of depth testing to do.
        gl?.glDepthFunc(GL10.GL_LEQUAL);
        // Really nice perspective calculations.
        gl?.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
    }

}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val view = GLSurfaceView(this)
        view.setRenderer(GameRenderer())
        setContentView(view)
    }
}
