package com.alexm.fingerprinter

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import tech.picnic.fingerpaintview.R
import com.snatik.storage.Storage
import java.io.File
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.support.v4.content.ContextCompat
import java.util.jar.Manifest
import android.R.attr.bitmap
import android.graphics.Bitmap
import android.graphics.drawable.Drawable




class MainActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    private val PERMISSION_REQUEST_CODE:Int = 1000

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

    private fun saveBitmap() {
        val storage = Storage(applicationContext)
        val path = storage.externalStorageDirectory
        storage.createDirectory("$path${File.separator}MyFolder", true)

        val bitmap:Bitmap
        if(finger.drawable is BitmapDrawable) {
            bitmap = (finger.drawable as BitmapDrawable).bitmap
            storage.createFile("$path${File.separator}MyFolder${File.separator}FingerDraw.bmp", bitmap)
            Toast.makeText(this, "Salvato", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, "L'immagine è vuota", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager
                        .PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_CODE)
        }else{
            saveBitmap()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            PERMISSION_REQUEST_CODE ->
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    saveBitmap()
                else
                    Toast.makeText(this, "Se non mi dai i permessi non posso scrivere, cornuto", Toast.LENGTH_SHORT).show()
        }
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