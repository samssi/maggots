package fi.maggots.renderer

import android.content.Context
import android.opengl.GLES20
import fi.maggots.core.readTextAndClose

private const val vertexAssetsDirectory = "shader/vertex";
private const val fragmentAssetsDirectory = "shader/fragment"

private fun openShader(context: Context, filepath: String): String {
    return context.assets.open(filepath).readTextAndClose()
}

internal fun shaderFile(shaderFile: String, type: Int): String {
    return when (type) {
        GLES20.GL_VERTEX_SHADER -> "${vertexAssetsDirectory}/${shaderFile}"
        GLES20.GL_FRAGMENT_SHADER -> "${fragmentAssetsDirectory}/${shaderFile}"
        else -> throw UnsupportedOperationException("Unknown shadertype given!")
    }
}

internal fun loadShader(context: Context, type: Int, shaderPath: String): Int {
    val shaderCode = openShader(context, shaderPath)
    return GLES20.glCreateShader(type).also {
            shader ->
        GLES20.glShaderSource(shader, shaderCode)
        GLES20.glCompileShader(shader)
    }
}