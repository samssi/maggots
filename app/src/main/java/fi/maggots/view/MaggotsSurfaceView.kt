package fi.maggots.view

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.core.view.GestureDetectorCompat
import fi.maggots.renderer.GameRenderer
import fi.maggots.util.DEBUG_TAG

internal const val TOUCH_SCALE_FACTOR: Float = 180.0f / 320f

class MaggotsSurfaceView(context: Context) : GLSurfaceView(context), GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
    private val renderer: GameRenderer
    private var mDetector: GestureDetectorCompat

    init {
        setEGLContextClientVersion(2)
        renderer = GameRenderer(context)
        setRenderer(renderer)
        mDetector = GestureDetectorCompat(context, this)

        // Render the view only when there is a change in the drawing data
        renderMode = RENDERMODE_WHEN_DIRTY
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (mDetector.onTouchEvent(event)) {
            true
        } else {
            when(event.action) {
                MotionEvent.ACTION_MOVE -> {
                    renderer.mTriangle.angle += renderer.mTriangle.move(event.x, event.y, height, width)
                    requestRender()
                }
            }
            return true
        }

    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean { return false }
    override fun onShowPress(e: MotionEvent?) { }
    override fun onDown(event: MotionEvent?): Boolean { return false }
    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean { return false }
    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean { return false }
    override fun onLongPress(e: MotionEvent?) { }
    override fun onDoubleTap(e: MotionEvent?): Boolean {
        Log.d(DEBUG_TAG, "doubleTap")
        return false
    }

    override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
        Log.d(DEBUG_TAG, "doubleTapEvent")
        renderer.camera.eyeZ += 0.1f
        requestRender()
        return true
    }

    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
        Log.d(DEBUG_TAG, "singleTapConfirmed")
        renderer.camera.eyeZ -= 0.1f
        requestRender()
        return onSingleTapUp(e)
    }
}