package com.aviatest.coreui.extentions

import android.content.res.Resources

// dp to pixels
val Int.dp: Float get() = this * Resources.getSystem().displayMetrics.density