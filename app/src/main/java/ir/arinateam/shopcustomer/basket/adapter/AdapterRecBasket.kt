package ir.arinateam.shopcustomer.basket.adapter

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import ir.arinateam.shopcustomer.MainActivity
import ir.arinateam.shopcustomer.R
import ir.arinateam.shopcustomer.basket.model.ModelRecBasket
import ir.arinateam.shopcustomer.databinding.LayoutRecBasketBinding
import ir.arinateam.shopcustomer.utils.BasketItemChange
import ir.arinateam.shopcustomer.utils.NumbersSeparator
import org.json.JSONObject

class AdapterRecBasket(
    private val context: Context,
    private val lsModelBasket: ArrayList<ModelRecBasket>,
    private val basketItemChange: BasketItemChange
) : RecyclerView.Adapter<AdapterRecBasket.ItemAdapter>() {

    private lateinit var bindingAdapter: LayoutRecBasketBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdapter {
        val inflater = LayoutInflater.from(context)
        bindingAdapter =
            DataBindingUtil.inflate(inflater, R.layout.layout_rec_basket, parent, false)
        return ItemAdapter(bindingAdapter)
    }

    override fun onBindViewHolder(holder: ItemAdapter, position: Int) {

        val model = lsModelBasket[position]

        Glide.with(context).load("http://applicationfortests.ir/" + model.image).diskCacheStrategy(
            DiskCacheStrategy.ALL
        )
            .fitCenter().into(holder.imgProduct)

        holder.tvBookName.text = model.name

        holder.tvCount.text = model.amount.toString()

        val numbersSeparator = NumbersSeparator()

        val price = model.amount * model.discountedPrice

        holder.tvBookPrice.text =
            numbersSeparator.doubleToStringNoDecimal(price.toDouble()) + " تومان"

        holder.imgProduct.setOnClickListener {

            val bundle = Bundle()
            bundle.putInt("productId", model.id)

            Navigation.findNavController(it)
                .navigate(R.id.action_basketFragment_to_productFragment, bundle)

        }

        holder.imgIncrease.setOnClickListener {

            for (i in 0 until MainActivity.jsonArrayBasket.length()) {

                val item = MainActivity.jsonArrayBasket.getJSONObject(i)

                if (model.id == item.getInt("productId")) {

                    val tempJson = JSONObject()

                    tempJson.put("productId", model.id)
                    tempJson.put("amount", model.amount + 1)

                    MainActivity.jsonArrayBasket.put(i, tempJson)

                    MainActivity.jsonObjectBasket = JSONObject()

                    MainActivity.jsonObjectBasket.put("orders", MainActivity.jsonArrayBasket)

                }

            }

            basketItemChange.onChanged()

        }

        holder.imgReduce.setOnClickListener {

            if (model.amount > 1) {

                for (i in 0 until MainActivity.jsonArrayBasket.length()) {

                    val item = MainActivity.jsonArrayBasket.getJSONObject(i)

                    if (model.id == item.getInt("productId")) {

                        val tempJson = JSONObject()

                        tempJson.put("productId", model.id)
                        tempJson.put("amount", model.amount - 1)

                        MainActivity.jsonArrayBasket.put(i, tempJson)

                        MainActivity.jsonObjectBasket = JSONObject()

                        MainActivity.jsonObjectBasket.put("orders", MainActivity.jsonArrayBasket)

                    }

                }

                basketItemChange.onChanged()

            }

        }

        holder.imgDelete.setOnClickListener {

            removeProduct(holder.adapterPosition)
            basketItemChange.onRemoved(model.id)

        }

    }

    private fun removeProduct(adapterPosition: Int) {

        lsModelBasket.removeAt(adapterPosition)
        notifyItemRemoved(adapterPosition)


    }

    override fun getItemCount(): Int = lsModelBasket.size

    inner class ItemAdapter(binding: LayoutRecBasketBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val imgProduct: ImageView = binding.imgProduct
        val tvBookName: TextView = binding.tvBookName
        val tvBookPrice: TextView = binding.tvBookPrice
        val imgIncrease: ImageView = binding.imgIncrease
        val tvCount: TextView = binding.tvCount
        val imgReduce: ImageView = binding.imgReduce
        val imgDelete: ImageView = binding.imgDelete

    }

}