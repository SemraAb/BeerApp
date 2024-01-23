package com.samra.beerapp.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface BeerApi {
    @GET("beers")
    suspend fun getBeers(
        @Query("page")page :Int ,
        @Query("per_page") per_page : Int
    ) : List<BeerDto>

    companion object {
        const val BASE_URL = "https://api.punkapi.com/v2/beers"
    }
}