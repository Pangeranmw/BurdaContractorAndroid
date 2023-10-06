package com.android.burdacontractor.feature.profile.domain.usecase

import com.android.burdacontractor.feature.deliveryorder.domain.repository.IDeliveryOrderRepository
import com.android.burdacontractor.feature.profile.domain.repository.IUserRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class UploadTtdInteractor @Inject constructor(private val userRepository: IUserRepository):
    UploadTtdUseCase
{
    override suspend fun execute(
        ttd: MultipartBody.Part
    )=userRepository.uploadTtd(ttd)
}