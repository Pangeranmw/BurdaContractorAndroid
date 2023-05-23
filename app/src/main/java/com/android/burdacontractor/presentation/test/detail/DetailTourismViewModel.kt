package com.android.burdacontractor.presentation.test.detail

import androidx.lifecycle.ViewModel
import com.android.burdacontractor.core.domain.model.Tourism
import com.android.burdacontractor.core.domain.usecase.TourismUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@HiltViewModel
class DetailTourismViewModel @Inject constructor(private val tourismUseCase: TourismUseCase) : ViewModel() {
    fun setFavoriteTourism(tourism: Tourism, newStatus:Boolean) =
        tourismUseCase.setFavoriteTourism(tourism, newStatus)
}

