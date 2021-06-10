package com.example.iqraaly_task.ui.fragments.passengerDetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.iqraaly_task.R
import com.example.iqraaly_task.utils.AppConstants
import com.example.iqraaly_task.databinding.FragmentPassengerDetailsBinding
import com.example.iqraaly_task.utils.Resource

class PassengerDetailsFragment : Fragment() {
    private lateinit var mBinding: FragmentPassengerDetailsBinding
    private lateinit var mViewModel: PassengerDetailsFragmentViewModel
    private lateinit var mPassengerId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentPassengerDetailsBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mPassengerId = requireArguments().getString(AppConstants.PASSENGER_ID_KEY).toString()
        mBinding.tryAgainBt.setOnClickListener {
            mViewModel.getData(requireContext(), mPassengerId)
        }
        setupViewModel()
        setupObservers()

    }

    private fun setupViewModel() {
        mViewModel = ViewModelProvider(this).get(PassengerDetailsFragmentViewModel::class.java)
        mBinding.lifecycleOwner = this
        mViewModel.getData(requireContext(), mPassengerId)
    }

    private fun setupObservers() {
        mViewModel.passengerInfo.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { newsResponse ->
                        mBinding.passengerModel = newsResponse
                        mBinding.pbContainer.visibility = View.GONE
                        mBinding.contentContainer.visibility = View.VISIBLE
                        mBinding.tryAgainContainer.visibility = View.GONE
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        Log.e(AppConstants.HOME_FRAGMENT_TAG, "An error occured: $message")
                    }
                }
                is Resource.Loading -> TODO()
            }
        })

        mViewModel.isHaveInternetConnection.observe(requireActivity(), {
            if (it == false) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.no_internet_connection),
                    Toast.LENGTH_LONG
                )
                    .show()
                mBinding.contentContainer.visibility = View.GONE
                mBinding.pbContainer.visibility = View.GONE
                mBinding.tryAgainContainer.visibility = View.VISIBLE
            } else {
                mBinding.tryAgainContainer.visibility = View.GONE
                mBinding.pbContainer.visibility = View.VISIBLE
            }
        })
    }

}