package dev.lordyorden.tradely.utilities

import android.content.Context
import okhttp3.Callback
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.ref.WeakReference

class HttpRequestHandler private constructor(context: Context) {

    companion object {

        @Volatile
        private var instance: HttpRequestHandler? = null

        fun getInstance(): HttpRequestHandler {
            return instance
                ?: throw IllegalStateException("HttpRequestHandler must be initialized by calling init(context) before use")
        }

        fun init(context: Context): HttpRequestHandler {
            return instance ?: synchronized(this) {
                instance ?: HttpRequestHandler(context).also { instance = it }
            }
        }
    }

    private val contextRef = WeakReference(context)
    private val client = OkHttpClient()

    private fun makeRequest(url: String, args: Map<String, String>): Request {
        //?where=label_en like "United States"&limit=3
        //"https://public.opendatasoft.com/api/explore/v2.1/catalog/datasets/countries-codes/records"

        val uri = (buildString {
            append(url)
            if (args.isNotEmpty()) {
                append("?")

                args.forEach { (key, value) ->
                    append(key)
                    append("=")
                    append(value)
                    append("&")
                }
            }
        }).toHttpUrl().toUri().toASCIIString()


        val request = Request.Builder()
            .url(uri)
            .build()
        return request
    }

    fun get(url: String, args: Map<String, String>, callback: Callback){
        val request = makeRequest(url, args)
        client.newCall(request).enqueue(callback)
    }

    fun get(url: String, args: Map<String, String>): String?{
        val request = makeRequest(url, args)
        client.newCall(request).execute().use { response ->
            return response.body?.string()
        }
    }

//    fun loadImage(
//        source: Drawable,
//        imageView: AppCompatImageView,
//        placeholder: Int = R.drawable.unavailable_photo
//    ) {
//        contextRef.get()?.let { context ->
//            Glide
//                .with(context)
//                .load(source)
//                .centerCrop()
//                .placeholder(placeholder)
//                .into(imageView)
//        }
//    }
//
//    fun loadImage(
//        source: String,
//        imageView: AppCompatImageView,
//        placeholder: Int = R.drawable.unavailable_photo
//    ) {
//        contextRef.get()?.let { context ->
//            Glide
//                .with(context)
//                .load(source)
//                .centerCrop()
//                .placeholder(placeholder)
//                .into(imageView)
//        }
//    }

}