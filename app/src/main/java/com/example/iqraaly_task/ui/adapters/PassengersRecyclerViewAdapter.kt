package com.example.iqraaly_task.ui.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.iqraaly_task.R
import com.example.iqraaly_task.utils.AppConstants
import com.example.iqraaly_task.databinding.PassengersRecyclerViewItemBinding
import com.example.iqraaly_task.pojo.PassengerModel

class PassengersRecyclerViewAdapter :
    RecyclerView.Adapter<PassengersRecyclerViewAdapter.ViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<PassengerModel>() {
        override fun areItemsTheSame(oldItem: PassengerModel, newItem: PassengerModel): Boolean {
            return oldItem.Id == newItem.Id
        }

        override fun areContentsTheSame(oldItem: PassengerModel, newItem: PassengerModel): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PassengersRecyclerViewItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            val passengerId = differ.currentList[position].Id
            val bundle = Bundle()
            bundle.putString(AppConstants.PASSENGER_ID_KEY, passengerId)
            it.findNavController()
                .navigate(R.id.action_homeFragment_to_passengerDetailsFragment, bundle)
        }
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class ViewHolder(private val binding: PassengersRecyclerViewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PassengerModel) {
            binding.passengerModel = item
        }
    }

}