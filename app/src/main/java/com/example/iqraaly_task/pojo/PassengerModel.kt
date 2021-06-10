package com.example.iqraaly_task.pojo

import com.google.gson.annotations.SerializedName

data class PassengerModel(
    @SerializedName("_id") var Id: String,
    @SerializedName("name") var name: String,
    @SerializedName("trips") var trips: Int,
)
