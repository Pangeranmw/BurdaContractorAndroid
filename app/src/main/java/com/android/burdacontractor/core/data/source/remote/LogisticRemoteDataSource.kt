package com.android.burdacontractor.core.data.source.remote

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.domain.model.LogisticCoordinate
import com.android.burdacontractor.core.domain.model.LogisticIsTracking
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
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
        databaseReference = Firebase.database
            .getReference("logistic")
            .child(logisticId)
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){
                    val lat = dataSnapshot.child("latitude").getValue(Double::class.java).toString()
                    val lon = dataSnapshot.child("longitude").getValue(Double::class.java).toString()
                    val speed = dataSnapshot.child("speed").getValue(Double::class.java).toString()
                    val bearing = dataSnapshot.child("bearing").getValue(Double::class.java).toString()
                    val acc = dataSnapshot.child("accuracy").getValue(Double::class.java).toString()
                    val provider = dataSnapshot.child("provider").getValue(String::class.java).toString()
                    val logisticCoordinate = LogisticCoordinate(lat.toDouble(), lon.toDouble(), bearing.toDouble(),speed.toDouble(), acc.toDouble(), provider)
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
    fun getTracking(logisticId: String) = callbackFlow<Resource<LogisticIsTracking>> {
        databaseReference = Firebase.database
            .getReference("logistic")
            .child(logisticId)
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){
                    val isTracking = dataSnapshot.child("isTracking").getValue(Boolean::class.java).toString().toBoolean()
                    val logisticTracking = LogisticIsTracking(isTracking)
                    this@callbackFlow.trySendBlocking(Resource.Success(logisticTracking))
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
        databaseReference = Firebase.database.getReference("logistic")
        databaseReference.child(logisticId).child("accuracy").setValue(coordinate.accuracy)
        databaseReference.child(logisticId).child("bearing").setValue(coordinate.bearing)
        databaseReference.child(logisticId).child("latitude").setValue(coordinate.latitude)
        databaseReference.child(logisticId).child("longitude").setValue(coordinate.longitude)
        databaseReference.child(logisticId).child("speed").setValue(coordinate.speed)
        databaseReference.child(logisticId).child("provider").setValue(coordinate.provider)
    }
    fun setIsTracking(logisticId: String, isTracking: Boolean) {
        databaseReference = Firebase.database.getReference("logistic")
        databaseReference.child(logisticId).child("isTracking").setValue(isTracking)
    }
}

