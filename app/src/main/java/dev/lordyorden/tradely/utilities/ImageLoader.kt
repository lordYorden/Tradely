package dev.lordyorden.tradely.utilities

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import dev.lordyorden.tradely.R
import java.lang.ref.WeakReference

class ImageLoader private constructor(context: Context) {

    companion object {

        @Volatile
        private var instance: ImageLoader? = null

        fun getInstance(): ImageLoader {
            return instance
                ?: throw IllegalStateException("ImageLoader must be initialized by calling init(context) before use")
        }

        fun init(context: Context): ImageLoader {
            return instance ?: synchronized(this) {
                instance ?: ImageLoader(context).also { instance = it }
            }
        }
    }

    private val contextRef = WeakReference(context)

    fun loadImage(
        source: Drawable,
        imageView: AppCompatImageView,
        placeholder: Int = R.drawable.unavailable_photo
    ) {
        contextRef.get()?.let { context ->
            Glide
                .with(context)
                .load(source)
                .centerCrop()
                .placeholder(placeholder)
                .into(imageView)
        }
    }

    fun loadImage(
        source: String,
        imageView: AppCompatImageView,
        placeholder: Int = R.drawable.unavailable_photo
    ) {
        contextRef.get()?.let { context ->
            Glide
                .with(context)
                .load(source)
                .centerCrop()
                .placeholder(placeholder)
                .into(imageView)
        }
    }

}