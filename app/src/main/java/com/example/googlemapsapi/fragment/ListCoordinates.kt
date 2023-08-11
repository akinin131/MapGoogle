package com.example.googlemapsapi.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.googlemapsapi.R
import com.example.googlemapsapi.adapter.ListCoordinatesAdapter
import com.example.googlemapsapi.databinding.FragmentListCoordinatesBinding
import com.example.googlemapsapi.databinding.FragmentMapBinding
import com.example.googlemapsapi.viewModel.ListViewModel
import com.example.googlemapsapi.viewModel.MapViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListCoordinates : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding: FragmentListCoordinatesBinding
    private val listViewModel: ListViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ListCoordinatesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentListCoordinatesBinding.inflate(inflater, container, false)
        return binding.root
        init()

    }

    private fun init() {
        adapter = ListCoordinatesAdapter()
        recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        listViewModel.getAllTests()

        listViewModel.testsLiveData.observe(viewLifecycleOwner) { tests ->
            adapter.setList(tests)
        }

    }

}