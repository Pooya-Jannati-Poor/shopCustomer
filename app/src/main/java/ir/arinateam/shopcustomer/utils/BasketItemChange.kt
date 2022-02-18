package ir.arinateam.shopcustomer.utils

interface BasketItemChange {

    fun onRemoved(id: Int)

    fun onChanged()

}