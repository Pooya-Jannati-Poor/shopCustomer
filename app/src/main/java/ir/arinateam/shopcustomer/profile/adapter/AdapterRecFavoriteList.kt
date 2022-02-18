package ir.arinateam.shopcustomer.profile.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import ir.arinateam.shopcustomer.R
import ir.arinateam.shopcustomer.category.model.ModelRecProduct
import ir.arinateam.shopcustomer.databinding.LayoutRecCategoryBinding
import ir.arinateam.shopcustomer.databinding.LayoutRecProductsBinding
import ir.arinateam.shopcustomer.profile.model.ModelRecFavoriteList
import ir.arinateam.shopcustomer.utils.NumbersSeparator

class AdapterRecFavoriteList(
    private val context: Context,
    private val lsModelProduct: List<ModelRecProduct>,
    private val lsModelRecFavoriteList: List<ModelRecFavoriteList>
) : RecyclerView.Adapter<AdapterRecFavoriteList.ItemAdapter>() {

    private lateinit var bindingAdapter: LayoutRecProductsBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdapter {
        val inflater = LayoutInflater.from(context)
        bindingAdapter =
            DataBindingUtil.inflate(inflater, R.layout.layout_rec_products, parent, false)
        return ItemAdapter(bindingAdapter)
    }

    override fun onBindViewHolder(holder: ItemAdapter, position: Int) {

        val model = lsModelProduct[position]

        Glide.with(context).load("http://applicationfortests.ir/" + model.image).diskCacheStrategy(
            DiskCacheStrategy.ALL
        )
            .fitCenter().into(holder.imgProduct)

        holder.tvProductName.text = model.name
        holder.tvWriter.text = model.writer
        holder.tvPublisher.text = model.publisher

        val numbersSeparator = NumbersSeparator()

        holder.tvPrice.text =
            numbersSeparator.doubleToStringNoDecimal(model.discountedPrice.toDouble()) + " تومان"

        holder.itemView.setOnClickListener {

            val bundle = Bundle()
            bundle.putInt("productId", model.id)

            Navigation.findNavController(it)
                .navigate(R.id.action_subCategoryFragment_to_productFragment, bundle)

        }

    }

    override fun getItemCount(): Int = lsModelProduct.size

    inner class ItemAdapter(binding: LayoutRecProductsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val imgProduct: ImageView = binding.imgProduct
        val tvProductName: TextView = binding.tvProductName
        val tvWriter: TextView = binding.tvWriter
        val tvPublisher: TextView = binding.tvPublisher
        val tvPrice: TextView = binding.tvPrice

    }

}