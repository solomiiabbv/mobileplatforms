package com.example.project1

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class LifeCycleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_life_cycle)

        findViewById<Button>(R.id.btnFinish).setOnClickListener {
            finish() // закриває тільки цю Activity
        }

        findViewById<Button>(R.id.btnClose).setOnClickListener {
            finishAffinity() // закриває весь додаток
        }
    }
}


