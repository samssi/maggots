package fi.maggots.renderer

import android.content.Context
import android.opengl.GLES20
import java.io.InputStream
import java.nio.charset.Charset

fun InputStream.readTextAndClose(charset: Charset = Charsets.UTF_8): String {
    return this.bufferedReader(charset).use { it.readText() }
}

internal const val shaderAssetsDirectory = "shader/vertex";
internal const val fragmentAssetsDirectory = "shader/fragment"


internal fun openShader(context: Context, filepath: String): String {
    return context.assets.open(filepath).readTextAndClose()
}

internal fun loadShader(context: Context, type: Int, shaderPath: String): Int {
    val shaderCode = openShader(context, shaderPath)
    return GLES20.glCreateShader(type).also {
            shader ->
        GLES20.glShaderSource(shader, shaderCode)
        GLES20.glCompileShader(shader)
    }
}