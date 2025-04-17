package com.coderscage.evolutionofgames.Utilities

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

object SaveSharedPreference {

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    // Key for storing whether ads are removed
    private const val PREF_ADS_REMOVED = "pref_ads_removed"

    fun setIsAdRemoved(context: Context, isRemoved: Boolean) {
        val editor = getSharedPreferences(context).edit()
        editor.putBoolean(PREF_ADS_REMOVED, isRemoved)
        editor.apply()
    }

    fun getIsAdRemoved(context: Context): Boolean {
        return getSharedPreferences(context).getBoolean(PREF_ADS_REMOVED, false) // Default to false
    }

    // Key for storing dark mode preference
    private const val PREF_DARK_MODE = "pref_dark_mode"

    fun setIsDarkMode(context: Context, isDarkMode: Boolean) {
        val editor = getSharedPreferences(context).edit()
        editor.putBoolean(PREF_DARK_MODE, isDarkMode)
        editor.apply()
    }

    fun getIsDarkMode(context: Context): Boolean {
        return getSharedPreferences(context).getBoolean(PREF_DARK_MODE, false) // Default to false
    }

    // Key for storing whether the app intro has been shown
    private const val PREF_SHOW_INTRO = "pref_show_intro"

    fun setShowIntro(context: Context, showIntro: String) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(PREF_SHOW_INTRO, showIntro)
        editor.apply()
    }

    fun getShowIntro(context: Context): String? {
        return getSharedPreferences(context).getString(PREF_SHOW_INTRO, "false") // Default to false
    }

    // You can add more keys and functions here for other preferences
    // For example, to store the user's preferred language:
    private const val PREF_PREFERRED_LANGUAGE = "pref_preferred_language"

    fun setPreferredLanguage(context: Context, languageCode: String) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(PREF_PREFERRED_LANGUAGE, languageCode)
        editor.apply()
    }

    fun getPreferredLanguage(context: Context): String? {
        return getSharedPreferences(context).getString(PREF_PREFERRED_LANGUAGE, "en") // Default to English
    }
}