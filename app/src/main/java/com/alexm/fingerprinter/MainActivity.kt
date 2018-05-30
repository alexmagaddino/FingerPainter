package com.alexm.fingerprinter

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import tech.picnic.fingerpaintview.R
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import android.content.Context
import com.alexm.fingerprinter.Interfaces.Contract


class MainActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener, View.OnClickListener, Contract.IView {

    private val PERMISSION_REQUEST_CODE:Int = 1000
    private val presenter = Presenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        alpha.setOnSeekBarChangeListener(this)
        red.setOnSeekBarChangeListener(this)
        green.setOnSeekBarChangeListener(this)
        blue.setOnSeekBarChangeListener(this)
        tolerance.setOnSeekBarChangeListener(this)
        width.setOnSeekBarChangeListener(this)

        clear.setOnClickListener(this)
        undo.setOnClickListener(this)
        save.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view){
            clear -> finger.clear()
            undo -> finger.undo()
            save -> checkPermission()
        }
    }

    override fun saveMessage(output:String) {
        Toast.makeText(this, output, Toast.LENGTH_SHORT).show()
    }


    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager
                        .PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_CODE)
        }else{
            presenter.saveData(applicationContext, finger.drawable)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            PERMISSION_REQUEST_CODE ->
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    presenter.saveData(applicationContext, finger.drawable)
                else
                    Toast.makeText(this, "Se non mi dai i permessi non posso scrivere, cornuto", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.subscribe(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.unsubscribe()
    }

    override fun onProgressChanged(seekBar: SeekBar?, value: Int, p2: Boolean) {
        if(seekBar?.id == alpha.id||seekBar?.id == red.id||seekBar?.id == green.id||seekBar?.id == blue.id){
            val a = 255 - alpha.progress
            val r = 255 - red.progress
            val g = 255 - green.progress
            val b = 255 - blue.progress
            val color = Color.argb(a,r,g,b)
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