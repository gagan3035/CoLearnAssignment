package com.example.colearn.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.colearn.databinding.LayoutFilterItemBinding
import com.example.colearn.models.FilterItem

/**
 * Created by Gagan on 01/03/21.
 */
class FilterAdapter(
    var itemList: List<FilterItem>,
    var itemClickListener: (item: String?, type: String) -> Unit
) :
    RecyclerView.Adapter<FilterAdapter.ViewHolder>() {

    var selectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            LayoutFilterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, itemClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position, itemList.get(position), itemList.get(0).filterVal?:"")
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun clear(){
        selectedPosition = -1
    }

    inner class ViewHolder(
        private var binding: LayoutFilterItemBinding,
        var itemClickListener: (item: String?, type: String) -> Unit


    ) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(position: Int, value: FilterItem, type: String) {
            if (position == 0) {
                binding.title.visibility = View.VISIBLE
                binding.title.text = value.name
                binding.filterItem.visibility = View.GONE
            } else {
                binding.filterItem.text = value.name
                binding.title.visibility = View.GONE
                binding.filterItem.visibility = View.VISIBLE
            }
            if (selectedPosition == -1) {
                binding.filterItem.isChecked = value.isChecked
            } else {
                binding.filterItem.isChecked = position == selectedPosition
            }
            binding.filterItem.setOnClickListener {
                if (position == selectedPosition) {
                    binding.filterItem.isChecked = false
                    selectedPosition = -1;
                } else {
                    itemClickListener(value.filterVal,type)
                    selectedPosition = position
                    bindingAdapter?.notifyDataSetChanged()
                }
            }
        }

    }
}