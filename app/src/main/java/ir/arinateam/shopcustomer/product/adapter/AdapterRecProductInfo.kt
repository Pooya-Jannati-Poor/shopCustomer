package ir.arinateam.shopcustomer.product.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.imageview.ShapeableImageView
import ir.arinateam.shopcustomer.R
import ir.arinateam.shopcustomer.databinding.LayoutRecHomeProductBinding
import ir.arinateam.shopcustomer.databinding.LayoutRecProductInfoBinding
import ir.arinateam.shopcustomer.product.model.ModelRecProductInfo
import ir.arinateam.shopcustomer.utils.NumbersSeparator

class AdapterRecProductInfo(
    private val context: Context,
    private val lsModelProductInfo: List<ModelRecProductInfo>
) : RecyclerView.Adapter<AdapterRecProductInfo.ItemAdapter>() {

    private lateinit var bindingAdapter: LayoutRecProductInfoBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdapter {
        val inflater = LayoutInflater.from(context)
        bindingAdapter =
            DataBindingUtil.inflate(inflater, R.layout.layout_rec_product_info, parent, false)
        return ItemAdapter(bindingAdapter)
    }

    override fun onBindViewHolder(holder: ItemAdapter, position: Int) {

        val model = lsModelProductInfo[position]

        holder.tvBookPropertyTitle.text = model.title
        holder.tvBookPropertyDescription.text = model.description

    }

    override fun getItemCount(): Int = lsModelProductInfo.size

    inner class ItemAdapter(binding: LayoutRecProductInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val tvBookPropertyTitle: TextView = binding.tvBookPropertyTitle
        val tvBookPropertyDescription: TextView = binding.tvBookPropertyDescription

    }

}