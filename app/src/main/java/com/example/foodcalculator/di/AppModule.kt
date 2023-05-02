package com.example.foodcalculator.di

import android.app.Application
import com.example.foodcalculator.data.remote.plants.PlantsApi
import com.example.foodcalculator.data.remote.recipes.RecipesApi
import com.example.foodcalculator.data.repository.PlantsRepository
import com.example.foodcalculator.data.repository.RecipesRepository
import com.example.foodcalculator.other.Constants
import com.example.foodcalculator.sign_in.GoogleAuthUiClient
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun providePlantsApi(moshi: Moshi): PlantsApi {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(Constants.PLANTS_BASE_URL)
            .build()
            .create(PlantsApi::class.java)
    }

    @Provides
    @Singleton
    fun providePlantsRepository(api: PlantsApi): PlantsRepository {
        return PlantsRepository(api)
    }
    @Provides
    @Singleton
    fun provideRecipesApi(): RecipesApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.RECIPES_BASE_URL)
            .build()
            .create(RecipesApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRecipesRepository(api: RecipesApi): RecipesRepository {
        return RecipesRepository(api)
    }

    @Provides
    @Singleton
    fun provideGoogleAuthUiClient(app: Application): GoogleAuthUiClient {
        return GoogleAuthUiClient(
            context = app.applicationContext,
            oneTapClient = Identity.getSignInClient(app.applicationContext)
        )
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore() = Firebase.firestore
}