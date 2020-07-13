package com.example.administrator.ipcworker;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.lang.reflect.Proxy;

public class MyIPCService extends Service {

    private final CsdHandler handler = new CsdHandler(){
        @Override
        public void handleMessage(Message msg) {
            Log.d("Tanck", "Server rsp from client:" + msg.what + ", async");

            try {
                Log.d("Tanck", "Server send 888 to client async");
                msg.replyTo.send(Message.obtain(null, 888));
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("Tanck", "send failed,"+ e.getMessage());
            }
        }
    };
    private final CsdMessenger messenger = new CsdMessenger(handler);

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        int clientVersion = intent.getIntExtra("CLIENT_VERSION", -1);
        IBinder tempBinder = clientVersion == -1 ? messenger.getSysBinder(handler) : messenger.getBinder();
        Log.d("Tanck", "Client Version:" + clientVersion  + ", binder:" + tempBinder.isBinderAlive() + ", " + tempBinder.toString());
        return  tempBinder;
    }
}
