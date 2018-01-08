package com.melnykov.mvvmtesting.data.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class User(@PrimaryKey @ColumnInfo(name = "username") @SerializedName("username") val username: String,
                @ColumnInfo(name = "first_name") @SerializedName("first_name") val firstName: String,
                @ColumnInfo(name = "last_name") @SerializedName("last_name") val lastName: String,
                @ColumnInfo(name = "url") @SerializedName("url") val url: String)
