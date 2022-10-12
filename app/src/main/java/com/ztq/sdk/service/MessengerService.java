package com.ztq.sdk.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class MessengerService extends Service {
    private static final int REPLY_MSG_ID = 2;
    private static final int MSG_ID = 1;

    static class BoundServiceHandler extends Handler {
        private final WeakReference<MessengerService> mService;

        public BoundServiceHandler(MessengerService service) {
            mService = new WeakReference<MessengerService>(service);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_ID:
                    Messenger replyMessenger = msg.replyTo;
                    Message replyMsg = Message.obtain(null, REPLY_MSG_ID);
                    //向客户端响应的消息内容
                    Bundle b = new Bundle();
                    b.putString("msg", "this is the message reply from service");
                    replyMsg.setData(b);
                    try {
                        replyMessenger.send(replyMsg);
                    } catch (RemoteException re) {
                        re.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private final Messenger mMessenger = new Messenger(new BoundServiceHandler(this));

    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(getApplicationContext(), "binding", Toast.LENGTH_SHORT).show();
        return mMessenger.getBinder();
    }
}