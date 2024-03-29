package com.samra.beerapp.data.mapper

import com.samra.beerapp.data.domain.Beer
import com.samra.beerapp.data.local.BeerEntity
import com.samra.beerapp.data.remote.BeerDto

fun BeerDto.toBeerEntity() : BeerEntity{
    return BeerEntity(
        id = id ,
        name = name ,
        tagline= tagline ,
        description = description ,
        firstBrewed = first_brewed ,
        imageUrl = image_url
    )
}

fun BeerEntity.toBeer():Beer{
    return Beer(
        id = id ,
        name = name ,
        tagline= tagline ,
        description = description ,
        firstBrewed = firstBrewed ,
        imageUrl = imageUrl
    )
}