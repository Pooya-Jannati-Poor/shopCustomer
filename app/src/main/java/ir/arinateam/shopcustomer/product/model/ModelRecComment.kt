package ir.arinateam.shopcustomer.product.model

import com.google.gson.annotations.SerializedName

data class ModelRecComment(


    val user: ModelUser,

    @SerializedName("score")
    val rate: Int,

    @SerializedName("comment")
    val description: String

)
