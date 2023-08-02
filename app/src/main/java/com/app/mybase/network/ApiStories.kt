package com.app.mybase.network

import android.database.Observable
import com.app.mybase.model.GetResponse
import com.app.mybase.model.PostResponse
import com.app.mybase.model.UserInputDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiStories {

    @POST("0e171a95b31b4eaeba71ab07f6b3c897/vigneshPost")
    suspend fun postUserDetails(
        @Body userInputDto: UserInputDto
    ): PostResponse

    @GET("0e171a95b31b4eaeba71ab07f6b3c897/vigneshPost")
    suspend fun getUserDetails(): List<GetResponse>
}