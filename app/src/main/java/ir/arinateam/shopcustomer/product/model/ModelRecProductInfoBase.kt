package ir.arinateam.shopcustomer.product.model

import ir.arinateam.shopcustomer.home.model.ModelRecHomeProduct


data class ModelRecProductInfoBase(

    val product: ModelProductDetail,

    val sameWriterProducts: List<ModelRecHomeProduct>

)
