package com.example.colearn.Utils

import androidx.recyclerview.widget.DiffUtil

/**
 * Created by Gagan on 01/03/21.
 */
class ImageDiffUtils(private val oldList: List<String>, private val newList: List<String>) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList.get(oldItemPosition) == oldList.get(newItemPosition)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList.get(oldItemPosition) == oldList.get(newItemPosition)
    }


}