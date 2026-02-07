package ru.pomidorka.weatherapp.ad

import android.app.Activity
import com.yandex.mobile.ads.appopenad.AppOpenAd
import com.yandex.mobile.ads.appopenad.AppOpenAdEventListener
import com.yandex.mobile.ads.appopenad.AppOpenAdLoadListener
import com.yandex.mobile.ads.appopenad.AppOpenAdLoader
import com.yandex.mobile.ads.common.AdError
import com.yandex.mobile.ads.common.AdRequestConfiguration
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData

object OpenAppAd {
    private var isShowed = false
    private const val ID = AdId.OPEN_AD_ID
    private var mAppOpenAd: AppOpenAd? = null

    fun load(activity: Activity) {
        if (isShowed) return

        fun clearAppOpenAd() {
            mAppOpenAd?.setAdEventListener(null)
            mAppOpenAd = null
        }

        fun showAppOpenAd() {
            mAppOpenAd?.show(activity)
            isShowed = true
        }

        val appOpenAdLoader = AppOpenAdLoader(activity)
        val adRequestConfiguration = AdRequestConfiguration.Builder(ID).build()

        val appOpenAdLoadListener: AppOpenAdLoadListener = object : AppOpenAdLoadListener {
            override fun onAdLoaded(appOpenAd: AppOpenAd) {
                mAppOpenAd = appOpenAd
                showAppOpenAd()
            }

            override fun onAdFailedToLoad(error: AdRequestError) { }
        }

        val appOpenAdEventListener = object : AppOpenAdEventListener {
            override fun onAdShown() {
                showAppOpenAd()
            }

            override fun onAdDismissed() {
                clearAppOpenAd()
            }

            override fun onAdClicked() { }

            override fun onAdFailedToShow(adError: AdError) { }

            override fun onAdImpression(impressionData: ImpressionData?) { }
        }

        mAppOpenAd?.setAdEventListener(appOpenAdEventListener)
        appOpenAdLoader.setAdLoadListener(appOpenAdLoadListener)
        appOpenAdLoader.loadAd(adRequestConfiguration)
    }
}