package com.zenai.snakegame

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.ripple.RippleDrawable
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.MaterialShapeDrawable.Factory
import com.google.android.material.shape.ShapeAppearanceModel

class MainActivity : AppCompatActivity() {
    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create a simple vertical layout with a TextView and a Button programmatically
        val rootLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 32, 32, 32)
        }

        val label = TextView(this).apply {
            text = "Clicked 0 times"
            textSize = 18f
        }

        val button = Button(this).apply {
            text = "Click me"
        }

        rootLayout.addView(label)
        rootLayout.addView(button)

        setContentView(rootLayout)

        button.setOnClickListener {
            count++
            label.text = getString(R.string.clicked, count)
        }
    }
}
