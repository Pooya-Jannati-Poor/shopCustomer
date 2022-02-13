package ir.arinateam.shopcustomer.utils

import android.content.Context
import android.graphics.Bitmap
import ir.arinateam.shopcustomer.utils.Loading
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.*


class PrepareImageForUpload {

    lateinit var loading: Loading


    fun buildImageBodyPart(context: Context, fileName: String, bitmap: Bitmap): MultipartBody.Part {
        loading = Loading(context)
        val leftImageFile = convertBitmapToFile(context, fileName, bitmap)
        val reqFile = RequestBody.create(MediaType.parse("image/*"), leftImageFile)
        loading.hideDialog()
        return MultipartBody.Part.createFormData(fileName, leftImageFile.name, reqFile)
    }

    private fun convertBitmapToFile(context: Context, fileName: String, bitmap: Bitmap): File {
        //create a file to write bitmap data
        val file = File(context.cacheDir, fileName)
        file.createNewFile()

        //Convert bitmap to byte array
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos)
        val bitMapData = bos.toByteArray()

        //write the bytes in file
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        try {
            fos?.write(bitMapData)
            fos?.flush()
            fos?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }

}