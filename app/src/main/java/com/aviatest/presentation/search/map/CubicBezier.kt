package com.aviatest.presentation.search.map

import android.graphics.Matrix
import android.graphics.PointF
import androidx.annotation.FloatRange
import kotlin.math.PI
import kotlin.math.atan
import kotlin.math.pow
import kotlin.math.sqrt

class CubicBezier(
    private val p0: PointF,
    private val p3: PointF,
    controlX1: Float,
    controlY1: Float,
    controlX2: Float,
    controlY2: Float
) : PathEvaluator {

    private val p1: PointF
    private val p2: PointF

    init {
        val dx = p3.x - p0.x
        val dy = p3.y - p0.y
        val scale = sqrt(dx.pow(2) + dy.pow(2)) / sqrt(2f)
        val matrix: Matrix = Matrix()
            .apply { setScale(scale, scale) }
            .apply {
                val correctionalAngle = if (dx < 0f) 180f else 0f
                postRotate(atan(dy / dx).radianToDegree() - 45f + correctionalAngle) }
            .apply { postTranslate(p0.x, p0.y) }

        val p13 = floatArrayOf(controlX1, controlY1)
        val p23 = floatArrayOf(controlX2, controlY2)

        p1 = FloatArray(2).also { matrix.mapPoints(it, p13) }.let { PointF(it[0], it[1]) }
        p2 = FloatArray(2).also { matrix.mapPoints(it, p23) }.let { PointF(it[0], it[1]) }
    }

    // X(t) = (1-t)^3 * X0 + 3*(1-t)^2 * t * X1 + 3*(1-t) * t^2 * X2 + t^3 * X3
    // Y(t) = (1-t)^3 * Y0 + 3*(1-t)^2 * t * Y1 + 3*(1-t) * t^2 * Y2 + t^3 * Y3
    override fun evaluate(@FloatRange(from = 0.0, to = 1.0) progress: Float): PointF {
        val x = evaluate(progress, p0.x, p1.x, p2.x, p3.x)
        val y = evaluate(progress, p0.y, p1.y, p2.y, p3.y)
        return PointF(x, y)
    }

    private fun evaluate(
        @FloatRange(from = 0.0, to = 1.0) t: Float,
        x0: Float,
        x1: Float,
        x2: Float,
        x3: Float
    ) = (1 - t).pow(3) * x0 +
            3 * (1 - t).pow(2) * t * x1 +
            3 * (1 - t) * t.pow(2) * x2 +
            t.pow(3) * x3

    companion object {
        fun Float.radianToDegree(): Float = this * 180f / PI.toFloat()
    }
}