package org.segura.triptip.fragments

import com.android.volley.Cache
import com.android.volley.NetworkResponse
import com.android.volley.ParseError
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

const val CACHE_HIT_REFRESHED: Long = 3 * 60 * 1000 // 3 min
const val CACHE_EXPIRED: Long = 3 * 24 * 60 * 60 * 1000 // 3 days

class CachedJSONObjectResponse(
    url: String?,
    listener: Response.Listener<JSONObject>?,
    errorListener: Response.ErrorListener?
) : JsonObjectRequest(url, null, listener, errorListener) {

    var cacheExpiredFrom: Long = System.currentTimeMillis()

    override fun parseNetworkResponse(response: NetworkResponse): Response<JSONObject> {
        return try {
            val cacheEntry: Cache.Entry = HttpHeaderParser.parseCacheHeaders(response) ?: Cache.Entry()
            cacheEntry.data = response.data
            cacheEntry.softTtl = cacheExpiredFrom + CACHE_HIT_REFRESHED
            cacheEntry.ttl = cacheExpiredFrom + CACHE_EXPIRED
            response.headers!!["Date"]?.let {
                cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(it)
            }
            response.headers!!["Last-Modified"]?.let {
                cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(it)
            }
            cacheEntry.responseHeaders = response.headers
            val jsonString = String(
                response.data,
                Charset.forName(HttpHeaderParser.parseCharset(response.headers))
            )
            Response.success(JSONObject(jsonString), cacheEntry)
        } catch (e: UnsupportedEncodingException) {
            Response.error(ParseError(e))
        } catch (e: JSONException) {
            Response.error(ParseError(e))
        }
    }
}
