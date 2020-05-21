package fi.maggots.renderer

import android.content.Context
import android.opengl.GLES20
import fi.maggots.core.readTextAndClose
import java.io.File

private const val vertexAssetsDirectory = "shader/vertex";
private const val fragmentAssetsDirectory = "shader/fragment"

private fun openShader(context: Context, filepath: String): String {
    return context.assets.open(filepath).readTextAndClose()
}

data class ShaderFile(val filepath: String, val type: Int)

internal fun shaderFileAndType(shaderFile: String): ShaderFile {
    return when (File(shaderFile).extension) {
        "vert" -> ShaderFile("${vertexAssetsDirectory}/${shaderFile}", GLES20.GL_VERTEX_SHADER)
        "frag" -> ShaderFile("${fragmentAssetsDirectory}/${shaderFile}", GLES20.GL_FRAGMENT_SHADER)
        else -> throw UnsupportedOperationException("Unknown shader file given!")
    }
}

internal fun loadShader(context: Context, shaderFile: ShaderFile): Int {
    val shaderCode = openShader(context, shaderFile.filepath)
    return GLES20.glCreateShader(shaderFile.type).also {
        GLES20.glShaderSource(it, shaderCode)
        GLES20.glCompileShader(it)
    }
}