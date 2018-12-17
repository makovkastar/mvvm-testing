package com.melnykov.mvvmtesting.data.local.dao

import com.melnykov.mvvmtesting.data.model.User
import com.melnykov.mvvmtesting.testutil.DatabaseTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.Test

class UserDaoTest : DatabaseTest() {

    @Test
    fun insertUserAndGetUserByUsername() {
        val user = User("makovkastar", "Oleksandr", "Melnykov", "https://github.com/makovkastar")

        db.userDao().insert(user)

        val userByUsername = db.userDao().getByUsername("makovkastar")
        assertThat(userByUsername, equalTo(user))
    }

    @Test
    fun insertUserReplacesExistingUser() {
        val user1 = User("makovkastar", "Oleksandr", "Melnykov", "https://github.com/makovkastar")
        val user2 = User("makovkastar", "John", "Doe", "https://github.com/johndoe")

        db.userDao().insert(user1)
        db.userDao().insert(user2)

        val userByUsername = db.userDao().getByUsername("makovkastar")
        assertThat(userByUsername, equalTo(user2))
    }
}
