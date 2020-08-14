package com.aviatest.coreui.extentions

import androidx.fragment.app.Fragment

inline fun <reified T> Fragment.getCallbackOrThrow(): T =
        getCallback<T>() ?: throw IllegalArgumentException("Callback not found: ${T::class}")

inline fun <reified T> Fragment.getCallback(): T? =
        targetFragment as? T
                ?: parentFragment as? T
                ?: requireActivity() as? T

fun Fragment.hideSoftKeyboard() {
        activity?.hideSoftKeyboard()
}