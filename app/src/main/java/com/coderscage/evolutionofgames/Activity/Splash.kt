package com.coderscage.evolutionofgames.Activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.coderscage.evolutionofgames.R
import com.coderscage.evolutionofgames.Utilities.RemoteConfigUtils
import com.coderscage.evolutionofgames.Utilities.SaveSharedPreference

class Splash : AppCompatActivity() {
    private val SPLASH_DISPLAY_LENGTH = 2000 // 2 seconds delay
    private lateinit var splashBackground: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splashBackground = findViewById(R.id.splashBackground)
        applyTheme()

        Handler(Looper.getMainLooper()).postDelayed({
            navigateToNextScreen()
        }, SPLASH_DISPLAY_LENGTH.toLong())
    }

    private fun applyTheme() {
        if (SaveSharedPreference.getIsDarkMode(applicationContext)) {
            splashBackground.setBackgroundColor(resources.getColor(R.color.dark_color, theme))
            statusBarNightColor()
        } else {
            splashBackground.setBackgroundColor(resources.getColor(R.color.colorPrimary, theme))
            statusBarNormalColor()
        }
    }

    private fun navigateToNextScreen() {
        val nextActivity = when {
            RemoteConfigUtils.isHideAppIntro() -> WebViewActivity::class.java
            SaveSharedPreference.getShowIntro(applicationContext) == "true" -> WebViewActivity::class.java
            else -> AppIntroActivity::class.java
        }

        startActivity(Intent(this, nextActivity))
        finish()
    }

    private fun statusBarNormalColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = resources.getColor(R.color.colorPrimary, theme)
        }
    }

    private fun statusBarNightColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = resources.getColor(R.color.dark_color, theme)
        }
    }
}