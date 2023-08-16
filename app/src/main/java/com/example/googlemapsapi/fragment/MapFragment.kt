package com.example.googlemapsapi.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.domain.model.CoordinatesModel
import com.example.googlemapsapi.R
import com.example.googlemapsapi.databinding.FragmentMapBinding
import com.example.googlemapsapi.databinding.PreviewdialogBinding
import com.example.googlemapsapi.viewModel.ListViewModel
import com.example.googlemapsapi.viewModel.MapViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback  {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: FragmentMapBinding
    private val mapViewModel: MapViewModel by viewModels()
    private val listViewModel: ListViewModel by viewModels()
    private var savedCameraPosition: CameraPosition? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        savedCameraPosition = savedInstanceState?.getParcelable("cameraPosition")

        listViewModel.getAllCoordinates()
    }

    private fun addMarkersFromViewModel(coordinatesList: List<CoordinatesModel>) {
        for (coordinates in coordinatesList) {
            val markerOptions =
                MarkerOptions().position(LatLng(coordinates.latitude, coordinates.longitude))
            mMap.addMarker(markerOptions)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        listViewModel.coordinatesLiveData.observe(viewLifecycleOwner) { coordinatesList ->
            addMarkersFromViewModel(coordinatesList) // Устанавливаем маркеры на карту
        }

        mMap.setOnCameraIdleListener {
            savedCameraPosition = mMap.cameraPosition
        }
    }

    @SuppressLint("SetTextI18n")
    fun showDialog(latitude: Double, longitude: Double) {
        val dialog = Dialog(requireContext())
        val dialogBinding = PreviewdialogBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)

        val degreesSymbol = "\u00B0"

        dialogBinding.editTextAddLatitude.setText("${dialogBinding.editTextAddLatitude.text}$latitude$degreesSymbol")
        dialogBinding.editTextLongitude.setText("${dialogBinding.editTextLongitude.text}$longitude$degreesSymbol")

        val sydney = LatLng(latitude, longitude)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))

        dialogBinding.btnclose.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.buttonSave.setOnClickListener {
            val questionText = dialogBinding.editTextAddLatitude.text.toString()
            val answerText = dialogBinding.editTextLongitude.text.toString()

            if (questionText.isNotEmpty() && answerText.isNotEmpty()) {
                Toast.makeText(requireContext(), "Координаты сохранены", Toast.LENGTH_SHORT).show()
                mapViewModel.saveTest(latitude, longitude)
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "Координаты не сохранены", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        dialog.show()
    }

    fun getMapCenter(): LatLng {
        val bounds = mMap.projection.visibleRegion.latLngBounds
        return bounds.center
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        savedCameraPosition?.let {
            outState.putParcelable("cameraPosition", it)
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedCameraPosition = savedInstanceState?.getParcelable("cameraPosition")
    }
}
