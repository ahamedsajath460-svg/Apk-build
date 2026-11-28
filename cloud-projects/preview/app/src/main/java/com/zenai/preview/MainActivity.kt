package com.zenai.preview

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Use the TextView defined in the layout (assumed to have the id "textView")
        val label = findViewById<TextView>(R.id.textView)

        // Use the Button defined in the layout (assumed to have the id "button")
        findViewById<Button>(R.id.button).setOnClickListener {
            count++
            label.text = getString(R.string.clicked, count)
        }
    }
}
