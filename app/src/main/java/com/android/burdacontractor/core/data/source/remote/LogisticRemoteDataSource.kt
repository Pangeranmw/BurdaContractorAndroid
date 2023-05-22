package com.android.burdacontractor.core.data.source.remote

import android.util.Log
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.local.storage.SessionManager
import com.android.burdacontractor.core.data.source.remote.network.AksesBarangService
import com.android.burdacontractor.core.data.source.remote.network.ApiResponse
import com.android.burdacontractor.core.data.source.remote.network.ApiService
import com.android.burdacontractor.core.data.source.remote.network.AuthService
import com.android.burdacontractor.core.data.source.remote.network.BarangService
import com.android.burdacontractor.core.data.source.remote.network.DeliveryOrderService
import com.android.burdacontractor.core.data.source.remote.network.GudangService
import com.android.burdacontractor.core.data.source.remote.network.KendaraanService
import com.android.burdacontractor.core.data.source.remote.network.PeminjamanService
import com.android.burdacontractor.core.data.source.remote.network.PengembalianService
import com.android.burdacontractor.core.data.source.remote.network.PerusahaanService
import com.android.burdacontractor.core.data.source.remote.network.PreOrderService
import com.android.burdacontractor.core.data.source.remote.network.ProyekService
import com.android.burdacontractor.core.data.source.remote.network.SuratJalanService
import com.android.burdacontractor.core.data.source.remote.network.UserService
import com.android.burdacontractor.core.data.source.remote.response.AddSuratJalanResponse
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.core.data.source.remote.response.SuratJalanDetailItem
import com.android.burdacontractor.core.data.source.remote.response.SuratJalanItem
import com.android.burdacontractor.core.data.source.remote.response2.TourismResponse
import com.android.burdacontractor.core.domain.model.LogisticCoordinate
import com.android.burdacontractor.core.domain.model.enum.SuratJalanStatus
import com.android.burdacontractor.core.domain.model.enum.SuratJalanTipe
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LogisticRemoteDataSource @Inject constructor(
    private var databaseReference: DatabaseReference,
) {
    fun getCoordinate(logisticId: String) = callbackFlow<Resource<LogisticCoordinate>> {
        databaseReference.child(logisticId)
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var logisticCoordinate = LogisticCoordinate(0.0, 0.0)
                if(dataSnapshot.exists()){
                    for (e in dataSnapshot.children){
                        val item = e.getValue(LogisticCoordinate::class.java)
                        if(item != null){
                            logisticCoordinate = LogisticCoordinate(item.latitude, item.longitude)
                        }
                    }
                    this@callbackFlow.trySendBlocking(Resource.Success(logisticCoordinate))
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        databaseReference.addValueEventListener(postListener)

        awaitClose {
            databaseReference.removeEventListener(postListener)
        }
    }

    fun setCoordinate(logisticId: String, coordinate: LogisticCoordinate) {
        databaseReference.child(logisticId).setValue(coordinate)
    }
}

