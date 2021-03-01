package com.example.colearn.Utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created by Gagan on 27/02/21.
 */
@Singleton
class ImageUploader @Inject constructor(@ApplicationContext private  var context: Context) {

    fun loadImage(url:String,view:ImageView){
        Glide.with(context).load(url).into(view)
    }
}