package com.android.burdacontractor.feature.kendaraan.domain.usecase

import com.android.burdacontractor.feature.kendaraan.domain.repository.IKendaraanRepository
import javax.inject.Inject

class GetKendaraanByLogisticInteractor @Inject constructor(private val kendaraanRepository: IKendaraanRepository):
    GetKendaraanByLogisticUseCase
{
    override suspend fun execute()= kendaraanRepository.getKendaraanByLogistic()
}