package ir.arinateam.shopcustomer.product.model

data class ModelProductDetail(

    val id: Int,

    val name: String,

    val publisher: String,

    val writer: String,

    val price: Int,

    val discountedPrice: Int,

    val hasDiscount: Boolean,

    val discount: Int,

    val amount: Int,

    val isbn: String,

    val score: Int,

    val image: String,

    val pages: Int,

    val publish_year: Int,

    val description: String,

    val comments: List<ModelRecCommentBase>

)
