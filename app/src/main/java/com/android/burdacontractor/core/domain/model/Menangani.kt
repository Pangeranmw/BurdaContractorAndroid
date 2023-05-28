package com.android.burdacontractor.core.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Menangani(
    var supervisorId: String? = null,
    var proyekId: String? = null,
    var supervisor: Supervisor? = null,
    var proyek: Proyek? = null,
): Parcelable

