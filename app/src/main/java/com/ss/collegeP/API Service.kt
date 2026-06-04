package com.ss.collegeP

import SpoonacularResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.spoonacular.com"

val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val spoonApi = retrofit.create(SpoonacularApiService::class.java)

interface SpoonacularApiService{
    @GET("recipes/random")
    suspend fun getRandomRecipes(
        @Query("apiKey") apiKey : String,
        @Query("number") number : Int = 150,
        @Query("includeNutrition") includeNutrition : Boolean = true
    ) : SpoonacularResponse
}