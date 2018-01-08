package com.melnykov.mvvmtesting.testutil

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

class SingleFragmentActivity : AppCompatActivity() {

    fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .add(android.R.id.content, fragment)
                .commit()
    }
}