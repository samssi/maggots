package fi.maggots

import android.content.Context
import android.opengl.GLSurfaceView
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import fi.maggots.renderer.GameRenderer

class MaggotsSurfaceView(context: Context) : GLSurfaceView(context) {
    private val renderer: GameRenderer

    init {
        setEGLContextClientVersion(2)
        renderer = GameRenderer()
        setRenderer(renderer)
        renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
    }
}

class MainActivity : AppCompatActivity() {
    private lateinit var glView: GLSurfaceView

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        glView = MaggotsSurfaceView(this)
        setContentView(glView)
    }
}

