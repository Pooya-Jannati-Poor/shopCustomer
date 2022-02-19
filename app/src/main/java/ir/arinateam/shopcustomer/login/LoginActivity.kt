package ir.arinateam.shopcustomer.login

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import ir.arinateam.shopcustomer.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

    override fun onPause() {
        super.onPause()

        finishAffinity()

    }

}