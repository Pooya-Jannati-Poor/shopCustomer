package ir.arinateam.shopcustomer.category.model

import com.google.gson.annotations.SerializedName

data class ModelRecProduct(

    val id: Int,

    val name: String,

    val image: String,

    val writer: String,

    val publisher: String,

    val discountedPrice: Int,

    @SerializedName("price")
    val basePrice: Int

)
