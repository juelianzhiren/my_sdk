package com.ztq.sdk.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ztq.sdk.R;

import java.lang.ref.WeakReference;

public class MessengerActivity extends BaseActivity {
    private static final int REPLY_MSG_ID = 2;
    private boolean mServiceConnected = false;
    private Button btn = null;
    //用于向Service端发送消息的Messenger
    private Messenger mBoundServiceMessenger = null;
    //用于接收Service发送消息的Messenger
    private final Messenger mReceiveMessenger = new Messenger(new ReceiveMessHandler(this));
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBoundServiceMessenger = null;
            mServiceConnected = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBoundServiceMessenger = new Messenger(service);
            mServiceConnected = true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);

        btn = (Button) findViewById(R.id.button);
        bindService(new Intent(this, MessengerService.class), conn, Context.BIND_AUTO_CREATE);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mServiceConnected) {
                    //获取消息对象
                    Message msg = Message.obtain(null, 1, 0, 0);
                    try {
                        //replyTo参数包含客户端Messenger
                        msg.replyTo = mReceiveMessenger;
                        //向Service端发送消息
                        mBoundServiceMessenger.send(msg);
                    } catch (RemoteException re) {
                        re.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mServiceConnected) {
            unbindService(conn);
            mServiceConnected = false;
        }
    }

    /**
     * 客户端实现一个Handler用于接收服务端返回的响应
     *
     * @author Administrator
     */
    static class ReceiveMessHandler extends Handler {
        //持有当前Activity的弱引用，避免内存泄露
        private final WeakReference<MessengerActivity> mActivity;

        public ReceiveMessHandler(MessengerActivity activity) {
            mActivity = new WeakReference<MessengerActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REPLY_MSG_ID:
                    Toast.makeText(mActivity.get(), msg.getData().getString("msg"), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}