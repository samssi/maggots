package fi.maggots

import android.opengl.GLSurfaceView
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import fi.maggots.view.MaggotsSurfaceView

class MainActivity : AppCompatActivity() {
    private lateinit var glView: GLSurfaceView


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        glView = MaggotsSurfaceView(this)
        setContentView(glView)
    }
}

