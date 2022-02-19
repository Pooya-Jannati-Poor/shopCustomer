package ir.arinateam.shopcustomer

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

    companion object {

        var jsonArrayBasket = JSONArray()
        var jsonObjectBasket = JSONObject()

    }

}