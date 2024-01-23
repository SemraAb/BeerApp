@file:OptIn(ExperimentalPagingApi::class)

package com.samra.beerapp.data.remote

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.LoadType.*
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.samra.beerapp.data.local.BeerDb
import com.samra.beerapp.data.local.BeerEntity
import com.samra.beerapp.data.mapper.toBeerEntity
import java.io.IOException

class BeerRemoteMediator(
    private val beerDb: BeerDb ,
    private val beerApi : BeerApi
): RemoteMediator<Int , BeerEntity>(){
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, BeerEntity>
    ): MediatorResult {
        return try{
            //load key -> which page of data should be loaded based on the LoadType and the current state of the paging operation.
            val loadKey  = when(loadType){
                REFRESH -> 1
                PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )
                APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if(lastItem == null){
                        // it means there is no data
                        1
                    } else{
                        //state.config.pageSize is 10 (each page contains 10 items).
                        //The last item in the current list has an ID of 35 for example
                        // it shows currect item is which on page
                        (lastItem.id/ state.config.pageSize) +1
                    }
                }
            }
            val beers = beerApi.getBeers(
                page = loadKey,
                per_page = state.config.pageSize
            )


            beerDb.withTransaction {
                if(loadType ==  LoadType.REFRESH){
                    beerDb.BeerDao().clearAll()
                }
                val beerEntity = beers.map { it.toBeerEntity() }
                beerDb.BeerDao().upsertAll(beerEntity)

                MediatorResult.Success(
                    endOfPaginationReached = beers.isEmpty()
                )
            }
        } catch (e:IOException){
            MediatorResult.Error(e)
        } catch (e: HttpException){
            MediatorResult.Error(e)
        }
    }

}