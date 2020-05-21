package fi.maggots.view

import fi.maggots.objects3d.Triangle

internal fun move(triangle: Triangle, x: Float, y: Float, height: Int, width: Int): Float {
    var directionX: Float = x - triangle.x
    var directionY: Float = y - triangle.y

    // reverse direction of rotation above the mid-line
    if (y > height / 2) {
        directionX *= -1
    }

    // reverse direction of rotation to left of the mid-line
    if (x < width / 2) {
        directionY *= -1
    }

    triangle.x = x
    triangle.y = y

    return (directionX + directionY) * TOUCH_SCALE_FACTOR
}