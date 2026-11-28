package com.html.lk

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var editText: EditText
    private lateinit var buttonRender: Button
    private lateinit var webViewPreview: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editText = findViewById(R.id.editText)
        buttonRender = findViewById(R.id.button)
        webViewPreview = findViewById(R.id.webViewPreview)

        // WebView setup
        val webSettings: WebSettings = webViewPreview.settings
        webSettings.javaScriptEnabled = true
        webViewPreview.loadDataWithBaseURL(null, "", "text/html", "UTF-8", null)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        viewModel.htmlContent.observe(this) { html ->
            webViewPreview.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
        }

        // Render on button click
        buttonRender.setOnClickListener {
            val html = editText.text.toString()
            viewModel.setHtml(html)
        }

        // Instant render on text change
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.setHtml(s?.toString().orEmpty())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}
