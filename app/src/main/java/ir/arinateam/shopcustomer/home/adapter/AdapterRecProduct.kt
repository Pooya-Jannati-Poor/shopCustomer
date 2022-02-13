package ir.arinateam.shopcustomer.home.adapter

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
import com.google.android.material.imageview.ShapeableImageView
import ir.arinateam.shopcustomer.R
import ir.arinateam.shopcustomer.databinding.LayoutRecHomeCategoryBinding
import ir.arinateam.shopcustomer.databinding.LayoutRecHomeProductBinding
import ir.arinateam.shopcustomer.home.model.ModelRecHomeProduct
import ir.arinateam.shopcustomer.utils.NumbersSeparator

class AdapterRecProduct(
    private val context: Context,
    private val lsModelProduct: List<ModelRecHomeProduct>
) : RecyclerView.Adapter<AdapterRecProduct.ItemAdapter>() {

    private lateinit var bindingAdapter: LayoutRecHomeProductBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdapter {
        val inflater = LayoutInflater.from(context)
        bindingAdapter =
            DataBindingUtil.inflate(inflater, R.layout.layout_rec_home_product, parent, false)
        return ItemAdapter(bindingAdapter)
    }

    override fun onBindViewHolder(holder: ItemAdapter, position: Int) {

        val model = lsModelProduct[position]

        Glide.with(context).load("http://applicationfortests.ir/" + model.image).diskCacheStrategy(
            DiskCacheStrategy.ALL
        )
            .fitCenter().into(holder.imgProduct)

        val numbersSeparator = NumbersSeparator()

        holder.tvBookName.text = model.name
        holder.tvProductPrice.text =
            numbersSeparator.doubleToStringNoDecimal(model.discountedPrice.toDouble()) + " تومان"

        holder.itemView.setOnClickListener {

            val bundle = Bundle()
            bundle.putInt("productId", model.id)

            Navigation.findNavController(it)
                .navigate(R.id.action_homeFragment_to_productFragment, bundle)

        }

    }

    override fun getItemCount(): Int = lsModelProduct.size

    inner class ItemAdapter(binding: LayoutRecHomeProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val imgProduct: ShapeableImageView = binding.imgProduct
        val tvBookName: TextView = binding.tvBookName
        val tvProductPrice: TextView = binding.tvProductPrice

    }

}