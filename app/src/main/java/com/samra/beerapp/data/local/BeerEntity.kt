package com.samra.beerapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("beerentity")
data class BeerEntity (
    @PrimaryKey
    val id : Int ,
    val name : String ,
    val tagline: String ,
    val description : String,
    val firstBrewed: String ,
    val imageUrl : String?
        )