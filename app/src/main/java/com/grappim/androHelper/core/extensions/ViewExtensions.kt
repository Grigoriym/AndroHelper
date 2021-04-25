package com.grappim.androHelper.core.extensions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

fun ViewGroup.inflate(@LayoutRes layoutId: Int): View =
    LayoutInflater.from(context).inflate(layoutId, this, false)

fun View.disable() {
    this.isClickable = false
    this.isEnabled = false
}

fun View.enable() {
    this.isClickable = true
    this.isEnabled = true
}

fun View.enableOrDisable(value: Boolean) {
    if (value) {
        this.enable()
    } else {
        this.disable()
    }
}

fun View.gone(): View {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
    return this
}

fun View.hide(): View {
    if (visibility != View.INVISIBLE) {
        visibility = View.INVISIBLE
    }
    return this
}

fun View.show(): View {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
    return this
}

fun View.showOrGone(show: Boolean) {
    visibility = if (show) {
        View.VISIBLE
    } else {
        View.GONE
    }
}