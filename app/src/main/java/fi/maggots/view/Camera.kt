package fi.maggots.view

const val ZOOM_MAX = 1.1f
const val ZOOM_MIN = 0.05f

data class Camera(
    var rm: FloatArray,
    var rmOffSet: Int,
    var eyeX: Float,
    var eyeY: Float,
    var eyeZ: Float,
    var centerX: Float,
    var centerY: Float,
    var centerZ: Float,
    var upX: Float,
    var upY: Float,
    var upZ: Float,
    var zoom: Float
)