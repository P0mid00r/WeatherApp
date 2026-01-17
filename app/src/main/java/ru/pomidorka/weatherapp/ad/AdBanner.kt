package ru.pomidorka.weatherapp.ad

import android.content.res.Resources
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import com.yandex.mobile.ads.banner.BannerAdSize
import com.yandex.mobile.ads.banner.BannerAdView
import com.yandex.mobile.ads.common.AdRequest

fun getDisplayWidth(): Int = Resources.getSystem().displayMetrics.widthPixels

@Composable
private fun AdBanner(
    modifier: Modifier,
    id: String,
    color: Color = Color.Transparent,
    widthBanner: Int = getDisplayWidth(),
    adBannerContext: ((BannerAdView) -> Unit)? = null,
) {
    var adBanner by remember { mutableStateOf<BannerAdView?>(null) }

    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .background(color),
        factory = {
            val banner = BannerAdView(it).apply {
                setAdUnitId(id)
                setAdSize(BannerAdSize.inlineSize(context, widthBanner, 85))
                val adRequest: AdRequest = AdRequest.Builder().build()
                loadAd(adRequest)
            }
            adBannerContext?.invoke(banner)
            adBanner = banner
            return@AndroidView banner
        }
    )

    DisposableEffect(Unit) {
        onDispose {
            adBanner?.destroy()
        }
    }
}