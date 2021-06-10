package com.example.iqraaly_task.data

import com.example.iqraaly_task.pojo.AllPassengersModel
import com.example.iqraaly_task.pojo.PassengerModel
import com.example.iqraaly_task.utils.AppConstants
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("passenger?size=${AppConstants.PAGE_SIZE}")
    suspend fun getAllPassengers(@Query("page") pageNum: Int): Response<AllPassengersModel>

    @GET("passenger/{id}")
    suspend fun getPassengerInfo(@Path("id") passengerId: String): Response<PassengerModel>
}