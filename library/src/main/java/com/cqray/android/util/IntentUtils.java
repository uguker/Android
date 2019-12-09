package com.cqray.android.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;

/**
 * 意图工具类
 * @author LeiJue
 */
public class IntentUtils {

    private IntentUtils() {
        throw new UnsupportedOperationException("can't instantiate me.");
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
}
