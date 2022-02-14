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
import ir.arinateam.shopcustomer.R
import ir.arinateam.shopcustomer.databinding.LayoutRecHomeCategoryBinding
import ir.arinateam.shopcustomer.home.model.ModelCategory

class AdapterRecCategory(
    private val context: Context,
    private val lsModelCategory: List<ModelCategory>
) : RecyclerView.Adapter<AdapterRecCategory.ItemAdapter>() {

    private lateinit var bindingAdapter: LayoutRecHomeCategoryBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdapter {
        val inflater = LayoutInflater.from(context)
        bindingAdapter =
            DataBindingUtil.inflate(inflater, R.layout.layout_rec_home_category, parent, false)
        return ItemAdapter(bindingAdapter)
    }

    override fun onBindViewHolder(holder: ItemAdapter, position: Int) {

        val model = lsModelCategory[position]

        Glide.with(context).load("http://applicationfortests.ir/" + model.image).diskCacheStrategy(
            DiskCacheStrategy.ALL
        )
            .fitCenter().into(holder.imgCategory)

        holder.tvCategory.text = model.name

        holder.itemView.setOnClickListener {

            val bundle = Bundle()
            bundle.putInt("categoryId", model.id)
            bundle.putString("categoryName", model.name)

            Navigation.findNavController(it)
                .navigate(R.id.action_homeFragment_to_subCategoryFragment, bundle)

        }

    }

    override fun getItemCount(): Int = lsModelCategory.size

    inner class ItemAdapter(binding: LayoutRecHomeCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val imgCategory: ImageView = binding.imgCategory
        val tvCategory: TextView = binding.tvCategory

    }

}