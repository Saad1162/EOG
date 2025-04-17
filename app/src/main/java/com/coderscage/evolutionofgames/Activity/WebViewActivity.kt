package com.coderscage.evolutionofgames.Activity

import android.Manifest
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver
import android.webkit.GeolocationPermissions
import android.webkit.JsPromptResult
import android.webkit.JsResult
import android.webkit.PermissionRequest
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.coderscage.evolutionofgames.R
import com.coderscage.evolutionofgames.Utilities.CustomProgress
import com.coderscage.evolutionofgames.Utilities.MainLanguageApplication
import com.coderscage.evolutionofgames.Utilities.RemoteConfigUtils
import com.coderscage.evolutionofgames.Utilities.SaveSharedPreference
import com.coderscage.evolutionofgames.Utilities.VideoEnabledWebView
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.navigation.NavigationView
import java.io.File
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.system.exitProcess


class WebViewActivityTwo : AppCompatActivity() {



        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(com.coderscage.evolutionofgames.R.layout.activity_main)

            // Find the WebView in your layout
            val webView = findViewById<WebView>(com.coderscage.evolutionofgames.R.id.webView)

            // Enable JavaScript
            webView.settings.javaScriptEnabled = true
            webView.settings.domStorageEnabled = true  // Enable DOM storage
            webView.settings.cacheMode = android.webkit.WebSettings.LOAD_DEFAULT  // Corrected line

            // Set up WebView client to handle links inside WebView and block external browsers
            webView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                    // Prevent opening links in an external browser, load everything inside the WebView
                    view?.loadUrl(request?.url.toString())
                    return true
                }
            }

            // Define a custom WebChromeClient to block JavaScript alerts, confirms, and prompts
            val webChromeClient = object : WebChromeClient() {
                override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
                    // Block JavaScript alerts
                    result?.cancel()
                    return true
                }

                override fun onJsConfirm(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
                    // Block JavaScript confirm dialogs
                    result?.cancel()
                    return true
                }

                override fun onJsPrompt(
                    view: WebView?,
                    url: String?,
                    message: String?,
                    defaultValue: String?,
                    result: JsPromptResult?
                ): Boolean {
                    // Block JavaScript prompt dialogs
                    result?.cancel()
                    return true
                }
            }

            // Attach the WebChromeClient to the WebView
            webView.webChromeClient = webChromeClient

            // Load a webpage
            webView.loadUrl("https://evolutionofgames.com") // Replace with your desired URL
            webView.loadUrl("https://zgamesandroid.com") // Replace with your desired URL
            webView.loadUrl("https://androzgames.com") // Replace with your desired URL


        }
        // Optionally handle the back button to navigate inside WebView
        @Deprecated("Deprecated in Java")
        override fun onBackPressed() {
            val webView = findViewById<WebView>(com.coderscage.evolutionofgames.R.id.webView)
            if (webView.canGoBack()) {
                webView.goBack()
            } else {
                super.onBackPressed()  // Exit the activity if no history exists
            }
        }
    }


class WebViewActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    SwipeRefreshLayout.OnRefreshListener, ViewTreeObserver.OnScrollChangedListener {





    private lateinit var mAdView: AdView
    private var mInterstitialAd: InterstitialAd? = null
    private lateinit var drawer: DrawerLayout
    private lateinit var forward: ImageView
    private lateinit var backward: ImageView
    private lateinit var refresh: ImageView
    private var mUploadMessage: ValueCallback<Uri>? = null
    private var mCapturedImageURI: Uri? = null
    private var mFilePathCallback: ValueCallback<Array<Uri>>? = null
    private var mCameraPhotoPath: String? = null
    private val INPUT_FILE_REQUEST_CODE = 1
    private val FILE_CHOOSER_RESULT_CODE = 1
    private val TAG = WebViewActivity::class.java.simpleName
    private var doubleBackToExitPressedOnce = false
    private var url: String? = null
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var webView: VideoEnabledWebView
    private lateinit var dialog: Dialog
    private lateinit var toolBarTitle: TextView
    private var colorFromToolbar: Int = 0
    private var colorFromTextColor: Int = 0
    private lateinit var mySwipeRefreshLayout: SwipeRefreshLayout
    val progressDialog = CustomProgress.getInstance()!!
    var mGeoLocationRequestOrigin: String? = null
    var mGeoLocationCallback: GeolocationPermissions.Callback? = null



    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        val `in` = intent
        val data = `in`.data
        setContentView(R.layout.activity_webview)
        navigationView = findViewById(R.id.nav_view)
        toolbar = findViewById(R.id.toolbar)
        toolBarTitle = findViewById(R.id.toolbar_title)
        webView = findViewById(R.id.webView)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        drawer = findViewById(R.id.drawer_layout)
        forward = findViewById(R.id.forward)
        backward = findViewById(R.id.backward)
        refresh = findViewById(R.id.refresh)
        mySwipeRefreshLayout = findViewById(R.id.swipeContainer)
        if (data == null) {
            setUpWebViewDefaults(RemoteConfigUtils.getBaseUrl(),this)
        } else {
            setUpWebViewDefaults(Objects.requireNonNull(data).toString(),this)
        }
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        if (RemoteConfigUtils.isRTL()) {
            drawer.layoutDirection = View.LAYOUT_DIRECTION_RTL
            toolbar.layoutDirection = View.LAYOUT_DIRECTION_RTL
        }
        if (!RemoteConfigUtils.enableSwipeRefresh()) {
            mySwipeRefreshLayout.isRefreshing = false
            mySwipeRefreshLayout.isEnabled = false
        } else {
            mySwipeRefreshLayout.setOnRefreshListener(this)
            mySwipeRefreshLayout.viewTreeObserver.addOnScrollChangedListener(this)
        }

        val menuView = navigationView.menu
        val darkModeItem = menuView.findItem(R.id.dark_switch)
        val shareItem = menuView.findItem(R.id.share)
        val rateItem = menuView.findItem(R.id.rate)
        val nightModeButton =
            darkModeItem.actionView!!.findViewById<SwitchCompat>(R.id.nightModeButton)

        val colorFrom = resources.getColor(R.color.colorWhite)
        val colorTo = resources.getColor(R.color.dark_color)
        val colorToTextColor = resources.getColor(R.color.colorPrimaryDark)
        val colorToToolbar = resources.getColor(R.color.dark_color)

        colorFromToolbar = resources.getColor(R.color.colorPrimary)
        colorFromTextColor = resources.getColor(R.color.colorPrimary)


        if (!RemoteConfigUtils.enableDarkMode()) {
            darkModeItem.setVisible(false)
        }

        if (!RemoteConfigUtils.enableShare()) {
            shareItem.setVisible(false)
        }

        if (!RemoteConfigUtils.enableRate()) {
            rateItem.setVisible(false)
        }

        if (RemoteConfigUtils.enableCameraPermission()) {
            if (!checkPermissions()) {
                requestPermissions()
            }
        }

        if (SaveSharedPreference.getIsDarkMode(applicationContext)) {
            animateNavDrawerBackground(colorFrom, colorTo, colorFromTextColor)
            animateToolBarBackground(colorFromToolbar, colorToToolbar)
            statusBarNightColor()
            nightModeButton.isChecked = true
            forward.setImageResource(R.drawable.white_forward)
            backward.setImageResource(R.drawable.white_backward)
            refresh.setImageResource(R.drawable.refresh_white)
            toolBarTitle.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.colorWhite
                )
            )
            toggle.drawerArrowDrawable.color = resources.getColor(R.color.colorWhite)
        } else {
            animateNavDrawerBackground(colorTo, colorFrom, colorToTextColor)
            animateToolBarBackground(colorToToolbar, colorFromToolbar)
            statusBarNormalColor()
            if (isValidColor(RemoteConfigUtils.getToolbarTextColor()!!)) {
                toolBarTitle.setTextColor(Color.parseColor(RemoteConfigUtils.getToolbarTextColor()))
            }
            if (RemoteConfigUtils.isToolBarIcons_Lite()) {
                forward.setImageResource(R.drawable.white_forward)
                backward.setImageResource(R.drawable.white_backward)
                refresh.setImageResource(R.drawable.refresh_white)
                toggle.drawerArrowDrawable.color = resources.getColor(R.color.colorWhite)
            } else {
                forward.setImageResource(R.drawable.dark_forward)
                backward.setImageResource(R.drawable.dark_backward)
                refresh.setImageResource(R.drawable.refresh_dark)
                toggle.drawerArrowDrawable.color = resources.getColor(R.color.dark_color)
            }
        }
        nightModeButton.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            if (isChecked) {
                SaveSharedPreference.setIsDarkMode(applicationContext, true)
                animateNavDrawerBackground(colorFrom, colorTo, colorFromTextColor)
                animateToolBarBackground(colorFromToolbar, colorToToolbar)
                statusBarNightColor()
                toggle.drawerArrowDrawable.color = resources.getColor(R.color.colorWhite)
                toolBarTitle.setTextColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.colorWhite
                    )
                )
                forward.setImageResource(R.drawable.white_forward)
                backward.setImageResource(R.drawable.white_backward)
                refresh.setImageResource(R.drawable.refresh_white)
            } else {
                SaveSharedPreference.setIsDarkMode(applicationContext, false)
                animateNavDrawerBackground(colorTo, colorFrom, colorToTextColor)
                animateToolBarBackground(colorToToolbar, colorFromToolbar)
                statusBarNormalColor()
                if (isValidColor(RemoteConfigUtils.getToolbarTextColor()!!)) {
                    toolBarTitle.setTextColor(Color.parseColor(RemoteConfigUtils.getToolbarTextColor()))
                }
                if (RemoteConfigUtils.isToolBarIcons_Lite()) {
                    forward.setImageResource(R.drawable.white_forward)
                    backward.setImageResource(R.drawable.white_backward)
                    refresh.setImageResource(R.drawable.refresh_white)
                    toggle.drawerArrowDrawable.color = resources.getColor(R.color.colorWhite)
                } else {
                    forward.setImageResource(R.drawable.dark_forward)
                    backward.setImageResource(R.drawable.dark_backward)
                    refresh.setImageResource(R.drawable.refresh_dark)
                    toggle.drawerArrowDrawable.color = resources.getColor(R.color.dark_color)
                }
            }
        }
        navigationView.setNavigationItemSelectedListener(this)
        url = intent.getStringExtra("url")
        MobileAds.initialize(
            this
        ) {}
        val adBannerLayout = findViewById<LinearLayout>(R.id.layout_banner_holder)
        val header = navigationView.getHeaderView(0)

        val nav_layout = header.findViewById<LinearLayout>(R.id.nav_layout)
        val description = header.findViewById<TextView>(R.id.description)
        val title = header.findViewById<TextView>(R.id.title)
        nav_layout.setBackgroundColor(resources.getColor(R.color.colorPrimary))
        if (isValidColor(RemoteConfigUtils.getToolbarTextColor())) {
            title.setTextColor(Color.parseColor(RemoteConfigUtils.getToolbarTextColor()))
            description.setTextColor(Color.parseColor(RemoteConfigUtils.getToolbarTextColor()))
        }
        mAdView = AdView(this)
        mAdView.adUnitId = RemoteConfigUtils.getBannerAdId()!!
        mAdView.setAdSize(AdSize.BANNER)
        adBannerLayout.addView(mAdView)


        val adRequest = AdRequest.Builder().build()
        if (!RemoteConfigUtils.isHideInterstitialAd()){
            InterstitialAd.load(this, RemoteConfigUtils.getInterstitialAdId().toString(), adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdLoaded(@NonNull interstitialAd: InterstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd
                        //Log.i(TAG, "onAdLoaded");
                        mInterstitialAd!!.fullScreenContentCallback =
                            object : FullScreenContentCallback() {
                                override fun onAdDismissedFullScreenContent() {
                                    // Called when fullscreen content is dismissed.
                                    Log.d("TAG", "The ad was dismissed.")
                                }

                                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                    // Called when fullscreen content failed to show.
                                    Log.d("TAG", "The ad failed to show.")
                                }

                                override fun onAdShowedFullScreenContent() {
                                    // Called when fullscreen content is shown.
                                    // Make sure to set your reference to null so you don't
                                    // show it a second time.
                                    mInterstitialAd = null
                                    Log.d("TAG", "The ad was shown.")
                                }
                            }
                        if (mInterstitialAd != null) {
                            Handler(Looper.getMainLooper()).postDelayed({
                                // Check again if the ad is still available
                                if (mInterstitialAd != null) {
                                    mInterstitialAd!!.show(this@WebViewActivity)
                                }
                            }, 12000) // Delay in milliseconds (e.g., 3000ms = 3 seconds)
                        }
                        else {
                            Log.d("TAG", "The interstitial ad wasn't ready yet.")
                        }
                    }

                    override fun onAdFailedToLoad(@NonNull loadAdError: LoadAdError) {
                        // Handle the error
                        //Log.i(TAG, loadAdError.getMessage());
                        mInterstitialAd = null
                    }
                })
        }


        if (!RemoteConfigUtils.isHideBannerAd()) {
            adBannerLayout.visibility = View.VISIBLE
            mAdView.loadAd(adRequest)
        }

        if (RemoteConfigUtils.isHideNavigationView()) {
            toolbar.navigationIcon = null
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }

        if (RemoteConfigUtils.isHideToolBar()) {
            toolbar.visibility = View.GONE
        }

        backward.setOnClickListener {
            if (isConnected()) {
                if (webView.canGoBack()) {
                    webView.goBack()
                }
            } else {
                tryAgainPage(url)
            }
        }
        forward.setOnClickListener {
            if (isConnected()) {
                if (webView.canGoForward()) {
                    webView.goForward()
                }
            } else {
                tryAgainPage(url)
            }
        }

        refresh.setOnClickListener { webView.reload() }



    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (webView.canGoBack()) {
                    if (isConnected()) {
                        webView.goBack()
                    } else {
                        tryAgainPage(url)
                    }
                } else {
                    if (doubleBackToExitPressedOnce) {
                        finish()
                    }
                    doubleBackToExitPressedOnce = true
                    Toast.makeText(this, getString(R.string.please_click_back), Toast.LENGTH_SHORT)
                        .show()
                    Handler().postDelayed({
                        doubleBackToExitPressedOnce = false
                    }, 1000)
                }
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (isConnected()) {
            when (id) {
                R.id.home -> webView.loadUrl(RemoteConfigUtils.getBaseUrl())
                R.id.about -> setUpWebViewDefaults(getString(R.string.about_website_link),this)
                R.id.portfolio -> setUpWebViewDefaults(getString(R.string.Games_website_link),this)
                R.id.share -> shareApp(this)
                R.id.rate -> rateApp()
                R.id.logout -> {
                    moveTaskToBack(true)
                    android.os.Process.killProcess(android.os.Process.myPid())
                    exitProcess(1)
                }

            }
        } else {
            tryAgainPage(url)
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun shareApp(activity: Activity) {
        Log.i(TAG, "shareApp: $packageName")
        val appPackageName = packageName
        val appName = activity.getString(R.string.app_name)
        val shareBodyText = "https://play.google.com/store/apps/details?id=$appPackageName"

        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TITLE, appName)
            putExtra(Intent.EXTRA_TEXT, shareBodyText)
        }
        activity.startActivity(Intent.createChooser(sendIntent, null))
    }

    private fun rateApp() {
        try {
            val rateIntent = rateIntentForUrl("market://details")
            startActivity(rateIntent)
        } catch (e: ActivityNotFoundException) {
            val rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details")
            startActivity(rateIntent)
        }
    }

    private fun rateIntentForUrl(url: String): Intent {
        val intent = Intent(
            Intent.ACTION_VIEW, Uri.parse(
                String.format(
                    "%s?id=%s", url,
                    packageName
                )
            )
        )
        var flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        flags = flags or Intent.FLAG_ACTIVITY_NEW_DOCUMENT
        intent.addFlags(flags)
        return intent
    }


    @SuppressLint("SetJavaScriptEnabled")
    private fun setUpWebViewDefaults(webUrl: String, activity: Activity) {
        webView = findViewById(R.id.webView)
        webView.webChromeClient = object : WebChromeClient() {

            @Throws(IOException::class)
            private fun createImageFile(): File? {
                @SuppressLint("SimpleDateFormat") val timeStamp =
                    SimpleDateFormat(getString(R.string.data_formate))
                        .format(Date())
                val imageFileName = "JPEG_" + timeStamp + "_"
                val storageDir =
                    Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES
                    )
                return File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",  /* suffix */
                    storageDir /* directory */
                )
            }

            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?,
            ): Boolean {
                // Double check that we don't have any existing callbacks

                // Double check that we don't have any existing callbacks
                if (mFilePathCallback != null) {
                    mFilePathCallback!!.onReceiveValue(null)
                }
                mFilePathCallback = filePathCallback
                var takePictureIntent: Intent? = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (takePictureIntent!!.resolveActivity(packageManager) != null) {
                    // Create the File where the photo should go
                    var photoFile: File? = null
                    try {
                        photoFile = createImageFile()
                        takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath)
                    } catch (_: IOException) {
                    }
                    if (photoFile != null) {
                        mCameraPhotoPath = "file:" + photoFile.absolutePath
                        takePictureIntent!!.putExtra(
                            MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(photoFile)
                        )
                    } else {
                        takePictureIntent = null
                    }
                }
                val contentSelectionIntent = Intent(Intent.ACTION_GET_CONTENT)
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE)
                contentSelectionIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                contentSelectionIntent.setType("*/*")
                val intentArray: Array<Intent?>
                intentArray = if (takePictureIntent != null) {
                    arrayOf(takePictureIntent)
                } else {
                    arrayOfNulls(0)
                }
                val chooserIntent = Intent(Intent.ACTION_CHOOSER)
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent)
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Chooser")
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray)
                startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE)
                return true
            }

            override fun onPermissionRequest(request: PermissionRequest?) {
                if (request != null) {
                    val resources = request.resources
                    if (resources.contains(PermissionRequest.RESOURCE_AUDIO_CAPTURE)) {
                        handleAudioPermissionRequest(request)
                    }
                }


            }

            override fun onGeolocationPermissionsShowPrompt(
                origin: String?,
                callback: GeolocationPermissions.Callback?,
            ) {

                if (ContextCompat.checkSelfPermission(
                        applicationContext,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                    != PackageManager.PERMISSION_GRANTED
                ) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            activity,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    ) {
                        AlertDialog.Builder(activity)
                            .setMessage("Please turn ON the GPS to make app work smoothly")
                            .setNeutralButton(
                                android.R.string.ok,
                                DialogInterface.OnClickListener { dialogInterface, i ->
                                    mGeoLocationCallback = callback
                                    mGeoLocationRequestOrigin = origin
                                    ActivityCompat.requestPermissions(
                                        activity,
                                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1001
                                    )

                                })
                            .show()

                    } else {
                        //no explanation need we can request the locatio
                        mGeoLocationCallback = callback
                        mGeoLocationRequestOrigin = origin
                        ActivityCompat.requestPermissions(
                            activity,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1001
                        )
                    }
                } else {
                    callback!!.invoke(origin, true, true)
                }

            }
        }

        val webSettings = webView.settings
        var userAgent: String?
        userAgent = CUSTOM_USER_AGENT

        userAgent = userAgent + " " + USER_AGENT_POSTFIX

        webSettings.setUserAgentString(userAgent)

        webSettings.javaScriptEnabled = true
        webSettings.saveFormData= false
        webSettings.setSupportZoom(false)
        webSettings.setGeolocationEnabled(true)
        webSettings.allowFileAccess
        webSettings.allowFileAccessFromFileURLs
        webSettings.allowUniversalAccessFromFileURLs
        webSettings.useWideViewPort = true
        webSettings.domStorageEnabled = true
        webView.isHapticFeedbackEnabled = false
        webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        webView.isVerticalScrollBarEnabled = false

        webView.setDownloadListener { url, _, _, _, _ -> startActivity(Intent(Intent.ACTION_VIEW).setData(
            Uri.parse(url))) }

        // Set a WebViewClient to handle events and provide custom behavior
        webView.webViewClient = object : WebViewClient() {

            // This method is deprecated in API level 24, but still needed for older versions
            @Deprecated("Deprecated in Java")
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                var url = url
                if (isConnected()) {
                    if (url == null) {
                        return false
                    }
                    if (url.startsWith(applicationContext.getString(R.string.google_play_url))) {
                        view!!.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                        return true
                    }
                    if (url.startsWith(applicationContext.getString(R.string.facebook_url))) {
                        view!!.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                        return true
                    }
                    if (url.startsWith("tel:")) {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse(url))
                        view!!.context.startActivity(intent)
                        return true
                    }
                    if (url.contains(applicationContext.getString(R.string.instagram_url))) {
                        val uri = Uri.parse("http://instagram.com/")
                        val likeIng = Intent(Intent.ACTION_VIEW, uri)
                        likeIng.setPackage(applicationContext.getString(R.string.instagram_package))
                        try {
                            view!!.context.startActivity(likeIng)
                        } catch (e: ActivityNotFoundException) {
                            view!!.context.startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("http://instagram.com/")
                                )
                            )
                        }
                        return true
                    }
                    if (url.startsWith("market://")) {
                        view!!.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                        return true
                    }
                    if (url.startsWith("https://api")) {
                        view!!.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                        return true
                    }
                    if (url.startsWith("mailto:")) {
                        try {
                            val to: MutableList<String> = ArrayList()
                            val cc: MutableList<String> = ArrayList()
                            val bcc: MutableList<String> = ArrayList()
                            var subject: String? = null
                            var body: String? = null
                            url = url.replaceFirst("mailto:".toRegex(), "")
                            val urlSections =
                                url.split("&".toRegex()).dropLastWhile { it.isEmpty() }
                                    .toTypedArray()
                            if (urlSections.size >= 2) {
                                to.addAll(
                                    Arrays.asList(
                                        *urlSections[0].split(",".toRegex())
                                            .dropLastWhile { it.isEmpty() }
                                            .toTypedArray()))
                                for (i in 1 until urlSections.size) {
                                    val urlSection = urlSections[i]
                                    val keyValue = urlSection.split("=".toRegex())
                                        .dropLastWhile { it.isEmpty() }
                                        .toTypedArray()
                                    if (keyValue.size == 2) {
                                        val key = keyValue[0]
                                        var value: String? = keyValue[1]
                                        value = URLDecoder.decode(value, "UTF-8")
                                        if (key == "cc") {
                                            cc.addAll(
                                                Arrays.asList(
                                                    *url.split(",".toRegex())
                                                        .dropLastWhile { it.isEmpty() }
                                                        .toTypedArray()))
                                        } else if (key == "bcc") {
                                            bcc.addAll(
                                                Arrays.asList(
                                                    *url.split(",".toRegex())
                                                        .dropLastWhile { it.isEmpty() }
                                                        .toTypedArray()))
                                        } else if (key == "subject") {
                                            subject = value
                                        } else if (key == "body") {
                                            body = value
                                        }
                                    }
                                }
                            } else {
                                to.addAll(
                                    Arrays.asList(
                                        *url.split(",".toRegex()).dropLastWhile { it.isEmpty() }
                                            .toTypedArray()))
                            }
                            val emailIntent = Intent(Intent.ACTION_SEND)
                            emailIntent.setType("message/rfc822")
                            val dummyStringArray =
                                arrayOfNulls<String>(0) // For list to array conversion
                            emailIntent.putExtra(
                                Intent.EXTRA_EMAIL,
                                to.toTypedArray()
                            )
                            if (cc.size > 0) {
                                emailIntent.putExtra(
                                    Intent.EXTRA_CC,
                                    to.toTypedArray()
                                )

                            }
                            if (bcc.size > 0) {
                                emailIntent.putExtra(
                                    Intent.EXTRA_BCC,
                                    to.toTypedArray()
                                )

                            }
                            if (subject != null) {
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
                            }
                            if (body != null) {
                                emailIntent.putExtra(Intent.EXTRA_TEXT, body)
                            }
                            view!!.context.startActivity(emailIntent)
                            return true
                        } catch (e: UnsupportedEncodingException) {
                            /* Won't happen*/
                        }
                    } else {
                        view!!.loadUrl(url)
                        return true
                    }
                    return false
                } else {
                    tryAgainPage(url)
                }
                return false
            }

            // Use this method for API level 24 and above
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?,
            ): Boolean {
                // Handle the URL loading here
                var url = request?.url.toString()
                if (isConnected()) {
                    if (url.startsWith(applicationContext.getString(R.string.google_play_url))) {
                        view!!.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                        return true
                    }
                    if (url.startsWith(applicationContext.getString(R.string.facebook_url))) {
                        view!!.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                        return true
                    }
                    if (url.startsWith("tel:")) {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse(url))
                        view!!.context.startActivity(intent)
                        return true
                    }
                    if (url.contains(applicationContext.getString(R.string.instagram_url))) {
                        val uri = Uri.parse("http://instagram.com/")
                        val likeIng = Intent(Intent.ACTION_VIEW, uri)
                        likeIng.setPackage(applicationContext.getString(R.string.instagram_package))
                        try {
                            view!!.context.startActivity(likeIng)
                        } catch (e: ActivityNotFoundException) {
                            view!!.context.startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("http://instagram.com/")
                                )
                            )
                        }
                        return true
                    }
                    if (url.startsWith("market://")) {
                        view!!.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                        return true
                    }
                    if (url.startsWith("https://api")) {
                        view!!.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                        return true
                    }
                    if (url.startsWith("mailto:")) {
                        try {
                            val to: MutableList<String> = ArrayList()
                            val cc: MutableList<String> = ArrayList()
                            val bcc: MutableList<String> = ArrayList()
                            var subject: String? = null
                            var body: String? = null
                            url = url.replaceFirst("mailto:".toRegex(), "")
                            val urlSections =
                                url.split("&".toRegex()).dropLastWhile { it.isEmpty() }
                                    .toTypedArray()
                            if (urlSections.size >= 2) {
                                to.addAll(
                                    Arrays.asList(
                                        *urlSections[0].split(",".toRegex())
                                            .dropLastWhile { it.isEmpty() }
                                            .toTypedArray()))
                                for (i in 1 until urlSections.size) {
                                    val urlSection = urlSections[i]
                                    val keyValue = urlSection.split("=".toRegex())
                                        .dropLastWhile { it.isEmpty() }
                                        .toTypedArray()
                                    if (keyValue.size == 2) {
                                        val key = keyValue[0]
                                        var value: String? = keyValue[1]
                                        value = URLDecoder.decode(value, "UTF-8")
                                        if (key == "cc") {
                                            cc.addAll(
                                                Arrays.asList(
                                                    *url.split(",".toRegex())
                                                        .dropLastWhile { it.isEmpty() }
                                                        .toTypedArray()))
                                        } else if (key == "bcc") {
                                            bcc.addAll(
                                                Arrays.asList(
                                                    *url.split(",".toRegex())
                                                        .dropLastWhile { it.isEmpty() }
                                                        .toTypedArray()))
                                        } else if (key == "subject") {
                                            subject = value
                                        } else if (key == "body") {
                                            body = value
                                        }
                                    }
                                }
                            } else {
                                to.addAll(
                                    Arrays.asList(
                                        *url.split(",".toRegex()).dropLastWhile { it.isEmpty() }
                                            .toTypedArray()))
                            }
                            val emailIntent = Intent(Intent.ACTION_SEND)
                            emailIntent.setType("message/rfc822")
                            if (cc.size > 0) {
                                emailIntent.putExtra(
                                    Intent.EXTRA_CC,
                                    to.toTypedArray()
                                )

                            }
                            if (bcc.size > 0) {
                                emailIntent.putExtra(
                                    Intent.EXTRA_BCC,
                                    to.toTypedArray()
                                )

                            }
                            if (subject != null) {
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
                            }
                            if (body != null) {
                                emailIntent.putExtra(Intent.EXTRA_TEXT, body)
                            }
                            view!!.context.startActivity(emailIntent)
                            return true
                        } catch (e: UnsupportedEncodingException) {
                            /* Won't happen*/
                        }
                    } else {
                        view!!.loadUrl(url)
                        return true
                    }
                    return false
                } else {
                    tryAgainPage(url)
                }
                return false
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                // Called when the page starts loading
                super.onPageStarted(view, url, favicon)
                Log.i(TAG, "shouldOverrideUrlLoading: URL +> $url")

                if (RemoteConfigUtils.enableLoader()) {
                    progressDialog.showProgress(view!!.context, true)
                }
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                // Called when the page finishes loading
                super.onPageFinished(view, url)
                progressDialog.hideProgress()

            }

            override fun onReceivedError(
                view: WebView?,
                errorCode: Int,
                description: String?,
                failingUrl: String?,
            ) {
                // Called when an error occurs
                Log.i(TAG, "onReceivedError description : $description")
            }
        }
        if (isConnected()) {
            if (url != null) {
                webView.loadUrl(url!!)
            } else {
                webView.loadUrl(webUrl)
            }
        } else {
            tryAgainPage(webUrl)
        }


    }
    override fun onResume() {
        super.onResume()

    }

    @SuppressLint("ObsoleteSdkInt")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
                super.onActivityResult(requestCode, resultCode, data)
                return
            }
            var results: Array<Uri?>? = null
            // Check that the response is a good one
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    // If there is not data, then we may have taken a photo
                    if (mCameraPhotoPath != null) {
                        results = arrayOf(Uri.parse(mCameraPhotoPath))
                    }
                } else {
                    val dataString = data.dataString
                    val clipData = data.clipData
                    if (clipData != null) {
                        results = arrayOfNulls(clipData.itemCount)
                        for (i in 0 until clipData.itemCount) {
                            val item = clipData.getItemAt(i)
                            results[i] = item.uri
                        }
                    }
                    if (dataString != null) {
                        results = arrayOf(Uri.parse(dataString))
                    }
                }
            }
            mFilePathCallback?.onReceiveValue(results as Array<Uri>)
            mFilePathCallback = null
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            if (requestCode != FILE_CHOOSER_RESULT_CODE || mUploadMessage == null) {
                super.onActivityResult(requestCode, resultCode, data)
                return
            }
            var result: Uri? = null
            try {
                if (resultCode != RESULT_OK) {
                } else {
                    // retrieve from the private variable if the intent is null
                    result = if (data == null) mCapturedImageURI else data.data
                }
            } catch (e: Exception) {
                Toast.makeText(
                    applicationContext, "activity :$e",
                    Toast.LENGTH_LONG
                ).show()
            }
            mUploadMessage!!.onReceiveValue(result)
            mUploadMessage = null
        }
    }


    override fun onRefresh() {
        if (isConnected()) {
            Handler().postDelayed({
                webView.reload()
                mySwipeRefreshLayout.isRefreshing = false
            }, 2000)
        } else {
            tryAgainPage(url)
        }
    }

    override fun onScrollChanged() {
        mySwipeRefreshLayout.isEnabled = webView.getScrollY() === 0
    }
    fun verifyPermissions(activity: Activity): Boolean {
        // Check if we have write permission
        val writePermission =
            ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (writePermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                activity,
                PERMISSIONS_REQ,
                REQUEST_CODE_PERMISSION
            )
            return false
        } else {
            return true
        }
    }

    private fun isConnected(): Boolean {
        val connMgr: ConnectivityManager =
            this.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        var networkInfo: NetworkInfo? = null
        networkInfo = connMgr.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    fun tryAgainPage(url: String?) {
        val intent = Intent(
            applicationContext,
            NoInternetConnectionActivity::class.java
        )
        intent.putExtra("url", url)
        startActivity(intent)
        finish()
    }


    private fun handleAudioPermissionRequest(request: PermissionRequest) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission already granted, allow audio recording
            request.grant(request.resources)
        } else {
            // Permission not granted, request permission
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.RECORD_AUDIO),
                RECORD_AUDIO_PERMISSION_REQUEST_CODE
            )
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1001 -> {
                //if permission is cancel result array would be empty
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission was granted
                    if (mGeoLocationCallback != null) {
                        mGeoLocationCallback!!.invoke(mGeoLocationRequestOrigin, true, true)
                    }
                } else {
                    //permission denied
                    if (mGeoLocationCallback != null) {
                        mGeoLocationCallback!!.invoke(mGeoLocationRequestOrigin, false, false)
                    }
                }
            }

            RECORD_AUDIO_PERMISSION_REQUEST_CODE -> {
                // Audio permission request
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    // Handle audio permission granted
                    webView.reload()
                } else {
                    // Permission denied
                    // Handle audio permission denied
                }
            }

        }
    }



    private fun animateNavDrawerBackground(colorFrom: Int, colorTo: Int, textColor: Int) {
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
        colorAnimation.setDuration(250) // milliseconds
        colorAnimation.addUpdateListener { animator ->
            navigationView.setBackgroundColor(animator.animatedValue as Int)
            navigationView.itemTextColor = ColorStateList.valueOf(textColor)
            navigationView.itemIconTintList = ColorStateList.valueOf(textColor)
        }
        colorAnimation.start()
    }

    private fun animateToolBarBackground(colorFrom: Int, colorTo: Int) {
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
        colorAnimation.setDuration(250) // milliseconds
        colorAnimation.addUpdateListener { animator -> toolbar.setBackgroundColor(animator.animatedValue as Int) }
        colorAnimation.start()
    }

    private fun statusBarNightColor() {
        window.statusBarColor = resources.getColor(R.color.dark_color, this.theme)
    }

    private fun statusBarNormalColor() {
        window.statusBarColor = resources.getColor(R.color.colorPrimary, this.theme)
    }

    override fun onStop() {
        super.onStop()
        mySwipeRefreshLayout.viewTreeObserver.removeOnScrollChangedListener(this)
    }

    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        )
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.CAMERA
        )
        if (shouldProvideRationale) {
            Log.i(
                "WebViewActivity.TAG",
                "Displaying permission rationale to provide additional context."
            )
        } else {
            Log.i("WebViewActivity.TAG", "Requesting permission")
            ActivityCompat.requestPermissions(
                this@WebViewActivity, arrayOf(Manifest.permission.CAMERA),
                REQUEST_PERMISSIONS_REQUEST_CODE
            )
        }
    }

    private fun isValidColor(color: String): Boolean {
        val colorPattern =
            Pattern.compile("#([0-9a-f]{3}|[0-9a-f]{6}|[0-9a-f]{8}|[0-9A-F]{3}|[0-9A-F]{6}|[0-9A-F]{8})")
        val m = colorPattern.matcher(color)
        return m.matches()
    }


    companion object {
        var USER_AGENT_POSTFIX =
            "SPWAndroid" // useful for identifying traffic, e.g. in Google Analytics

        var CUSTOM_USER_AGENT =
            "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Mobile Safari/537.36" // custom user-agent
        private const val RECORD_AUDIO_PERMISSION_REQUEST_CODE = 123
        private const val REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 101
        private const val REQUEST_PERMISSIONS_REQUEST_CODE = 34
        private const val REQUEST_CODE_PERMISSION = 2
        private val PERMISSIONS_REQ = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

}

