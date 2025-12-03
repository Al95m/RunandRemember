package com.example.runandremember

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Usuario(
    val id: Int,
    val name: String,
    val surname: String,
    val password: String,
    val email: String,
    val height: String,
    val weight: String,
    val birth: String
) : Parcelable
