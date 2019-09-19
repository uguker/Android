package com.uguke.android.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import java.io.File;

/**
 * 意图工具类
 * @author LeiJue
 */
public class IntentUtils {

    private IntentUtils() {
        throw new UnsupportedOperationException("can't instantiate me.");
    }

    public static void launch(Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent == null) {
            return;
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void openDetailSettings(Context context, String packageName) {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.parse("package:" + packageName));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 拨打电话（直接拨打电话），需要添加Manifest.permission.CALL_PHONE权限
     * @param context 上下文
     * @param tel 手机号码
     */
    @SuppressLint("MissingPermission")
    public static void call(@NonNull Context context, @NonNull String tel) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tel));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     * @param context 上下文
     * @param tel 手机号码
     */
    public static void dial(@NonNull Context context, @NonNull String tel) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 发送短信
     * @param context 上下文
     * @param tel 发送对象
     * @param body 消息
     */
    public static void sendSms(@NonNull Context context, @NonNull String tel, @NonNull String body) {
        Uri uri = Uri.parse("smsto:" + tel);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", body);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void installApp(Context context, File file) {
        Intent intent = new Intent();
        //判读版本是否在7.0以上
        if (Build.VERSION.SDK_INT >= 24) {
            //provider authorities
            //Uri apkUri = FileProvider.getUriForFile(context, "com.toommi.dapp", file);
            Uri apkUri = FileProvider.getUriForFile(context, context.getPackageName(), file);
            //Granting Temporary Permissions to a URI
            intent.setAction(Intent.ACTION_VIEW);
            //intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setAction(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }
}
