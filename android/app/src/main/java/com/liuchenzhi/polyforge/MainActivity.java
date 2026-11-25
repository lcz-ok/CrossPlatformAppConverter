package com.liuchenzhi.polyforge;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * POLYFORGE — Cross-Platform App Converter
 * Solely created by Liu Chenzhi. 刘宸至一手创作！
 */
public class MainActivity extends Activity {

    private static final int REQ_FILE = 1001;
    private WebView webView;
    private ValueCallback<Uri[]> fileCallback;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webView = new WebView(this);
        webView.setBackgroundColor(Color.parseColor("#04060B"));

        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setAllowFileAccess(true);
        s.setAllowContentAccess(true);
        s.setLoadWithOverviewMode(true);
        s.setUseWideViewPort(true);
        s.setTextZoom(100);

        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> callback, FileChooserParams params) {
                if (fileCallback != null) fileCallback.onReceiveValue(null);
                fileCallback = callback;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                try {
                    startActivityForResult(Intent.createChooser(intent, "选择文件 / Select files"), REQ_FILE);
                    return true;
                } catch (Exception e) {
                    fileCallback = null;
                    Toast.makeText(MainActivity.this, "无法打开文件选择器", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        });

        webView.addJavascriptInterface(new Bridge(), "AndroidBridge");
        webView.loadUrl("file:///android_asset/index.html");
        setContentView(webView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != REQ_FILE) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        Uri[] uris = null;
        if (resultCode == RESULT_OK && data != null) {
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                uris = new Uri[count];
                for (int i = 0; i < count; i++) {
                    uris[i] = data.getClipData().getItemAt(i).getUri();
                }
            } else if (data.getData() != null) {
                uris = new Uri[]{ data.getData() };
            }
        }
        if (fileCallback != null) {
            fileCallback.onReceiveValue(uris);
            fileCallback = null;
        }
    }

    /** 导出生成的工程 / 报告到系统下载目录 */
    private class Bridge {
        @JavascriptInterface
        public void saveFile(String name, String base64, String mime) {
            try {
                byte[] bytes = Base64.decode(base64, Base64.DEFAULT);
                String safeName = name == null || name.trim().isEmpty() ? "polyforge-export" : name;
                String path;

                if (Build.VERSION.SDK_INT >= 29) {
                    ContentValues cv = new ContentValues();
                    cv.put(MediaStore.Downloads.DISPLAY_NAME, safeName);
                    cv.put(MediaStore.Downloads.MIME_TYPE,
                            mime == null || mime.isEmpty() ? "application/octet-stream" : mime);
                    cv.put(MediaStore.Downloads.RELATIVE_PATH,
                            Environment.DIRECTORY_DOWNLOADS + "/POLYFORGE");
                    Uri uri = getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, cv);
                    if (uri == null) throw new IllegalStateException("MediaStore 写入失败");
                    OutputStream os = getContentResolver().openOutputStream(uri);
                    if (os == null) throw new IllegalStateException("输出流打开失败");
                    os.write(bytes);
                    os.close();
                    path = "Download/POLYFORGE/" + safeName;
                } else {
                    File dir = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "POLYFORGE");
                    if (!dir.exists() && !dir.mkdirs()) throw new IllegalStateException("目录创建失败");
                    File f = new File(dir, safeName);
                    FileOutputStream fos = new FileOutputStream(f);
                    fos.write(bytes);
                    fos.close();
                    path = f.getAbsolutePath();
                }
                toastOnUi("已保存 / Saved: " + path);
            } catch (Exception e) {
                toastOnUi("保存失败 / Save failed: " + e.getMessage());
            }
        }
    }

    private void toastOnUi(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }
}
