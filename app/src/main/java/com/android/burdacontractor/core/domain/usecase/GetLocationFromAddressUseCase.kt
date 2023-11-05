package com.android.burdacontractor.core.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.remote.response.GetLocationFromAddressResponseItem
import kotlinx.coroutines.flow.Flow

interface GetLocationFromAddressUseCase {
    suspend fun execute(q: String): Flow<Resource<List<GetLocationFromAddressResponseItem>>>
}