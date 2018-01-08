package com.melnykov.mvvmtesting.util

import android.app.Activity
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.widget.Toast

fun Activity.showToast(@StringRes messageRes: Int) = Toast.makeText(this, messageRes, Toast.LENGTH_SHORT).show()

fun Fragment.showToast(@StringRes messageRes: Int) = activity.showToast(messageRes)
