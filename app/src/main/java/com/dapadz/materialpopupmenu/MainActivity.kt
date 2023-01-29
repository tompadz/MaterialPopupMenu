package com.dapadz.materialpopupmenu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button = findViewById<ImageView>(R.id.image)
        button.setOnClickListener {
            showMenu(it)
        }
    }

    private fun showMenu(v: View) {
        val config = MaterialPopupConfiguration.Builder()
            .setTitle("Actions")
            .blurEnable(true)
            .build()
        val menu = MaterialPopupMenu(
            context = this,
            view = v,
            config = config,
        )
        menu.setOnMenuItemClickListener {
            Snackbar.make(this, v, it.title !!, 1000).show()
        }
        menu.addMenuAndShow(R.menu.menu)
    }
}