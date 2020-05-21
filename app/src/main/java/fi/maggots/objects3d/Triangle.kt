package fi.maggots.objects3d

import android.content.Context
import android.opengl.GLES20
import fi.maggots.renderer.loadShader
import fi.maggots.renderer.shaderFileAndType
import fi.maggots.view.TOUCH_SCALE_FACTOR
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

const val COORDS_PER_VERTEX = 3

var triangleCoords = floatArrayOf(
    // in counterclockwise order:
    0.0f, 0.5f, 0.0f,      // top
    -0.5f, -0.5f, 0.0f,    // bottom left
    0.5f, -0.5f, 0.0f      // bottom right
)

class Triangle(context: Context) {
    // Use to access and set the view transformation
    private var vPMatrixHandle: Int = 0

    private var mProgram: Int

    @Volatile
    var angle: Float = 0f
    @Volatile
    var x: Float = 0f
    @Volatile
    var y: Float = 0f

    init {
        val vertexShader: Int = loadShader(context, shaderFileAndType("default.vert"))
        val fragmentShader: Int = loadShader(context, shaderFileAndType("default.frag"))

        mProgram = GLES20.glCreateProgram().also {
            GLES20.glAttachShader(it, vertexShader)
            GLES20.glAttachShader(it, fragmentShader)
            GLES20.glLinkProgram(it)
        }
    }

    // Set color with red, green, blue and alpha (opacity) values
    private val color = floatArrayOf(0.63671875f, 0.76953125f, 0.22265625f, 1.0f)

    private var vertexBuffer: FloatBuffer =
        // (number of coordinate values * 4 bytes per float)
        ByteBuffer.allocateDirect(triangleCoords.size * 4).run {
            // use the device hardware's native byte order
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(triangleCoords)
                position(0)
            }
        }

    private var positionHandle: Int = 0
    private var mColorHandle: Int = 0
    private val vertexCount: Int = triangleCoords.size / COORDS_PER_VERTEX
    private val vertexStride: Int = COORDS_PER_VERTEX * 4 // 4 bytes per vertex

    internal fun draw(mvpMatrix: FloatArray) { // pass in the calculated transformation matrix
        drawShapes()
        attachCamera(mvpMatrix)
    }

    private fun attachCamera(mvpMatrix: FloatArray) {
        // get handle to shape's transformation matrix
        vPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix")

        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, mvpMatrix, 0)

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(positionHandle)
    }

    private fun drawShapes() {
        GLES20.glUseProgram(mProgram)
        positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition").also {
            GLES20.glEnableVertexAttribArray(it)

            GLES20.glVertexAttribPointer(
                it,
                COORDS_PER_VERTEX,
                GLES20.GL_FLOAT,
                false,
                vertexStride,
                vertexBuffer
            )

            mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor").also {
                    colorHandle -> GLES20.glUniform4fv(colorHandle, 1, color, 0)
                GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)
                GLES20.glDisableVertexAttribArray(it)
            }
        }
    }

    internal fun move(triangle: Triangle, x: Float, y: Float, height: Int, width: Int): Float {
        var directionX: Float = x - triangle.x
        var directionY: Float = y - triangle.y

        // reverse direction of rotation above the mid-line
        if (y > height / 2) {
            directionX *= -1
        }

        // reverse direction of rotation to left of the mid-line
        if (x < width / 2) {
            directionY *= -1
        }

        triangle.x = x
        triangle.y = y

        return (directionX + directionY) * TOUCH_SCALE_FACTOR
    }
}