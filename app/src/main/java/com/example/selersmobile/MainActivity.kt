package com.example.selersmobile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.selersmobile.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home) // Asegurate de tener activity_main.xml
    }
}