package com.example.runandremember

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Planning(
    val id: Int,
    val descplan: String,
    val sportId: Int
) : Parcelable
