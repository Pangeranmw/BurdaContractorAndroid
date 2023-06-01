package com.android.burdacontractor.feature.auth.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserLogin(
    val id: String,
    val role: String,
    val nama: String,
    val foto: String? = null,
    val noHp: String,
    val ttd: String? = null,
    val email: String,
    val token: String,
    val createdAt: Long,
    val updatedAt: Long,
): Parcelable