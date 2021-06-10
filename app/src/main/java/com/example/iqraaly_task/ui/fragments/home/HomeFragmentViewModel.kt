package com.example.iqraaly_task.ui.fragments.home

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iqraaly_task.data.ApiRepository
import com.example.iqraaly_task.data.RetrofitBuilder
import com.example.iqraaly_task.pojo.AllPassengersModel
import com.example.iqraaly_task.utils.AppConstants
import com.example.iqraaly_task.utils.NetworkUtils
import com.example.iqraaly_task.utils.Resource
import kotlinx.coroutines.*
import retrofit2.Response

class HomeFragmentViewModel : ViewModel() {

    val allPassengers: MutableLiveData<Resource<AllPassengersModel>> = MutableLiveData()
    val isHaveInternetConnection = MutableLiveData<Boolean?>()
    private var allPassengersResponse: AllPassengersModel? = null
    var pageNum = AppConstants.FIRST_PAGE

    fun getData(context: Context) {
        if (NetworkUtils.isInternetAvailable(context)) {
            if (pageNum > AppConstants.FIRST_PAGE) {
                allPassengers.postValue(Resource.Loading())
            } else {
                isHaveInternetConnection.value = true
            }
            getAllPassengers()
        } else {
            isHaveInternetConnection.value = false
        }
    }

    private fun getAllPassengers() = viewModelScope.launch {
        if (pageNum > AppConstants.FIRST_PAGE) {
            allPassengers.postValue(Resource.Loading())
        }
        val response = ApiRepository(RetrofitBuilder.apiService).getAllPassengers(pageNum)
        allPassengers.postValue(handleAllPassengersResponse(response))
    }

    private fun handleAllPassengersResponse(response: Response<AllPassengersModel>): Resource<AllPassengersModel> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                pageNum++
                if (allPassengersResponse == null) {
                    allPassengersResponse = resultResponse
                } else {
                    val oldPassengers = allPassengersResponse?.data
                    val newPassengers = resultResponse.data
                    oldPassengers?.addAll(newPassengers)
                }
                return Resource.Success(allPassengersResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

}