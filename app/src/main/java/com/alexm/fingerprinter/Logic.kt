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
import java.io.File

class Logic: Contract.ILogic {
    override fun saveImages(context: Context, drawable: Drawable?): Observable<String> {
        return Observable.defer({
            var outout:String = "not initialized"
            try {
                val storage = Storage(context)
                val path = storage.externalStorageDirectory
                storage.createDirectory("$path${File.separator}MyFolder", false)
                val bitmap: Bitmap
                if (drawable is BitmapDrawable) {
                    bitmap = drawable.bitmap
                    storage.createFile("$path${File.separator}MyFolder${File.separator}FingerDraw.bmp", bitmap)
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