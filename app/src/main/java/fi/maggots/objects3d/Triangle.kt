package fi.maggots.objects3d

import android.content.Context
import fi.maggots.renderer.COORDINATES_PER_VERTEX
import fi.maggots.view.TOUCH_SCALE_FACTOR
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class Triangle(context: Context) {
    @Volatile
    var angle: Float = 0f
    @Volatile
    var x: Float = 0f
    @Volatile
    var y: Float = 0f

    private var coordinates = floatArrayOf(
        // in counterclockwise order:
        0.0f, 0.5f, 0.0f,      // top
        -0.5f, -0.5f, 0.0f,    // bottom left
        0.5f, -0.5f, 0.0f      // bottom right
    )

    internal val vertexCount: Int = coordinates.size / COORDINATES_PER_VERTEX

    internal var vertexBuffer: FloatBuffer =
        // (number of coordinate values * 4 bytes per float)
        ByteBuffer.allocateDirect(coordinates.size * 4).run {
            // use the device hardware's native byte order
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(coordinates)
                position(0)
            }
        }

    internal fun move(newX: Float, newY: Float, height: Int, width: Int): Float {
        var directionX: Float = newX - x
        var directionY: Float = newY - y

        // reverse direction of rotation above the mid-line
        if (newY > height / 2) {
            directionX *= -1
        }

        // reverse direction of rotation to left of the mid-line
        if (newX < width / 2) {
            directionY *= -1
        }

        x = newX
        y = newY

        return (directionX + directionY) * TOUCH_SCALE_FACTOR
    }
}