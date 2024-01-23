package com.samra.beerapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [BeerEntity::class] , version = 1)
abstract class BeerDb : RoomDatabase(){
    abstract fun BeerDao(): BeerDao
}