package fi.maggots

import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import fi.maggots.renderer.GameRenderer

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val view = GLSurfaceView(this)
        view.setRenderer(GameRenderer())
        setContentView(view)
    }
}
