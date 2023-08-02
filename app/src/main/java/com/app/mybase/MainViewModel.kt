package com.app.mybase


import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.app.mybase.base.BaseViewModel
import com.app.mybase.helper.ApisResponse
import com.app.mybase.model.UserInputDto
import com.app.mybase.room.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : BaseViewModel() {

    fun postUserDetails(userInputDto: UserInputDto) = liveData(Dispatchers.IO) {
        emit(ApisResponse.Loading)
        emit(mainRepository.postUserDetails(userInputDto))
        emit(ApisResponse.Complete)
    }

    fun getUserDetails() = liveData(Dispatchers.IO) {
        emit(ApisResponse.Loading)
        emit(mainRepository.getUserDetails())
        emit(ApisResponse.Complete)
    }

    fun getAllUser(): LiveData<List<UserEntity>> =
        mainRepository.getAllUser()

    fun insertAll(userEntityList: List<UserEntity>) {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.insertAll(userEntityList)
        }
    }

}