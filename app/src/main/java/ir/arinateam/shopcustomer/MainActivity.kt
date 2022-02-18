package ir.arinateam.shopcustomer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    companion object {

        var jsonArrayBasket = JSONArray()
        var jsonObjectBasket = JSONObject()

    }

}