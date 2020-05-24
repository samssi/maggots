package fi.maggots.objects3d

import android.content.Context
import android.opengl.GLES20
import fi.maggots.renderer.COORDINATES_PER_VERTEX
import fi.maggots.renderer.loadShader
import fi.maggots.renderer.shaderFileAndType
import fi.maggots.view.TOUCH_SCALE_FACTOR
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

abstract class OpenGlObject {
    internal var positionHandle: Int = 0
    internal val vertexStride: Int = COORDINATES_PER_VERTEX * 4 // 4 bytes per vertex

    abstract fun vertexShader(context: Context): Int
    abstract fun fragmentShader(context: Context): Int
    abstract fun program(context: Context): Int
    abstract fun coordinates(): FloatArray
}

class Triangle(context: Context): OpenGlObject() {
    @Volatile
    internal var angle: Float = 0f
    @Volatile
    internal var x: Float = 0f
    @Volatile
    internal var y: Float = 0f

    private var mColorHandle: Int = 0

    // Set color with red, green, blue and alpha (opacity) values
    private val color = floatArrayOf(0.63671875f, 0.76953125f, 0.22265625f, 1.0f)

    internal var mProgram: Int = program(context)

    internal val vertexCount: Int = coordinates().size / COORDINATES_PER_VERTEX

    private var vertexBuffer: FloatBuffer =
        // (number of coordinate values * 4 bytes per float)
        ByteBuffer.allocateDirect(coordinates().size * 4).run {
            // use the device hardware's native byte order
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(coordinates())
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

    internal fun draw() {
        GLES20.glUseProgram(mProgram)
        positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition").also {
            GLES20.glEnableVertexAttribArray(it)

            GLES20.glVertexAttribPointer(
                it,
                COORDINATES_PER_VERTEX,
                GLES20.GL_FLOAT,
                false,
                vertexStride,
                vertexBuffer
            )

            // Here you assign the color for default.frag glsl see vColor
            mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor").also {
                colorHandle ->
                    GLES20.glUniform4fv(colorHandle, 1, color, 0)
                    GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)
                    GLES20.glDisableVertexAttribArray(it)
            }
        }
    }

    override fun vertexShader(context: Context): Int {
        return loadShader(context, shaderFileAndType("default.vert"))
    }

    override fun fragmentShader(context: Context): Int {
        return loadShader(context, shaderFileAndType("default.frag"))
    }

    override fun program(context: Context): Int {
        return GLES20.glCreateProgram().also {
            GLES20.glAttachShader(it, vertexShader(context))
            GLES20.glAttachShader(it, fragmentShader(context))
            GLES20.glLinkProgram(it)
        }
    }

    override fun coordinates(): FloatArray {
        return floatArrayOf(
            // in counterclockwise order:
            0.0f, 0.5f, 0.0f,      // top
            -0.5f, -0.5f, 0.0f,    // bottom left
            0.5f, -0.5f, 0.0f      // bottom right
        )
    }
}