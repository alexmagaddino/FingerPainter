package com.alexm.fingerprinter.Interfaces

import android.content.Context
import android.graphics.drawable.Drawable
import io.reactivex.Observable

interface Contract{
    interface IView{
        fun saveMessage(output: String)
    }
    interface IPresenter{
        fun subscribe(v: IView)
        fun unsubscribe()
        fun saveData(context: Context, drawable: Drawable?)
    }
    interface ILogic{
        fun saveImages(context: Context, drawable: Drawable?): Observable<String>
    }
}