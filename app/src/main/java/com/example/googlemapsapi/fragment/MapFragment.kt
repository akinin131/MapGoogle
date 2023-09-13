package com.example.googlemapsapi.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
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
    private var _binding: FragmentMapBinding?=null
    private val binding get() = _binding!!
    private var savedCameraPosition: CameraPosition? = null
    private val mapViewModel: MapViewModel by viewModels()
    private val listViewModel: ListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        savedCameraPosition = savedInstanceState?.getParcelable("cameraPosition", CameraPosition::class.java)

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
            val latitudeText = dialogBinding.editTextAddLatitude.text.toString()
            val longitudeText = dialogBinding.editTextLongitude.text.toString()

            if (latitudeText.isNotEmpty() && longitudeText.isNotEmpty()) {
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
        savedCameraPosition = savedInstanceState?.getParcelable("cameraPosition", CameraPosition::class.java)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
