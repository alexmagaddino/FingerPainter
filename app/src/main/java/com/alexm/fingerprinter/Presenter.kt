package com.alexm.fingerprinter

import android.content.Context
import android.graphics.drawable.Drawable
import com.alexm.fingerprinter.Interfaces.Contract.*

class Presenter: IPresenter{

    private var v: IView? = null
    private val logic = Logic()

    override fun subscribe(v: IView) {
        this.v = v
    }

    override fun unsubscribe() {
        v = null
    }

    override fun saveData(context: Context, drawable: Drawable?) {
        logic.saveImages(context, drawable)
                .subscribe({t -> v?.saveMessage(t) },{e -> e.printStackTrace()})
    }
}