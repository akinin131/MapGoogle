package com.example.googlemapsapi.adapter

import android.annotation.SuppressLint
import android.content.Context

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.domain.model.CoordinatesModel
import com.example.googlemapsapi.R
import com.example.googlemapsapi.databinding.ItemBinding

class ListCoordinatesAdapter :
    RecyclerView.Adapter<ListCoordinatesAdapter.CoordinatesViewHolder>() {

    private var listCoordinates = emptyList<CoordinatesModel>()

    class CoordinatesViewHolder(itemView: View) : ViewHolder(itemView) {
        private val binding = ItemBinding.bind(itemView)
        private val degreesSymbol = "\u00B0"

        @SuppressLint("SetTextI18n")
        fun bind(context: Context, coordinates: CoordinatesModel) {
            binding.apply {
                id.text = "${context.getString(R.string.dot) + " "} ${coordinates.id}"
                latitude.text = "${coordinates.latitude}$degreesSymbol"
                longitude.text = "${coordinates.longitude}$degreesSymbol"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoordinatesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return CoordinatesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listCoordinates.size
    }

    override fun onBindViewHolder(holder: CoordinatesViewHolder, position: Int) {
        val currentTest = listCoordinates[position]
        val context = holder.itemView.context
        holder.bind(context, currentTest)
    }

    fun setList(newList: List<CoordinatesModel>) {
        val diffResult = DiffUtil.calculateDiff(CoordinatesDiffCallback(listCoordinates, newList))
        listCoordinates = newList
        diffResult.dispatchUpdatesTo(this)
    }
}