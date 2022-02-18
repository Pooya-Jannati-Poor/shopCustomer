package ir.arinateam.shopcustomer.profile.model

import com.google.gson.annotations.SerializedName
import ir.arinateam.shopcustomer.category.model.ModelRecProduct

data class ModelRecFavoriteList(

    @SerializedName("id")
    val favoriteId: Int,

    val product: ModelRecProduct
)
