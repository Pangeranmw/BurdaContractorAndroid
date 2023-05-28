package com.android.burdacontractor.core.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProjectManager(
    var userId: String? = null,
    var kodePm: String? = null,
    var user: User? = null,
): Parcelable

