package com.and1ss.android.photogallery.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.and1ss.android.photogallery.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(
                    R.id.fragment_container,
                    PhotoGalleryFragment.newInstance()
                )
                .addToBackStack(null)
                .commit()
        }
    }
}