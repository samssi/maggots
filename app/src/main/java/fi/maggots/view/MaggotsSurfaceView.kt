package fi.maggots.view

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.MotionEventCompat
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

    private fun zoomDirection(prevAY: Float, aY: Float, bY: Float, prevBY: Float) {
        // TODO: won't work always because both fingers must move in the same event time
        // TODO: calculate what happens to distance between A and B touch instead
        Log.d(DEBUG_TAG, "A: prev: $prevAY, current: $aY")
        Log.d(DEBUG_TAG, "B: prev: $prevBY, current: $bY")
        if(aY < prevAY && bY < prevBY) {
            Log.d(DEBUG_TAG, "ZOOM IN")
            renderer.camera.eyeX -= 0.1f
        }
        else if (aY > prevAY && bY > prevBY) {
            Log.d(DEBUG_TAG, "ZOOM OUT")
            renderer.camera.eyeX += 0.1f
        }
        requestRender()
    }

    private var prevA = 0f
    private var prevB = 0f

    private fun moveEvent(event: MotionEvent): Boolean {
        Log.d(DEBUG_TAG,"ACTION_MOVE, x: ${event.x}, y: ${event.y}")
        Log.d(DEBUG_TAG, "ACTION_MOVE, pointer count: ${event.pointerCount}");
        return when (event.pointerCount) {
            1 -> {
                renderer.mTriangle.angle += renderer.mTriangle.move(event.x, event.y, height, width)
                requestRender()
                true
            }
            2 -> {
                Log.d(DEBUG_TAG,"ACTION_MOVE pointer 1, x: ${event.getX(0)}, y: ${event.getY(0)}")
                Log.d(DEBUG_TAG,"ACTION_MOVE pointer 2, x: ${event.getX(1)}, y: ${event.getY(1)}")
                if (prevA != 0f && prevB != 0f) {
                    zoomDirection(prevA, event.getY(0), prevB, event.getY(1))
                }
                prevA = event.getY(0)
                prevB = event.getY(1)
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
                MotionEvent.ACTION_DOWN -> {
                    Log.d(DEBUG_TAG,"ACTION_DOWN, x: ${event.x}, y: ${event.y}")

                }
                MotionEvent.ACTION_UP -> {
                    prevA = 0f
                    prevB = 0f
                }
            }
            return true
        }

    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean { return false }
    override fun onShowPress(e: MotionEvent?) { }
    override fun onDown(event: MotionEvent?): Boolean {
        Log.d(DEBUG_TAG, "onDown")
        return false
    }
    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean { return false }
    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean { return false }
    override fun onLongPress(e: MotionEvent?) {
        Log.d(DEBUG_TAG, "longPress")
        renderer.camera.centerX -= 0.1f
        renderer.camera.eyeX -= 0.1f
        requestRender()
    }
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

