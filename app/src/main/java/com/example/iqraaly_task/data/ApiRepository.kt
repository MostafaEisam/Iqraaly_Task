package com.example.iqraaly_task.data

class ApiRepository(private val apiService: ApiService) {
    suspend fun getAllPassengers(pageNum: Int) = apiService.getAllPassengers(pageNum)
    suspend fun getPassengerInfo(passengerId: String) = apiService.getPassengerInfo(passengerId)
}