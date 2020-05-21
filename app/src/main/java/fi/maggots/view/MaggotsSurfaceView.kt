package fi.maggots.view

import android.content.Context
import android.opengl.GLSurfaceView
import android.view.MotionEvent
import fi.maggots.renderer.GameRenderer

internal const val TOUCH_SCALE_FACTOR: Float = 180.0f / 320f

class MaggotsSurfaceView(context: Context) : GLSurfaceView(context) {
    private val renderer: GameRenderer

    init {
        setEGLContextClientVersion(2)
        renderer = GameRenderer(context)
        setRenderer(renderer)

        // Render the view only when there is a change in the drawing data
        renderMode = RENDERMODE_WHEN_DIRTY
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.action) {
            MotionEvent.ACTION_MOVE -> {
                renderer.mTriangle.angle += renderer.mTriangle.move(renderer.mTriangle, event.x, event.y, height, width)
                requestRender()
            }
        }
        return true
    }
}