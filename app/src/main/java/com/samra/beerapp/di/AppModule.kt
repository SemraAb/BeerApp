package com.samra.beerapp.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.RemoteMediator
import androidx.room.Room
import com.samra.beerapp.data.domain.Beer
import com.samra.beerapp.data.local.BeerDb
import com.samra.beerapp.data.local.BeerEntity
import com.samra.beerapp.data.remote.BeerApi
import com.samra.beerapp.data.remote.BeerRemoteMediator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@OptIn(ExperimentalPagingApi::class)
@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideRetrofit() = Retrofit.Builder()
        .baseUrl(BeerApi.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Singleton
    @Provides
    fun provideApi(retrofit: Retrofit) = retrofit.create(Beer::class.java)

    @Provides
    @Singleton
    fun provideRoom(@ApplicationContext context: Context): BeerDb =
        Room.databaseBuilder(
            context,
            BeerDb::class.java,
            "BeerDb"
        ).build()


    @Provides
    @Singleton
    fun provieBeerPager(beerDb: BeerDb , beerApi: BeerApi): Pager<Int , BeerEntity>{
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = BeerRemoteMediator(beerDb , beerApi) ,
            pagingSourceFactory = {
                beerDb.BeerDao().pagingSource()
            }
        )
    }
}


