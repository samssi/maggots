package fi.maggots.util

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

internal fun createFloatBuffer(triangleCoordinates: FloatArray): FloatBuffer {
    return createByteBuffer(triangleCoordinates).apply {
        put(triangleCoordinates)
        position(0)
    }
}

internal fun createByteBuffer(floatArray: FloatArray): FloatBuffer {
    // TODO: direct vs non-direct. Direct won't be GC:d but is faster
    val byteBuffer = ByteBuffer.allocateDirect(floatArrayByteAllocationSize(floatArray.size)).run {
        order(ByteOrder.nativeOrder())
    }
    return byteBuffer.asFloatBuffer()
}

internal fun floatArrayByteAllocationSize(arrayLength: Int): Int {
    return arrayLength * 4;
}