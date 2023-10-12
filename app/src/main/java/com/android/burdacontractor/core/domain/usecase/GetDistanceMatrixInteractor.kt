package com.android.burdacontractor.core.domain.usecase

import com.android.burdacontractor.core.data.LogisticRepository
import com.android.burdacontractor.core.data.OSRMRepository
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.remote.response.DistanceMatrixResponse
import com.android.burdacontractor.core.domain.model.LogisticCoordinate
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDistanceMatrixInteractor @Inject constructor(private val osrmRepository: OSRMRepository):
    GetDistanceMatrixUseCase {
    override suspend fun execute(coordinates: String): Flow<Resource<DistanceMatrixResponse>> = osrmRepository.getDistanceMatrix(coordinates)
}