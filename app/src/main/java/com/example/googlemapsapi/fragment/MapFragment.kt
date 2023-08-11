package com.example.googlemapsapi.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.googlemapsapi.R
import com.example.googlemapsapi.databinding.FragmentMapBinding
import com.example.googlemapsapi.viewModel.MapViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: FragmentMapBinding
    private val mapViewModel: MapViewModel by viewModels()
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
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Restore camera position if available
        if (savedCameraPosition != null) {
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(savedCameraPosition!!))
        } else {
            val sydney = LatLng(-34.0, 151.0)
            mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        }

        mMap.setOnCameraIdleListener {
            savedCameraPosition = mMap.cameraPosition
        }

    }

    @SuppressLint("SetTextI18n")
    fun showDialog(latitude: Double, longitude: Double) {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.previewdialog)
        val editTextAddQuestion: EditText = dialog.findViewById(R.id.editTextAddQuestion)
        val editTextAnswer: EditText = dialog.findViewById(R.id.editTextAnswer)
        val btnClose: TextView = dialog.findViewById(R.id.btnclose)
        val btnAddToList: Button = dialog.findViewById(R.id.buttondiolog)
        editTextAddQuestion.setText(editTextAddQuestion.text.toString() + latitude)
        editTextAnswer.setText(editTextAnswer.text.toString() + longitude);
        val sydney = LatLng( latitude, longitude)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        btnAddToList.setOnClickListener {
            val questionText = editTextAddQuestion.text.toString()
            val answerText = editTextAnswer.text.toString()

            if (questionText.isNotEmpty() && answerText.isNotEmpty()) {

                Toast.makeText(requireContext(), "Вопрос и ответ добавлены", Toast.LENGTH_SHORT)
                    .show()
                mapViewModel.saveTest(latitude,longitude)
                dialog.dismiss()

            } else {
                Toast.makeText(requireContext(), "Введите вопрос и ответ", Toast.LENGTH_SHORT)
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