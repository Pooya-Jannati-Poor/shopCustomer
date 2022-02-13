package ir.arinateam.shopcustomer.home.model

data class ModelGetCustomerData(

    val sliders: List<ModelRecBannerTopHome>,

    val categories: List<ModelCategory>,

    val latestProducts: List<ModelRecHomeProduct>,

    val popularProducts: List<ModelRecHomeProduct>,

)
