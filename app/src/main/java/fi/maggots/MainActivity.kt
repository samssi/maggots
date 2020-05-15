package fi.maggots

import android.content.Context
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import fi.maggots.renderer.GameRenderer

private const val TOUCH_SCALE_FACTOR: Float = 180.0f / 320f

class MaggotsSurfaceView(context: Context) : GLSurfaceView(context) {
    private val renderer: GameRenderer
    private var previousX: Float = 0f
    private var previousY: Float = 0f

    init {
        setEGLContextClientVersion(2)
        renderer = GameRenderer(context)
        setRenderer(renderer)

        // Render the view only when there is a change in the drawing data
        renderMode = RENDERMODE_WHEN_DIRTY
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        val x: Float = event.x
        val y: Float = event.y

        when(event.action) {
            MotionEvent.ACTION_MOVE -> {
                var directionX: Float = x - previousX
                var directionY: Float = y - previousY

                // reverse direction of rotation above the mid-line
                if (y > height / 2) {
                    directionX *= -1
                }

                // reverse direction of rotation to left of the mid-line
                if (x < width / 2) {
                    directionY *= -1
                }

                renderer.angle += (directionX + directionY) * TOUCH_SCALE_FACTOR
                requestRender()
            }
        }
        previousX = x
        previousY = y
        return true
    }
}

class MainActivity : AppCompatActivity() {
    private lateinit var glView: GLSurfaceView

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        glView = MaggotsSurfaceView(this)
        setContentView(glView)
    }
}

