package ir.arinateam.shopcustomer.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import ir.arinateam.shopcustomer.R

class Loading(context: Context) {

    var dialogLoading: Dialog = Dialog(context)

    init {
        dialogLoading.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogLoading.setContentView(R.layout.loading_lottie)
        dialogLoading.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogLoading.setCancelable(false)
        dialogLoading.show()
    }

    fun hideDialog() {

        dialogLoading.dismiss()

    }

}