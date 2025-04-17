package com.coderscage.evolutionofgames.Utilities

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.net.http.SslError
import android.util.Log
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.coderscage.evolutionofgames.Activity.NoInternetConnectionActivity
import com.coderscage.evolutionofgames.R
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.util.*


class WebViewClientImpl(activity: Activity?) : WebViewClient() {
    private var activity: Activity? = null
    private var progressDialog: CustomProgress = CustomProgress.getInstance()
    var url: String? = null

    init {
        this.activity = activity
    }

    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        var url = request.url.toString()
        if (isConnected(view)) {
            if (url == null) {
                return false
            }
            if (url.startsWith(activity!!.getString(R.string.facebook_url))) {
                view.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                return true
            }
            if (url.startsWith("tel:")) {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse(url))
                view.context.startActivity(intent)
                return true
            }
            if (url.contains(activity!!.getString(R.string.instagram_url))) {
                val uri = Uri.parse("http://instagram.com/")
                val likeIng = Intent(Intent.ACTION_VIEW, uri)
                likeIng.setPackage(activity!!.getString(R.string.instagram_package))
                try {
                    view.context.startActivity(likeIng)
                } catch (e: ActivityNotFoundException) {
                    view.context.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/")
                        )
                    )
                }
                return true
            }
            if (url.startsWith("market://")) {
                view.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                return true
            }
            if (url.startsWith("https://api")) {
                view.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
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
                    val urlSections = url.split("&".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                    if (urlSections.size >= 2) {
                        to.addAll(
                            Arrays.asList(
                                *urlSections[0].split(",".toRegex()).dropLastWhile { it.isEmpty() }
                                    .toTypedArray()))
                        for (i in 1 until urlSections.size) {
                            val urlSection = urlSections[i]
                            val keyValue =
                                urlSection.split("=".toRegex()).dropLastWhile { it.isEmpty() }
                                    .toTypedArray()
                            if (keyValue.size == 2) {
                                val key = keyValue[0]
                                var value: String? = keyValue[1]
                                value = URLDecoder.decode(value, "UTF-8")
                                if (key == "cc") {
                                    cc.addAll(
                                        Arrays.asList(
                                            *url.split(",".toRegex()).dropLastWhile { it.isEmpty() }
                                                .toTypedArray()))
                                } else if (key == "bcc") {
                                    bcc.addAll(
                                        Arrays.asList(
                                            *url.split(",".toRegex()).dropLastWhile { it.isEmpty() }
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
//                    val emailIntent = Intent(Intent.ACTION_SEND)
//                    emailIntent.setType("message/rfc822")
//                    val dummyStringArray = arrayOfNulls<String>(0) // For list to array conversion
//                    emailIntent.putExtra(Intent.EXTRA_EMAIL, to.toArray<String>(dummyStringArray))
//                    if (cc.size > 0) {
//                        emailIntent.putExtra(Intent.EXTRA_CC, cc.toArray<String>(dummyStringArray))
//                    }
//                    if (bcc.size > 0) {
//                        emailIntent.putExtra(
//                            Intent.EXTRA_BCC,
//                            bcc.toArray<String>(dummyStringArray)
//                        )
//                    }
//                    if (subject != null) {
//                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
//                    }
//                    if (body != null) {
//                        emailIntent.putExtra(Intent.EXTRA_TEXT, body)
//                    }
//                    view.context.startActivity(emailIntent)
                    return true
                } catch (e: UnsupportedEncodingException) {
                    /* Won't happen*/
                }
            } else {
                view.loadUrl(url)
                return true
            }
            return false
        } else {
            tryAgainPage(view, url)
        }
        return false
    }

    override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
        super.onPageStarted(view, url, favicon)
        if (RemoteConfigUtils.enableLoader()) {
            progressDialog.showProgress(view.context, true)
        }

        // edit this
    }

    override fun onPageFinished(view: WebView, url: String) {
        progressDialog.hideProgress()
    }

    @Suppress("deprecation")
    override fun shouldInterceptRequest(view: WebView, url: String): WebResourceResponse? {
        return super.shouldInterceptRequest(view, url)
    }

    private fun isConnected(view: View): Boolean {
        val connMgr =
            view.context.getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
        var networkInfo: NetworkInfo? = null
        if (connMgr != null) {
            networkInfo = connMgr.activeNetworkInfo
        }
        return if (networkInfo != null && networkInfo.isConnected) true else false
    }

    fun tryAgainPage(view: View, url: String?) {
        val intent = Intent(
            view.context,
            NoInternetConnectionActivity::class.java
        )
        intent.putExtra("url", url)
        view.context.startActivity(intent)
        activity!!.finish()
    }

    override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
        super.onReceivedSslError(view, handler, error)
        Log.i(ContentValues.TAG, "onReceivedSslError: $error")
    }

    override fun onReceivedError(
        view: WebView,
        errorCode: Int,
        description: String,
        failingUrl: String,
    ) {
        Log.i(ContentValues.TAG, "onReceivedError: $description")
    }
}