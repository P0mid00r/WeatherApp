package ru.pomidorka.weatherapp

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.imageLoader
import coil3.request.CachePolicy
import coil3.request.crossfade

class App : Application(), SingletonImageLoader.Factory {
    override fun newImageLoader(context: PlatformContext): ImageLoader {
        return ImageLoader(this).newBuilder()
            .memoryCachePolicy(CachePolicy.DISABLED)
            .diskCachePolicy(CachePolicy.DISABLED)
            .crossfade(true)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        imageLoader.memoryCache?.clear()
        imageLoader.diskCache?.clear()
    }
}