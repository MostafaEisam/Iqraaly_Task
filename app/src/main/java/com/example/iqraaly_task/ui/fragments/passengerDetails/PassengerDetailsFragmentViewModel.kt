package com.example.iqraaly_task.ui.fragments.passengerDetails

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iqraaly_task.data.ApiRepository
import com.example.iqraaly_task.data.RetrofitBuilder
import com.example.iqraaly_task.pojo.PassengerModel
import com.example.iqraaly_task.utils.NetworkUtils
import com.example.iqraaly_task.utils.Resource
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import retrofit2.Response

class PassengerDetailsFragmentViewModel : ViewModel() {

    val passengerInfo: MutableLiveData<Resource<PassengerModel>> = MutableLiveData()
    val isHaveInternetConnection = MutableLiveData<Boolean?>()

    fun getData(context: Context, passengerId: String) {
        if (NetworkUtils.isInternetAvailable(context)) {
            isHaveInternetConnection.value = true
            getPassengerInfo(passengerId)
        } else {
            isHaveInternetConnection.value = false
        }
    }

    private fun getPassengerInfo(passengerId: String) = viewModelScope.launch {
        val response = ApiRepository(RetrofitBuilder.apiService).getPassengerInfo(passengerId)
        passengerInfo.postValue(handleAllPassengersResponse(response))
    }

    private fun handleAllPassengersResponse(response: Response<PassengerModel>): Resource<PassengerModel> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


}