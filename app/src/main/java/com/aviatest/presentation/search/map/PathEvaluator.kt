package com.aviatest.presentation.search.map

import android.graphics.PointF
import androidx.annotation.FloatRange

interface PathEvaluator {
    fun evaluate(@FloatRange(from = 0.0, to = 1.0) progress: Float): PointF
}