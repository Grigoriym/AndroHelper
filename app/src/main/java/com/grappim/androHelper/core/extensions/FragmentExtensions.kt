package com.grappim.androHelper.core.extensions

import androidx.fragment.app.Fragment

fun Fragment.showToast(message: String?, long: Boolean = true) =
    this.requireContext().showToast(
        message,
        long
    )