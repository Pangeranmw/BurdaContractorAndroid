//package ir.reza_mahmoudi.locationtracker.presentation
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import dagger.hilt.android.lifecycle.HiltViewModel
//import ir.reza_mahmoudi.locationtracker.domain.entity.LocationHistoryItem
//import ir.reza_mahmoudi.locationtracker.domain.usecase.AddLocationHistoryItemUseCase
//import ir.reza_mahmoudi.locationtracker.domain.usecase.GetLocationHistoryListUseCase
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class MainViewModel @Inject constructor(
//    private val addLocationHistoryItemUseCase: AddLocationHistoryItemUseCase,
//    private val getLocationHistoryListUseCase: GetLocationHistoryListUseCase,
//    ) : ViewModel() {
//
//    private val _locationHistoryList =
//        MutableStateFlow<List<LocationHistoryItem>>(emptyList())
//    val locationHistoryList: StateFlow<List<LocationHistoryItem>>
//        get() = _locationHistoryList
//
//    private fun getLocationHistoryList() {
//        viewModelScope.launch {
//            getLocationHistoryListUseCase().collect {
//                _locationHistoryList.emit(it.reversed())
//            }
//        }
//    }
//
//    fun addLocationHistory(item: LocationHistoryItem) {
//        viewModelScope.launch {
//            addLocationHistoryItemUseCase(item)
//            getLocationHistoryList()
//        }
//    }
//
//}