package com.melnykov.mvvmtesting.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v4.app.Fragment
import com.melnykov.mvvmtesting.R

fun Activity.startBrowser(url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    if (isIntentAvailable(intent)) {
        startActivity(intent)
    } else {
        showToast(R.string.general_toast_no_application_available)
    }
}

fun Fragment.startBrowser(url: String) = activity?.startBrowser(url)

fun Context.isIntentAvailable(intent: Intent): Boolean {
    val activities = packageManager.queryIntentActivities(
            intent, PackageManager.MATCH_DEFAULT_ONLY)
    return activities.size > 0
}
