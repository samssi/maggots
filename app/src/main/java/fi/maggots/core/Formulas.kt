package fi.maggots.core

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

data class PointsXY(val x: Float, val y: Float)

internal fun pythagoreanDistance(point1: PointsXY, point2: PointsXY): Float {
    val aPow2 = (point2.x - point1.x).pow(2)
    val bPow2 = (point2.y - point2.y).pow(2)
    val aPlusB = aPow2 + bPow2
    return abs(sqrt(aPlusB))
}