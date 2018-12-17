package com.melnykov.mvvmtesting.util

import android.databinding.BindingAdapter
import android.view.View

@BindingAdapter("android:visibility")
fun setVisibility(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}
