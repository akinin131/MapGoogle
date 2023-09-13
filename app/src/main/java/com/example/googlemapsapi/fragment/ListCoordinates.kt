package com.example.googlemapsapi.fragment

import android.content.IntentFilter
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.googlemapsapi.adapter.ListCoordinatesAdapter
import com.example.googlemapsapi.databinding.FragmentListCoordinatesBinding
import com.example.googlemapsapi.util.DataUpdatedListReceiver
import com.example.googlemapsapi.viewModel.ListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListCoordinates : Fragment() {

    private var _binding: FragmentListCoordinatesBinding? = null
    private val binding get() = _binding!!
    private val listViewModel: ListViewModel by viewModels()
    private val recyclerView: RecyclerView by lazy { binding.recyclerView }
    private val adapter: ListCoordinatesAdapter by lazy { ListCoordinatesAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentListCoordinatesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()

    }

    private fun init() {

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
        val filter = IntentFilter("com.example.DATA_UPDATED")
        requireContext().registerReceiver(dataUpdatedReceiver, filter)
    }

    override fun onPause() {
        super.onPause()
        requireContext().unregisterReceiver(dataUpdatedReceiver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

