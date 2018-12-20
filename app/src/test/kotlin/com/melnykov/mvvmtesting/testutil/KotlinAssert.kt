package com.melnykov.mvvmtesting.testutil

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@UseExperimental(ExperimentalContracts::class)
fun assertNotNull(actual: Any?) {
    contract { returns() implies (actual != null) }
    org.junit.Assert.assertNotNull(actual)
}
