/**
 * CUSTOM CODES
 */

package io.flutter.plugins.webviewflutter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.annotation.Nullable;

public class WebViewFileChooser {
    private static WebViewFileChooser instance = new WebViewFileChooser();

    private WebViewFileChooser() {
    }

    public static WebViewFileChooser getInstance() {
        return instance;
    }

    private Activity activity;

    Activity getActivity() {
        return activity;
    }

    void updateContext(Context context) {
        if (context instanceof Activity) {
            activity = (Activity) context;
        }
    }

    private ValueCallback<Uri[]> filePathCallback;

    private final int CHOOSE_FILE_REQ_CODE = 523732742;

    boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
        if (activity == null) {
            return false;
        }
        this.filePathCallback = filePathCallback;
        final Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        activity.startActivityForResult(intent, CHOOSE_FILE_REQ_CODE);
        return true;
    }

    boolean onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (filePathCallback == null) {
            return false;
        }
        if (requestCode != CHOOSE_FILE_REQ_CODE || resultCode != Activity.RESULT_OK) {
            filePathCallback.onReceiveValue(null);
            filePathCallback = null;
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            filePathCallback.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
            filePathCallback = null;
            return true;
        }
        return false;
    }
}
