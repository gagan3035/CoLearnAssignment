package com.example.colearn.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.colearn.Utils.ImageDiffUtils
import com.example.colearn.databinding.LayoutImageGridItemBinding
import com.example.imageloader.imageloader.ImageLoader

/**
 * Created by Gagan on 27/02/21.
 */
class ImageGridAdapter(val itemClickListener: (url:String,position:Int) -> Unit) : RecyclerView.Adapter<ImageGridAdapter.ImageGridViewHolder>() {
    var list : ArrayList<String> = ArrayList<String>()
    var fullList : ArrayList<String> = ArrayList<String>()



    fun setData(thumbnailList: List<String>,fullList:List<String>) {
//        val diffUtils = ImageDiffUtils(list,thumbnailList)
//        val diffUtilsResult = DiffUtil.calculateDiff(diffUtils)
        list.addAll(thumbnailList)
        this.fullList.addAll(fullList)
        notifyDataSetChanged()
        //diffUtilsResult.dispatchUpdatesTo(this)
    }

    fun clearData(){
        list.clear()
        fullList.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageGridViewHolder {
        val binding =
            LayoutImageGridItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageGridViewHolder(binding,itemClickListener)
    }

    override fun onBindViewHolder(holder: ImageGridViewHolder, position: Int) {
        holder.bind(list.get(position),fullList.get(position),position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ImageGridViewHolder(
        val binding: LayoutImageGridItemBinding,
        val itemClickListener: (url: String,position:Int) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(url: String,fullUrl:String,position:Int) {
            //Glide.with(binding.root).load(url).into(binding.imageView)
            ImageLoader.with(binding.root.context).load(binding.imageView,url)
            binding.imageView.setOnClickListener {
                itemClickListener(fullUrl,position)
            }

        }

    }
}