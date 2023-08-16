package com.example.googlemapsapi.fragment

import android.content.IntentFilter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.RecyclerView
import com.example.googlemapsapi.adapter.ListCoordinatesAdapter
import com.example.googlemapsapi.databinding.FragmentListCoordinatesBinding
import com.example.googlemapsapi.util.DataUpdatedListReceiver
import com.example.googlemapsapi.viewModel.ListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListCoordinates : Fragment() {

    private lateinit var binding: FragmentListCoordinatesBinding
    private val listViewModel: ListViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ListCoordinatesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentListCoordinatesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        adapter = ListCoordinatesAdapter()
        recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        listViewModel.getAllCoordinates()

        listViewModel.coordinatesLiveData.observe(viewLifecycleOwner) { newData ->
            adapter.setList(newData)
        }
    }

    private val dataUpdatedReceiver = DataUpdatedListReceiver {

        updateList()

    }

    private fun updateList() {
        listViewModel.coordinatesLiveData.observe(viewLifecycleOwner) { newData ->
            adapter.setList(newData)
        }
        listViewModel.getAllCoordinates()
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter("com.yourapp.DATA_UPDATED")
        requireContext().registerReceiver(dataUpdatedReceiver, filter)
    }

    override fun onPause() {
        super.onPause()
        requireContext().unregisterReceiver(dataUpdatedReceiver)
    }
}