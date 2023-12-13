package com.android.burdacontractor.feature.profile.domain.usecase

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.android.burdacontractor.feature.profile.domain.model.User

interface GetAllUsersUseCase {
    fun execute(
        size: Int = 5,
        search: String? = null,
        filter: String? = null,
    ): LiveData<PagingData<User>>
}