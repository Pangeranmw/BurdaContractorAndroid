package com.android.burdacontractor.presentation.test.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.android.burdacontractor.core.domain.usecase.TourismUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(tourismUseCase: TourismUseCase) : ViewModel() {
    val favoriteTourism = tourismUseCase.getFavoriteTourism().asLiveData()
}

