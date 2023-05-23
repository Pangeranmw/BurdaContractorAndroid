package com.android.burdacontractor.core.data.source.remote

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.domain.model.LogisticCoordinate
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
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
                var logisticCoordinate = LogisticCoordinate()
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

