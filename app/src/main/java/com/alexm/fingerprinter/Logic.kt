package com.alexm.fingerprinter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.alexm.fingerprinter.Interfaces.Contract
import com.snatik.storage.Storage
import io.reactivex.Observable
import io.reactivex.Observable.just
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import tech.picnic.fingerpaintview.R
import java.io.File

class Logic: Contract.ILogic {

    private val FILE_KEY:String = "Preferences file"
    private val PREFS_KEY:String = "Preferences key"

    override fun saveImages(context: Context, drawable: Drawable?): Observable<String> {
        val sharedPref =  context.getSharedPreferences(FILE_KEY, Context.MODE_PRIVATE)
        var count = sharedPref.getInt(PREFS_KEY, 0)
        return Observable.defer({
            var outout:String = "not initialized"
            try {
                val storage = Storage(context)
                val path = storage.externalStorageDirectory
                storage.createDirectory("$path${File.separator}MyFolder", false)
                val bitmap: Bitmap
                if (drawable is BitmapDrawable) {
                    bitmap = drawable.bitmap
                    storage.createFile("$path${File.separator}MyFolder${File.separator}FingerDraw$count.bmp", bitmap)
                    count++
                    with (sharedPref.edit()) {
                        putInt(PREFS_KEY, count)
                        apply()
                    }
                    outout = "All green"
                }
            }catch (e:Exception){
                e.printStackTrace()
                outout = e.toString()
            }
            return@defer just(outout)
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}