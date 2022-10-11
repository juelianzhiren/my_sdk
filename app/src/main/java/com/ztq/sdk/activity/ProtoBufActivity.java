package com.ztq.sdk.activity;

import android.os.Bundle;

import com.ztq.sdk.R;
import com.ztq.sdk.log.Log;
import com.ztq.sdk.protobuf.UserProto;

public class ProtoBufActivity extends BaseActivity{
    private static final String TAG = "noahedu.ProtoBufActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protobuf);

        UserProto.User.Builder builder = UserProto.User.newBuilder();
        builder.setName("CSDN");
        builder.setAge(366);

        UserProto.User user = builder.build();
        Log.v(TAG, "user.getName() = " + user.getName());
        Log.v(TAG, "user.getAge() = " + user.getAge());

        // 序列化
        byte[] bytes = user.toByteArray();
        StringBuffer stringBuffer = new StringBuffer();
        for (byte b : bytes) {
            stringBuffer.append(b + " ");
        }
        // 打印序列化结果
        Log.v(TAG, "result = " + stringBuffer.toString());
    }
}