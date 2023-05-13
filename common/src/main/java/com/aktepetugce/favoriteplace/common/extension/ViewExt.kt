package com.aktepetugce.favoriteplace.common.extension

import android.os.SystemClock
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar

fun View.onClick(debounceDuration: Long = 300L, action: (View) -> Unit) {
    setOnClickListener(
        DebouncedOnClickListener(debounceDuration) {
            action(it)
        }
    )
}

private class DebouncedOnClickListener(
    private val debounceDuration: Long,
    private val clickAction: (View) -> Unit
) : View.OnClickListener {

    private var lastClickTime: Long = 0

    override fun onClick(v: View) {
        val now = SystemClock.elapsedRealtime()
        if (now - lastClickTime >= debounceDuration) {
            lastClickTime = now
            clickAction(v)
        }
    }
}

fun View.gone() {
    this.isGone = true
}

fun View.visible() {
    this.isVisible = true
}

fun View.showSnackbar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
}
