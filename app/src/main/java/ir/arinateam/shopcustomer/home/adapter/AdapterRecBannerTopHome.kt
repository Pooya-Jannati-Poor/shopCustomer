package ir.arinateam.shopcustomer.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.imageview.ShapeableImageView
import ir.arinateam.shopcustomer.R
import ir.arinateam.shopcustomer.databinding.LayoutRecHomeBannerTopBinding
import ir.arinateam.shopcustomer.home.model.ModelRecBannerTopHome

class AdapterRecBannerTopHome(
    private val context: Context,
    private val lsModelRecBannerTopHome: List<ModelRecBannerTopHome>
) : RecyclerView.Adapter<AdapterRecBannerTopHome.ItemAdapter>() {

    private lateinit var bindingAdapter: LayoutRecHomeBannerTopBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdapter {
        val inflater = LayoutInflater.from(context)
        bindingAdapter =
            DataBindingUtil.inflate(inflater, R.layout.layout_rec_home_banner_top, parent, false)
        return ItemAdapter(bindingAdapter)
    }

    override fun onBindViewHolder(holder: ItemAdapter, position: Int) {

        val model = lsModelRecBannerTopHome[position]

        Glide.with(context).load("http://applicationfortests.ir/" + model.image).diskCacheStrategy(
            DiskCacheStrategy.ALL
        )
            .fitCenter().into(holder.imgBanner)

    }

    override fun getItemCount(): Int = lsModelRecBannerTopHome.size

    inner class ItemAdapter(binding: LayoutRecHomeBannerTopBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val imgBanner: ShapeableImageView = binding.imgBanner

    }

}