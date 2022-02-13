package ir.arinateam.shopcustomer.product.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ir.arinateam.shopcustomer.R
import ir.arinateam.shopcustomer.databinding.LayoutRecCommentsBinding
import ir.arinateam.shopcustomer.databinding.LayoutRecProductInfoBinding
import ir.arinateam.shopcustomer.product.model.ModelRecComment

class AdapterRecComment(
    private val context: Context,
    private val lsModelComment: List<ModelRecComment>
) : RecyclerView.Adapter<AdapterRecComment.ItemAdapter>() {

    private lateinit var bindingAdapter: LayoutRecCommentsBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdapter {
        val inflater = LayoutInflater.from(context)
        bindingAdapter =
            DataBindingUtil.inflate(inflater, R.layout.layout_rec_comments, parent, false)
        return ItemAdapter(bindingAdapter)
    }

    override fun onBindViewHolder(holder: ItemAdapter, position: Int) {

        val model = lsModelComment[position]

        holder.tvUsername.text = model.user.name
        holder.tvComment.text = model.description
        holder.rateComment.rating = model.rate.toFloat()

    }

    override fun getItemCount(): Int = lsModelComment.size

    inner class ItemAdapter(binding: LayoutRecCommentsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val tvUsername: TextView = binding.tvUsername
        val tvComment: TextView = binding.tvComment
        val rateComment: RatingBar = binding.rateComment

    }

}