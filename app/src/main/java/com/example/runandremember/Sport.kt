package com.example.runandremember

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Sport(
    val id: Int,
    val image: String?,
    val name: String,
    val description: String,
    val time: String,
    val day: String,
    val usuaId: Int
) : Parcelable
