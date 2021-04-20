package org.segura.triptip.fragments

import com.mapzen.tangram.networking.DefaultHttpHandler
import okhttp3.*
import java.io.File
import java.util.concurrent.TimeUnit

private const val maxCacheSize = 32 * 1024 * 1024 // 32 MB
private const val maxDaysStale = 7
private const val cachedHostName = "tile.nextzen.com"

fun configureBuilder(cacheDirectory: File?): OkHttpClient.Builder {
    val builder = DefaultHttpHandler.getClientBuilder()
    if (cacheDirectory != null && cacheDirectory.exists()) {
        builder.cache(Cache(cacheDirectory, maxCacheSize.toLong()))
    }
    return builder
}

class CachingHttpHandler(cacheDirectory: File?) : DefaultHttpHandler(configureBuilder(cacheDirectory)) {

    private val tileCacheControl = CacheControl.Builder().maxStale(maxDaysStale, TimeUnit.DAYS).build()

    override fun configureRequest(url: HttpUrl, builder: Request.Builder) {
        if (cachedHostName == url.host()) {
            builder.cacheControl(tileCacheControl)
        }
    }
}
