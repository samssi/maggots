package fi.maggots.view

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.core.view.GestureDetectorCompat
import fi.maggots.core.PointsXY
import fi.maggots.core.pythagoreanDistance
import fi.maggots.renderer.GameRenderer
import fi.maggots.util.DEBUG_TAG
import kotlin.math.abs

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

    private fun calculateNewZoom(previousDistance: Float, currentDistance: Float, zoomFactor: Float) {
        val newZoomValue = renderer.camera.zoom + zoomFactor
        if(newZoomValue > 0.05f && newZoomValue < 1.1f) {
            renderer.camera.zoom = newZoomValue
        }
    }

    private fun zoom(previousDistance: Float, currentDistance: Float) {
        if (currentDistance < previousDistance) {
            calculateNewZoom(previousDistance, currentDistance, -0.1f)
        }
        else if (currentDistance > previousDistance) {
            calculateNewZoom(previousDistance, currentDistance, 0.1f)
        }
        requestRender()
    }

    private var previousDistance = -1f

    private fun moveEvent(event: MotionEvent): Boolean {
        return when (event.pointerCount) {
            1 -> {
                renderer.mTriangle.angle += renderer.mTriangle.move(event.x, event.y, height, width)
                requestRender()
                true
            }
            2 -> {
                val point1 = PointsXY(event.getX(0), event.getY(0))
                val point2 = PointsXY(event.getX(1), event.getY(1))
                val currentDistance = pythagoreanDistance(point1, point2)

                if (previousDistance > -1f) {
                    zoom(previousDistance, currentDistance)
                }
                previousDistance = currentDistance
                true
            }
            else -> {
                false
            }
        }
    }

    // TODO: https://developer.android.com/training/gestures/scale
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (mDetector.onTouchEvent(event)) {
            true
        } else {
            when(event.action) {
                MotionEvent.ACTION_MOVE -> {
                    return moveEvent(event)
                }
                MotionEvent.ACTION_UP -> {
                    previousDistance = -1f;

                }
            }
            return true
        }

    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean { return false }
    override fun onShowPress(e: MotionEvent?) { }
    override fun onDown(event: MotionEvent?): Boolean {
        return false
    }
    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean { return false }
    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean { return false }
    override fun onLongPress(e: MotionEvent?) {
        /*Log.d(DEBUG_TAG, "longPress")
        renderer.camera.centerX -= 0.1f
        renderer.camera.eyeX -= 0.1f
        requestRender()*/
    }
    override fun onDoubleTap(e: MotionEvent?): Boolean {
        Log.d(DEBUG_TAG, "doubleTap")
        return false
    }

    override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
        /*Log.d(DEBUG_TAG, "doubleTapEvent")
        renderer.camera.eyeZ += 0.1f
        requestRender()
        return true*/
        return false
    }

    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
        /*Log.d(DEBUG_TAG, "singleTapConfirmed")
        renderer.camera.eyeZ -= 0.1f
        requestRender()
        return onSingleTapUp(e)*/
        return false
    }
}

