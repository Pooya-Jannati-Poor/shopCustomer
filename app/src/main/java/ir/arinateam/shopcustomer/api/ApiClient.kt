package ir.arinateam.shopadmin.api


import com.google.gson.GsonBuilder
import ir.arinateam.shopcustomer.api.ApiInterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {

    private val baseUrl = "http://applicationfortests.ir/api/"

    private var request: ApiInterface

    companion object {

        lateinit var retrofit: Retrofit

    }

    init {

        val gosn = GsonBuilder()
            .setLenient()
            .create()

        retrofit = Retrofit
            .Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gosn))
            .build()
        request = retrofit.create(ApiInterface::class.java)
    }

}