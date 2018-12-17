package com.melnykov.mvvmtesting.injection

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import com.melnykov.mvvmtesting.MvvmTestingApplication
import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector

object Injector {

    fun init(application: MvvmTestingApplication) {
        application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity?) {
            }

            override fun onActivityResumed(activity: Activity?) {
            }

            override fun onActivityStarted(activity: Activity?) {
            }

            override fun onActivityDestroyed(activity: Activity?) {
            }

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
            }

            override fun onActivityStopped(activity: Activity?) {
            }

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                if (activity is HasSupportFragmentInjector && activity is Injectable) {
                    AndroidInjection.inject(activity)
                }
                if (activity is FragmentActivity) {
                    activity.supportFragmentManager.registerFragmentLifecycleCallbacks(
                        object : FragmentManager.FragmentLifecycleCallbacks() {
                            override fun onFragmentPreAttached(fm: FragmentManager, f: Fragment, context: Context) {
                                if (f is Injectable) {
                                    AndroidSupportInjection.inject(f)
                                }
                            }
                        }, true)
                }
            }
        })
    }
}
