package com.example.runandremember

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Hour(
    val id: Int,
    val hourtime: String,
    val sportId: Int
) : Parcelable
