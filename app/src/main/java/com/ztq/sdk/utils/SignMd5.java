package com.ztq.sdk.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignMd5 {

    public static String getMd5(Context context) {
        Signature[] signs = getRawSignature(context, "com.noahedu.learnstore");
        try {
            //获取原始签名MD5
            String signValidString = getSignValidString(signs[0].toByteArray());
            System.out.println("~~md5: " + signValidString);
            return signValidString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Signature[] getRawSignature(Context context, String packageName) {
        if (packageName == null || packageName.length() == 0) {
            return null;
        }
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);
            if (info != null) {
                return info.signatures;
            }
            //errout("info is null, packageName = " + packageName);
            return null;
        } catch (PackageManager.NameNotFoundException e) {
            //errout("NameNotFoundException");
            return null;
        }
    }

    private static String getSignValidString(byte[] paramArrayOfByte) throws NoSuchAlgorithmException {
        MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
        localMessageDigest.update(paramArrayOfByte);
        return toHexString(localMessageDigest.digest());
    }

    public static String toHexString(byte[] paramArrayOfByte) {
        if (paramArrayOfByte == null) {
            return null;
        }
        StringBuilder localStringBuilder = new StringBuilder(2 * paramArrayOfByte.length);
        for (int i = 0; ; i++) {
            if (i >= paramArrayOfByte.length) {
                return localStringBuilder.toString();
            }
            String str = Integer.toString(0xFF & paramArrayOfByte[i], 16);
            if (str.length() == 1) {
                str = "0" + str;
            }
            localStringBuilder.append(str);
        }
    }
}
