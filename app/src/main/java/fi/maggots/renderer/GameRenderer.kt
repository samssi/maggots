package fi.maggots.renderer

import android.opengl.GLSurfaceView
import android.opengl.GLU
import fi.maggots.objects3d.Cube
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

internal class GameRenderer : GLSurfaceView.Renderer {
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
        gl?.glViewport(0, 0, width, height)
        // Select the projection matrix
        gl?.glMatrixMode(GL10.GL_PROJECTION)
        // Reset the projection matrix
        gl?.glLoadIdentity()
        // Calculate the aspect ratio of the window
        GLU.gluPerspective(gl, 45.0f, width.toFloat() / height.toFloat(), 0.1f, 100.0f)
        // Select the modelview matrix
        gl?.glMatrixMode(GL10.GL_MODELVIEW)
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