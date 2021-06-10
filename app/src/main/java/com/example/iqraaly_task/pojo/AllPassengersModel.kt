package com.example.iqraaly_task.pojo

import com.google.gson.annotations.SerializedName

data class AllPassengersModel(
    @SerializedName("totalPassengers") var totalPassengers : Int,
    @SerializedName("totalPages") var totalPages : Int,
    @SerializedName("data") var data : ArrayList<PassengerModel>
)
