package fi.maggots.objects3d

import fi.maggots.util.createByteBuffer
import java.nio.ByteBuffer
import java.nio.FloatBuffer
import javax.microedition.khronos.opengles.GL10

internal class Triangle {
    private val vertexBuffer: FloatBuffer
    private val colorBuffer: FloatBuffer
    private val indexBuffer: ByteBuffer

    private val vertices = floatArrayOf(
        0.0f, 0.5f, 0.0f,
        0.5f, -0.5f, 0.0f,
        -0.5f, -0.5f, 0.0f
    )

    private val colors = floatArrayOf(
        1.0f, 1.0f, 1.0f, 1.0f
    )

    private val indices = byteArrayOf(
        0, 1, 2
    )

    fun draw(gl: GL10) {
        // Sets front facing polygons CW -> clock wise
        gl.glFrontFace(GL10.GL_CW)
        // Pointer size is dependant on dimensions eg. 2 => (x,y), 3 => (x,y,z)
        gl.glVertexPointer(2, GL10.GL_FLOAT, 0, vertexBuffer)
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer)
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY)
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY)
        gl.glDrawElements(
            GL10.GL_TRIANGLES, 3, GL10.GL_UNSIGNED_BYTE, indexBuffer
        )
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY)
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY)
    }

    init {
        vertexBuffer = createByteBuffer(vertices)
        colorBuffer = createByteBuffer(colors)
        indexBuffer = ByteBuffer.allocateDirect(indices.size)
        indexBuffer.put(indices)
        indexBuffer.position(0)
    }
}