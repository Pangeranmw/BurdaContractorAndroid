package com.android.burdacontractor.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User (
    val id: String,
    val nama: String,
    val email: String,
    val foto: String? = null,
    val ttd: String? = null,
    val noHp: String? = null,
    val role: String,
    val deviceToken: String? = null,
    val createdAt: Long,
    val updatedAt: Long,
): Parcelable