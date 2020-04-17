package fi.maggots.objects3d

import android.opengl.GLES20
import fi.maggots.renderer.loadShader
import fi.maggots.util.createFloatBuffer
import java.nio.FloatBuffer

const val COORDINATES_PER_VERTEX = 3

// https://developer.android.com/training/graphics/opengl/shapes
internal class Triangle2 {
    private var program: Int

    private val triangleCoordinates = floatArrayOf(
        0.0f, 0.5f, 0.0f,
        0.5f, -0.5f, 0.0f,
        -0.5f, -0.5f, 0.0f
    )

    private val color = floatArrayOf(
        0.63671875f, 0.76953125f, 0.22265625f, 1.0f
    )

    private var vertexBuffer: FloatBuffer = createFloatBuffer(triangleCoordinates)

    private val vertexShaderCode =
        "attribute vec4 vPosition;" +
                "void main() {" +
                "  gl_Position = vPosition;" +
                "}"

    private val fragmentShaderCode =
        "precision mediump float;" +
                "uniform vec4 vColor;" +
                "void main() {" +
                "  gl_FragColor = vColor;" +
                "}"

    init {
        val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        program = GLES20.glCreateProgram().also {
            GLES20.glAttachShader(it, vertexShader)
            GLES20.glAttachShader(it, fragmentShader)
            GLES20.glLinkProgram(it)
        }
    }

    private var positionHandle: Int = 0
    private var mColorHandle: Int = 0

    private val vertexCount: Int = triangleCoordinates.size / COORDINATES_PER_VERTEX
    private val vertexStride: Int = COORDINATES_PER_VERTEX * 4

    internal fun draw() {
        GLES20.glUseProgram(program)

        positionHandle = GLES20.glGetAttribLocation(program, "vPosition").also {
            GLES20.glEnableVertexAttribArray(it)
            GLES20.glVertexAttribPointer(
                it,
                COORDINATES_PER_VERTEX,
                GLES20.GL_FLOAT,
                false,
                vertexStride,
                vertexBuffer
            )

            mColorHandle = GLES20.glGetAttribLocation(program, "vColor").also {
                colorHandle -> GLES20.glUniform4fv(colorHandle, 1, color, 0)
            }

            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)
            GLES20.glDisableVertexAttribArray(it)
        }
    }
}