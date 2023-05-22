package com.android.burdacontractor.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.burdacontractor.core.data.source.local.entity.TourismEntity
import com.android.burdacontractor.core.data.source.local.room.TourismDao

@Database(entities = [TourismEntity::class], version = 1, exportSchema = false)
abstract class TourismDatabase : RoomDatabase() {

    abstract fun tourismDao(): TourismDao

}