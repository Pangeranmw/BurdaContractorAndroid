package com.android.burdacontractor.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User (
    val id: String,
    val nama: String,
    val email: String,
    val foto: String,
    val ttd: String,
    val noHp: String,
    val role: String,
    val createdAt: String,
    val updatedAt: String,
): Parcelable