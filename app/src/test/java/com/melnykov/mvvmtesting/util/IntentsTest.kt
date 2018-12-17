package com.melnykov.mvvmtesting.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class IntentsTest {

    @Mock
    private lateinit var context: Context

    @Mock
    private lateinit var intent: Intent

    @Mock
    private lateinit var packageManager: PackageManager

    @Mock
    private lateinit var resolveInfo: ResolveInfo

    @Test
    fun isAvailableReturnsFalse_WhenIntentActivitiesNotFound() {
        mockQueryIntentActivities(false)
        assertThat(context.isIntentAvailable(intent), `is`(false))
    }

    @Test
    fun isAvailableReturnsTrue_WhenIntentActivitiesFound() {
        mockQueryIntentActivities(true)
        assertThat(context.isIntentAvailable(intent), `is`(true))
    }

    private fun mockQueryIntentActivities(hasMatchingActivities: Boolean) {
        `when`(packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY))
            .thenReturn(if (hasMatchingActivities) listOf(resolveInfo) else emptyList())
        `when`(context.packageManager).thenReturn(packageManager)
    }
}