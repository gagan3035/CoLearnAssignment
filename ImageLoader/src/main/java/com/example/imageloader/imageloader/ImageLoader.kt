package com.example.imageloader.imageloader

import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.util.LruCache
import android.widget.ImageView
import java.util.*
import java.util.Collections.synchronizedMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ImageLoader (context: Context)  {

    private val maxCacheSize: Int = (Runtime.getRuntime().maxMemory() / 1024).toInt()/8
    private val inMemoryCache: LruCache<String, Bitmap>

    private val executorService: ExecutorService

    private val imageViewMap = synchronizedMap(WeakHashMap<ImageView, String>())
    private val handler: Handler

    init {
        inMemoryCache = object : LruCache<String, Bitmap>(maxCacheSize) {
            override fun sizeOf(key: String, bitmap: Bitmap): Int {
                return bitmap.byteCount / 1024
            }
        }

        executorService = Executors.newFixedThreadPool(5,
            ImageLoaderHelper.ImageBackgroundThreadFactory()
        )
        handler = Handler()

        val metrics = context.resources.displayMetrics
        screenWidth = metrics.widthPixels
        screenHeight = metrics.heightPixels


    }

    companion object {

        private var INSTANCE: ImageLoader? = null

        internal var screenWidth = 0
        internal var screenHeight = 0

        @Synchronized
        fun with(context: Context): ImageLoader {

            require(context != null) {
                "Context should not be null."
            }

            return INSTANCE ?: ImageLoader(context).also {
                INSTANCE = it
            }

        }
    }

    fun load(imageView: ImageView, imageUrl: String) {

        require(imageView != null) {
            "ImageView should not be null."
        }

        require(imageUrl != null && imageUrl.isNotEmpty()) {
            "Image Url should not be empty"
        }

        imageView.setImageResource(0)
        imageViewMap[imageView] = imageUrl

        val bitmap = checkInCache(imageUrl)
        bitmap?.let {
            loadImageIntoImageView(imageView, it, imageUrl)
        } ?: run {
            executorService.submit(PhotosLoader(ImageRequest(imageUrl, imageView)))
        }
    }

    @Synchronized
    private  fun loadImageIntoImageView(imageView: ImageView, bitmap: Bitmap?, imageUrl: String) {

        require(bitmap != null) {
            "Bitmap should not be null"
        }

        val scaledBitmap =
            ImageLoaderHelper.scaleBitmapForLoad(bitmap, imageView.width, imageView.height)

        scaledBitmap?.let {
            if(!isImageViewReused(ImageRequest(imageUrl, imageView))) imageView.setImageBitmap(scaledBitmap)
        }
    }

    private fun isImageViewReused(imageRequest: ImageRequest): Boolean {
        val tag = imageViewMap[imageRequest.imageView]
        return tag == null || tag != imageRequest.imgUrl
    }

    @Synchronized
    private fun checkInCache(imageUrl: String): Bitmap? = inMemoryCache.get(imageUrl)

    inner class DisplayBitmap(private var imageRequest: ImageRequest) : Runnable {
        override fun run() {
            if(!isImageViewReused(imageRequest)) loadImageIntoImageView(imageRequest.imageView, checkInCache(imageRequest.imgUrl), imageRequest.imgUrl)
        }
    }

    inner class ImageRequest(var imgUrl: String, var imageView: ImageView)

    inner class PhotosLoader(private var imageRequest: ImageRequest) : Runnable {

        override fun run() {

            if(isImageViewReused(imageRequest)) return

            val bitmap = ImageLoaderHelper.downloadBitmapFromURL(imageRequest.imgUrl)
            inMemoryCache.put(imageRequest.imgUrl, bitmap)

            if(isImageViewReused(imageRequest)) return

            val displayBitmap = DisplayBitmap(imageRequest)
            handler.post(displayBitmap)
        }
    }
}


