package com.example.googlemapsapi.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.domain.model.CoordinatesModel
import com.example.googlemapsapi.R
import com.example.googlemapsapi.databinding.ItemBinding

class ListCoordinatesAdapter: RecyclerView.Adapter<ListCoordinatesAdapter.CoordinatesViewHolder>() {

    private var listTest = emptyList<CoordinatesModel>()
    class CoordinatesViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemBinding.bind(itemView)

        fun bind(test: CoordinatesModel) {
            binding.apply {
                id.text = test.id.toString()
                latitude.text = test.latitude.toString()
                longitude.text = test.longitude.toString()

            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoordinatesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return CoordinatesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listTest.size
    }

    override fun onBindViewHolder(holder: CoordinatesViewHolder, position: Int) {
        val currentTest = listTest[position]
        holder.bind(currentTest)
    }
    @SuppressLint("NotifyDataSetChanged")
    fun setList(newTests: List<CoordinatesModel>) {
        listTest = newTests
        notifyDataSetChanged()
    }
}