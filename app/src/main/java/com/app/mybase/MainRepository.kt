package com.app.mybase

import androidx.lifecycle.LiveData
import com.app.mybase.helper.ApisResponse
import com.app.mybase.helper.AppConstants
import com.app.mybase.model.GetResponse
import com.app.mybase.model.PostResponse
import com.app.mybase.model.UserInputDto
import com.app.mybase.network.ApiStories
import com.app.mybase.room.UserDao
import com.app.mybase.room.UserEntity
import javax.inject.Inject

class MainRepository @Inject constructor(var apiStories: ApiStories, var userDao: UserDao) {


    suspend fun postUserDetails(userInputDto: UserInputDto): ApisResponse<PostResponse> {
        return try {
            val callApi = apiStories.postUserDetails(userInputDto)
            ApisResponse.Success(callApi)
        } catch (e: Exception) {
            ApisResponse.Error(e.message ?: AppConstants.SOMETHING_WENT_WRONG)
        }
    }

    suspend fun getUserDetails(): ApisResponse<List<GetResponse>> {
        return try {
            val callApi = apiStories.getUserDetails()
            ApisResponse.Success(callApi)
        } catch (e: Exception) {
            ApisResponse.Error(e.message ?: AppConstants.SOMETHING_WENT_WRONG)
        }
    }

    fun getAllUser(): LiveData<List<UserEntity>> = userDao.getAllUser()

    suspend fun insertAll(userEntityList: List<UserEntity>) {
        userDao.insertAll(userEntityList)
    }


}