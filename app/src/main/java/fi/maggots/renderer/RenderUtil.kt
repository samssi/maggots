package fi.maggots.renderer

import android.opengl.GLES20

internal fun loadShader(type: Int, shaderCode: String): Int {
    return GLES20.glCreateShader(type).also {
            shader ->
        GLES20.glShaderSource(shader, shaderCode)
        GLES20.glCompileShader(shader)
    }
}