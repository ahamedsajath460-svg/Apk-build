package com.example.htmlpreviewstudio

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var editTextHtml: EditText
    private lateinit var buttonPreview: Button
    private lateinit var webViewPreview: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextHtml = findViewById(R.id.editTextHtml)
        buttonPreview = findViewById(R.id.buttonPreview)
        webViewPreview = findViewById(R.id.webViewPreview)

        configureWebView()

        buttonPreview.setOnClickListener {
            val htmlContent = editTextHtml.text.toString()
            webViewPreview.loadDataWithBaseURL(
                null,
                htmlContent,
                "text/html",
                "UTF-8",
                null
            )
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun configureWebView() {
        val webSettings: WebSettings = webViewPreview.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
    }

    override fun onDestroy() {
        super.onDestroy()
        webViewPreview.destroy()
    }
}
