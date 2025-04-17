package com.coderscage.evolutionofgames.Activity


import com.coderscage.evolutionofgames.Activity.WebViewActivity
import android.content.Intent

import android.graphics.Color

import android.os.Build

import android.os.Bundle

import androidx.fragment.app.Fragment

import com.coderscage.evolutionofgames.R

import com.coderscage.evolutionofgames.Utilities.RemoteConfigUtils

import com.coderscage.evolutionofgames.Utilities.SaveSharedPreference

import com.github.appintro.AppIntro

import com.github.appintro.AppIntroFragment

import java.util.regex.Pattern





open class AppIntroActivity : AppIntro() {

    var app_intro_text_color = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        app_intro_text_color = if (isValidColor(RemoteConfigUtils.getAppIntroTextColor())) {

            Color.parseColor(RemoteConfigUtils.getAppIntroTextColor())

        } else {

            getResources().getColor(R.color.colorWhite)

        }

        setColorSkipButton(app_intro_text_color)

        setNextArrowColor(app_intro_text_color)

        setColorDoneText(app_intro_text_color)

        setIndicatorColor(app_intro_text_color, 1)

        statusBarNormalColor()

        addSlide(

            AppIntroFragment.newInstance(

                getString(R.string.slide_one_title),

                getString(R.string.slide_one_text),

                R.drawable.slide_one_icon,

                getResources().getColor(R.color.colorPrimary),

                app_intro_text_color,

                app_intro_text_color

            )

        )

        addSlide(

            AppIntroFragment.newInstance(

                getString(R.string.slide_two_title),

                getString(R.string.slide_two_text),

                R.drawable.slide_two_icon,

                getResources().getColor(R.color.colorPrimary),

                app_intro_text_color,

                app_intro_text_color

            )

        )

        addSlide(

            AppIntroFragment.newInstance(

                getString(R.string.slide_three_title),

                getString(R.string.slide_three_text),

                R.drawable.slide_three_icon,

                getResources().getColor(R.color.colorPrimary),

                app_intro_text_color,

                app_intro_text_color

            )

        )

    }



    private fun isValidColor(color: String): Boolean {

        val colorPattern =

            Pattern.compile("#([0-9a-f]{3}|[0-9a-f]{6}|[0-9a-f]{8}|[0-9A-F]{3}|[0-9A-F]{6}|[0-9A-F]{8})")

        val m = colorPattern.matcher(color)

        return m.matches()

    }



    override fun onDonePressed(currentFragment: Fragment?) {

        super.onDonePressed(currentFragment)

        val intent = Intent(

            getApplicationContext(),

            WebViewActivity::class.java

        )

        startActivity(intent)

        finish()

        SaveSharedPreference.setShowIntro(getApplicationContext(), "true")

    }



    override fun onSkipPressed(currentFragment: Fragment?) {

        super.onSkipPressed(currentFragment)

        val intent = Intent(

            getApplicationContext(),

            WebViewActivity::class.java

        )

        startActivity(intent)

        finish()

        SaveSharedPreference.setShowIntro(getApplicationContext(), "true")

    }



    fun statusBarNormalColor() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            getWindow().setStatusBarColor(

                getResources().getColor(

                    R.color.colorPrimary,

                    this.getTheme()

                )

            )

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary))

        }

    }

}