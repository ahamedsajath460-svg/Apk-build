package com.zenai.htmlpreviewstudio

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val label = findViewById<TextView>(R.id.textview_first)
        // Restore saved count if available
        if (savedInstanceState != null) {
            count = savedInstanceState.getInt("COUNT_KEY", 0)
            label.text = getString(R.string.clicked, count)
        }

        findViewById<Button>(R.id.button_first).setOnClickListener {
            count++
            label.text = getString(R.string.clicked, count)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("COUNT_KEY", count)
    }
}
