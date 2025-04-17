package com.coderscage.evolutionofgames.Utilities

import android.annotation.SuppressLint
import android.util.Log
import com.coderscage.evolutionofgames.R
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings

object RemoteConfigUtils {
    @SuppressLint("StaticFieldLeak")
    private var firebaseRemoteConfig: FirebaseRemoteConfig? = null
    private const val BASE_URL_KEY = "base_url"
    private const val HIDE_INTERSTITIAL_Ad_KEY = "HideInterstitialAd"
    private const val HIDE_BANNER_AD_KEY = "HideBannerAd"
    private const val BANNER_AD_ID_KEY = "ad_unitId_BANNER"
    private const val APP_OPEN_Ad_ID_KEY = "app_open_ad_id"
    private const val HIDE_APP_OPEN_AD_KEY = "hide_app_open_ad"
    private const val INTERSTITIAL_Ad_ID_KEY = "ad_unitId_interstitialAd"
    private const val HIDE_NAVIGATION_VIEW = "HideNavigationView"
    private const val HIDE_TOOLBAR = "HideToolBar"
    private const val TOOLBAR_TEXT_COLOR = "ToolBar_text_color"
    private const val IS_TOOLBAR_ICONS_LITE = "isToolBarIcons_Lite"
    private const val ENABLE_LOADER = "Enable_Loader"
    private const val LOADER_STYLE = "Loader_style"
    private const val LOADER_COLOR = "Loader_color"
    private const val ENABLE_SWIPE_REFRESH = "Enable_SwipeRefresh"
    private const val HIDE_APP_INTRO = "HideAppIntro"
    private const val APP_INTRO_TEXT_COLOR = "AppIntroTextColor"
    private const val IS_RTL = "Is_RTL"
    private const val ENABLE_DARK_MODE = "enable_DarkMode"
    private const val ENABLE_SHARE = "enable_share"
    private const val ENABLE_RATE = "enable_rate"
    private const val ENABLE_CONTACT_US = "enable_contact_us"
    private const val ENABLE_CAMERA_PERMISSION = "enable_camera_permission"
    fun init() {
        firebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        firebaseRemoteConfig!!.setConfigSettingsAsync(configSettings)
        firebaseRemoteConfig!!.setDefaultsAsync(R.xml.remote_config_defaults)
        firebaseRemoteConfig!!.fetchAndActivate().addOnCompleteListener {
                task ->
            if (task.isSuccessful) {
                val updated = task.result
                Log.d("TAG", "Config params updated: $updated")

            } else {

            }
        }
    }

    fun getBaseUrl(): String {
        return firebaseRemoteConfig!!.getString(BASE_URL_KEY)
    }

    fun isHideInterstitialAd(): Boolean {
        return firebaseRemoteConfig!!.getBoolean(HIDE_INTERSTITIAL_Ad_KEY)
    }

    fun isHideToolBar(): Boolean {
        return firebaseRemoteConfig!!.getBoolean(HIDE_TOOLBAR)
    }

    fun isHideBannerAd(): Boolean {
        return firebaseRemoteConfig!!.getBoolean(HIDE_BANNER_AD_KEY)
    }

    fun isHideNavigationView(): Boolean {
        return firebaseRemoteConfig!!.getBoolean(HIDE_NAVIGATION_VIEW)
    }

    fun getBannerAdId(): String? {
        return firebaseRemoteConfig!!.getString(BANNER_AD_ID_KEY)
    }


    fun getInterstitialAdId(): String? {
        return firebaseRemoteConfig!!.getString(INTERSTITIAL_Ad_ID_KEY)
    }

    fun getToolbarTextColor(): String {
        return firebaseRemoteConfig!!.getString(TOOLBAR_TEXT_COLOR)
    }


    fun isToolBarIcons_Lite(): Boolean {
        return firebaseRemoteConfig!!.getBoolean(IS_TOOLBAR_ICONS_LITE)
    }

    fun enableLoader(): Boolean {
        return firebaseRemoteConfig!!.getBoolean(ENABLE_LOADER)
    }

    @JvmStatic
    fun getLoaderStyle(): String {
        return firebaseRemoteConfig!!.getString(LOADER_STYLE)
    }
    @JvmStatic
    fun getLoaderColor(): String {
        return firebaseRemoteConfig!!.getString(LOADER_COLOR)
    }

    fun getAppIntroTextColor(): String {
        return firebaseRemoteConfig!!.getString(APP_INTRO_TEXT_COLOR)
    }

    fun enableSwipeRefresh(): Boolean {
        return firebaseRemoteConfig!!.getBoolean(ENABLE_SWIPE_REFRESH)
    }

    fun isHideAppIntro(): Boolean {
        return firebaseRemoteConfig!!.getBoolean(HIDE_APP_INTRO)
    }

    fun isRTL(): Boolean {
        return firebaseRemoteConfig!!.getBoolean(IS_RTL)
    }

    fun enableDarkMode(): Boolean {
        return firebaseRemoteConfig!!.getBoolean(ENABLE_DARK_MODE)
    }


    fun enableShare(): Boolean {
        return firebaseRemoteConfig!!.getBoolean(ENABLE_SHARE)
    }

    fun enableRate(): Boolean {
        return firebaseRemoteConfig!!.getBoolean(ENABLE_RATE)
    }

    fun enableContactUs(): Boolean {
        return firebaseRemoteConfig!!.getBoolean(ENABLE_CONTACT_US)
    }

    fun enableCameraPermission(): Boolean {
        return firebaseRemoteConfig!!.getBoolean(ENABLE_CAMERA_PERMISSION)
    }

}
