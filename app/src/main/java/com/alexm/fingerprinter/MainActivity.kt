package com.alexm.fingerprinter

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.*
import tech.picnic.fingerpaintview.R

class MainActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        red.setOnSeekBarChangeListener(this)
        green.setOnSeekBarChangeListener(this)
        blue.setOnSeekBarChangeListener(this)
        tolerance.setOnSeekBarChangeListener(this)
        width.setOnSeekBarChangeListener(this)

        clear.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view){
            clear -> finger.clear()
        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, value: Int, p2: Boolean) {
        if(seekBar?.id == red.id||seekBar?.id == green.id||seekBar?.id == blue.id){
            val r = red.progress
            val g = green.progress
            val b = blue.progress
            val color = Color.rgb(r,g,b)
            finger.strokeColor = color
            colorPreview.setBackgroundColor(color)
        }else if(seekBar?.id == tolerance.id)
            finger.touchTolerance = tolerance.progress.toFloat()
        else if(seekBar?.id == width.id)
            finger.strokeWidth = width.progress.toFloat()
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {

    }
}