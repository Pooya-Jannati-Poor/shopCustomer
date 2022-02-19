package ir.arinateam.shopcustomer

import android.app.Application
import android.content.Context
import android.os.Environment
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import java.io.File
import java.io.PrintWriter
import java.io.RandomAccessFile
import java.io.StringWriter
import java.util.*

class AppController : Application() {

    override fun onCreate() {
        super.onCreate()


        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(object : CalligraphyInterceptor(
                    CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/iransansdnregular.ttf")
                        .setFontAttrId(io.github.inflationx.calligraphy3.R.attr.fontPath)
                        .build()
                ) {

                })
                .build()
        )

    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

    }

    override fun onLowMemory() {
        super.onLowMemory()
        System.gc()
    }

}