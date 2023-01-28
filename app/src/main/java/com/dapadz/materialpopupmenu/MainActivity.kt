package com.dapadz.materialpopupmenu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            showFolderMenu(it)
        }
    }

    private fun showFolderMenu(v: View) {
        val config = MaterialPopupConfiguration.Builder()
            .setTitle("Hello world")
            .build()
        val menu = MaterialPopupMenu(
            context = this,
            view = v,
            config = config,
        )
        menu.setOnMenuItemClickListener {
            Log.i("MENU_CLICK", it.title.toString())
        }
        menu.addMenu(R.menu.menu)
        menu.show()
    }
}