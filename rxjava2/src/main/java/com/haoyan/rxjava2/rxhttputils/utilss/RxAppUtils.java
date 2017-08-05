package com.haoyan.rxjava2.rxhttputils.utilss;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.haoyan.rxjava2.RxApp;

import java.util.UUID;

/**
 * Created by haoyan on 2017/7/28.
 *
 * 关于应用的工具类
 */

public class RxAppUtils {
    /**
     * 获取手机版本号
     *
     * @return 返回版本号
     */
    public static String getAppVersion() {
        PackageInfo pi;
        String versionNum;
        try {
            PackageManager pm = RxApp.getInstance().getPackageManager();
            pi = pm.getPackageInfo(RxApp.getInstance().getPackageName(), PackageManager.GET_CONFIGURATIONS);
            versionNum = pi.versionName;
        } catch (Exception e) {
            versionNum = "0";
        }
        return versionNum;
    }

    /**
     * 获取手机唯一标识码UUID
     *
     * @return 返回UUID
     * <p>
     * 记得添加相应权限
     * android.permission.READ_PHONE_STATE
     */
    public static String getUUID() {

        Context context = RxApp.getInstance();

        String uuid = (String) RxSPUtils.get(context, "PHONE_UUID", "");

        if (TextUtils.isEmpty(uuid)) {

            try {
                TelephonyManager telephonyManager = (TelephonyManager) context
                        .getSystemService(Context.TELEPHONY_SERVICE);
                String tmDevice = telephonyManager.getDeviceId();
                String tmSerial = telephonyManager.getSimSerialNumber();

                String androidId = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
                UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
                String uniqueId = deviceUuid.toString();
                uuid = uniqueId;
                RxSPUtils.put(context, "PHONE_UUID", uuid);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return uuid;

    }
}
