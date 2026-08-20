package com.example.ads

import android.app.Activity
import android.content.Context
import android.graphics.Color as AndroidColor
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

object AdsConfig {
    // Production AdMob IDs for Speed Math Warrior.
    const val APP_ID = "ca-app-pub-5046472701966472~2574787864"
    const val NATIVE_AD_UNIT_ID = "ca-app-pub-5046472701966472/9372025303"
    const val INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-5046472701966472/8880392096"
}

class AdsManager(context: Context) {
    private val appContext = context.applicationContext
    private var interstitialAd: InterstitialAd? = null
    private var completedSessionsSinceAd = 0

    init {
        MobileAds.initialize(appContext)
        loadInterstitial()
    }

    private fun loadInterstitial() {
        InterstitialAd.load(
            appContext,
            AdsConfig.INTERSTITIAL_AD_UNIT_ID,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            interstitialAd = null
                            loadInterstitial()
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: com.google.android.gms.ads.AdError) {
                            interstitialAd = null
                            loadInterstitial()
                        }
                    }
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    interstitialAd = null
                }
            }
        )
    }

    /** Show an interstitial after every 2 completed practice sessions. */
    fun showAfterPractice(activity: Activity) {
        completedSessionsSinceAd++
        if (completedSessionsSinceAd < 2) return

        completedSessionsSinceAd = 0
        val ad = interstitialAd ?: run {
            loadInterstitial()
            return
        }
        ad.show(activity)
    }
}

@Composable
fun NativeAdCard(modifier: Modifier = Modifier) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val nativeAdView = remember { createNativeAdView(context) }

    DisposableEffect(nativeAdView) {
        loadNativeAd(nativeAdView)
        onDispose { nativeAdView.destroy() }
    }

    AndroidView(
        factory = { nativeAdView },
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 120.dp)
            .padding(vertical = 4.dp)
    )
}

private fun createNativeAdView(context: Context): NativeAdView {
    val adView = NativeAdView(context)
    adView.setBackgroundColor(AndroidColor.TRANSPARENT)

    val root = LinearLayout(context).apply {
        orientation = LinearLayout.VERTICAL
        setPadding(18, 14, 18, 14)
        background = android.graphics.drawable.GradientDrawable().apply {
            setColor(AndroidColor.WHITE)
            cornerRadius = 28f
            setStroke(1, AndroidColor.rgb(220, 235, 228))
        }
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    val headline = TextView(context).apply {
        textSize = 15f
        setTextColor(AndroidColor.rgb(20, 30, 26))
        setTypeface(typeface, android.graphics.Typeface.BOLD)
    }
    val body = TextView(context).apply {
        textSize = 12f
        setTextColor(AndroidColor.rgb(80, 90, 86))
        setPadding(0, 6, 0, 8)
    }
    val media = MediaView(context).apply {
        layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            140
        )
    }
    val cta = Button(context).apply {
        text = "Learn More"
        setTextColor(AndroidColor.WHITE)
        setBackgroundColor(AndroidColor.rgb(0, 120, 80))
    }

    root.addView(headline)
    root.addView(body)
    root.addView(media)
    root.addView(cta)
    adView.addView(root)

    adView.headlineView = headline
    adView.bodyView = body
    adView.mediaView = media
    adView.callToActionView = cta
    return adView
}

private fun loadNativeAd(adView: NativeAdView) {
    val loader = AdLoader.Builder(adView.context, AdsConfig.NATIVE_AD_UNIT_ID)
        .forNativeAd { nativeAd: NativeAd ->
            (adView.headlineView as? TextView)?.text = nativeAd.headline ?: "Sponsored"
            (adView.bodyView as? TextView)?.text = nativeAd.body ?: "Discover something useful."
            (adView.callToActionView as? Button)?.text = nativeAd.callToAction ?: "Learn More"
            adView.mediaView?.mediaContent = nativeAd.mediaContent
            adView.setNativeAd(nativeAd)
        }
        .withAdListener(object : AdListener() {
            override fun onAdFailedToLoad(error: LoadAdError) {
                adView.visibility = android.view.View.GONE
            }
        })
        .withNativeAdOptions(NativeAdOptions.Builder().build())
        .build()

    loader.loadAd(AdRequest.Builder().build())
}
