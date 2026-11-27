package com.nickname.generator;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class MainActivity extends Activity {
    private static final int FILE_REQ = 1;
    private WebView wv;
    private ValueCallback<Uri[]> fileCb;

    // Makes the web app's Share + backup Download buttons work inside the APK
    private static final String INJECT =
        "(function(){try{Object.defineProperty(navigator,'share',{configurable:true,value:function(o){Android.share((o&&(o.text||o.url))||'');return Promise.resolve();}});}catch(e){}"
      + "window.dl=function(n,x,ty){Android.saveFile(n,x,ty||'text/plain');try{pop('\\u2B07 '+n)}catch(e){}};})();";

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        wv = new WebView(this);
        setContentView(wv);

        WebSettings s = wv.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setAllowFileAccess(true);
        s.setAllowContentAccess(true);
        s.setTextZoom(100);

        wv.addJavascriptInterface(new Bridge(), "Android");
        wv.setWebViewClient(new WebViewClient() {
            @Override public void onPageFinished(WebView v, String u) { v.evaluateJavascript(INJECT, null); }
            @Override public boolean shouldOverrideUrlLoading(WebView v, WebResourceRequest r) {
                Uri u = r.getUrl();
                if ("http".equals(u.getScheme()) || "https".equals(u.getScheme())) {
                    startActivity(new Intent(Intent.ACTION_VIEW, u));
                    return true;
                }
                return false;
            }
        });
        wv.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onShowFileChooser(WebView v, ValueCallback<Uri[]> cb, FileChooserParams p) {
                if (fileCb != null) fileCb.onReceiveValue(null);
                fileCb = cb;
                try {
                    startActivityForResult(p.createIntent(), FILE_REQ);
                } catch (Exception e) {
                    fileCb = null;
                    return false;
                }
                return true;
            }
        });
        wv.loadUrl("file:///android_asset/index.html");
    }

    @Override
    protected void onActivityResult(int req, int res, Intent data) {
        if (req == FILE_REQ && fileCb != null) {
            fileCb.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(res, data));
            fileCb = null;
        }
        super.onActivityResult(req, res, data);
    }

    @Override
    public void onBackPressed() {
        // Close an open popup first, otherwise leave the app
        wv.evaluateJavascript(
            "(function(){var o=document.querySelector('.ov.show');if(o){o.classList.remove('show');return 1}return 0})()",
            v -> { if (!"1".equals(v)) runOnUiThread(() -> MainActivity.super.onBackPressed()); });
    }

    private class Bridge {
        @JavascriptInterface
        public void share(String text) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_TEXT, text);
            startActivity(Intent.createChooser(i, "Share"));
        }

        @JavascriptInterface
        public void saveFile(String name, String content, String mime) {
            try {
                byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
                if (Build.VERSION.SDK_INT >= 29) {
                    ContentValues cv = new ContentValues();
                    cv.put(MediaStore.Downloads.DISPLAY_NAME, name);
                    cv.put(MediaStore.Downloads.MIME_TYPE, mime);
                    cv.put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);
                    Uri u = getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, cv);
                    try (OutputStream o = getContentResolver().openOutputStream(u)) { o.write(bytes); }
                } else {
                    File d = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    d.mkdirs();
                    try (FileOutputStream o = new FileOutputStream(new File(d, name))) { o.write(bytes); }
                }
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Saved to Downloads: " + name, Toast.LENGTH_LONG).show());
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Save failed", Toast.LENGTH_LONG).show());
            }
        }
    }
}
