package com.example.googlemapsapi.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.domain.model.CoordinatesModel

class CoordinatesDiffCallback(
    private val oldList: List<CoordinatesModel>,
    private val newList: List<CoordinatesModel>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}