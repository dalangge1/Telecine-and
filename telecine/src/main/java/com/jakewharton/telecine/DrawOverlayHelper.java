package com.jakewharton.telecine;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import static android.os.Build.VERSION_CODES.M;

final class DrawOverlayHelper {

    private static final int REQUEST_CODE_DRAW_OVERLAY_PERMISSION = 2424;

    private DrawOverlayHelper() {
        throw new AssertionError("No instances.");
    }

    static boolean hasPermission(Context context) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(context);
    }

    @TargetApi(M) static void requestPermission(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:" + activity.getPackageName()));
        activity.startActivityForResult(intent, REQUEST_CODE_DRAW_OVERLAY_PERMISSION);
    }

    static boolean handleActivityResult(Activity activity, int requestCode, Analytics analytics) {
        if (requestCode != REQUEST_CODE_DRAW_OVERLAY_PERMISSION) {
            return false;
        }
        if (hasPermission(activity)) {
            CaptureHelper.fireScreenCaptureIntent(activity, analytics);
        }
        return false;
    }
}
