package com.example.iqraaly_task.ui.fragments.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.iqraaly_task.R
import com.example.iqraaly_task.databinding.FragmentHomeBinding
import com.example.iqraaly_task.ui.adapters.PassengersRecyclerViewAdapter
import com.example.iqraaly_task.utils.AppConstants
import com.example.iqraaly_task.utils.Resource

class HomeFragment : Fragment() {
    private lateinit var mBinding: FragmentHomeBinding
    private lateinit var mViewModel: HomeFragmentViewModel
    private lateinit var mPassengersRecyclerViewAdapter: PassengersRecyclerViewAdapter
    private lateinit var mLayoutManager: LinearLayoutManager
    private var isLoading = false
    private var isLastPage = false
    private var isScrolling = false
    private val mScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= AppConstants.PAGE_SIZE
            val shouldPaginate =
                isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                mViewModel.getData(requireContext())
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupUI()
        setupObservers()
    }

    private fun setupViewModel() {
        mViewModel = ViewModelProvider(this).get(HomeFragmentViewModel::class.java)
        if (mViewModel.pageNum == AppConstants.FIRST_PAGE) {
            mViewModel.getData(requireContext())
        }
    }

    private fun setupUI() {
        mPassengersRecyclerViewAdapter = PassengersRecyclerViewAdapter()
        mBinding.homeFragmentViewModel = mViewModel
        mBinding.lifecycleOwner = this

        mLayoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )

        mBinding.passengersRv.apply {
            layoutManager = mLayoutManager
            adapter = mPassengersRecyclerViewAdapter
            addOnScrollListener(this@HomeFragment.mScrollListener)
        }
    }

    private fun setupObservers() {
        mViewModel.allPassengers.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { newsResponse ->
                        mPassengersRecyclerViewAdapter.differ.submitList(newsResponse.data.toList())
                        val totalPages = newsResponse.totalPages
                        isLastPage = mViewModel.pageNum == totalPages
                        mBinding.mainPb.visibility = View.GONE
                        mBinding.passengersRv.visibility = View.VISIBLE
                        mBinding.tryAgainBt.visibility = View.GONE
                        mBinding.bottomPb.visibility = View.GONE
                        isLoading = false
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        Log.e(AppConstants.HOME_FRAGMENT_TAG, "An error occured: $message")
                    }
                }
                is Resource.Loading -> {
                    mBinding.bottomPb.visibility = View.VISIBLE
                    mBinding.tryAgainBt.visibility = View.GONE
                    mBinding.mainPb.visibility = View.GONE
                    isLoading = true
                }
            }
        })

        mViewModel.isHaveInternetConnection.observe(requireActivity(), {
            if (it == false) {
                if (mViewModel.pageNum > 1) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.no_internet_connection),
                        Toast.LENGTH_LONG
                    )
                        .show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.no_internet_connection),
                        Toast.LENGTH_LONG
                    )
                        .show()
                    mBinding.passengersRv.visibility = View.GONE
                    mBinding.mainPb.visibility = View.GONE
                    mBinding.tryAgainBt.visibility = View.VISIBLE
                    mBinding.bottomPb.visibility = View.GONE
                    isLoading = false
                }
            } else {
                mBinding.mainPb.visibility = View.VISIBLE
                mBinding.tryAgainBt.visibility = View.GONE
                mBinding.bottomPb.visibility = View.GONE
                isLoading = true
            }
        })
    }

}