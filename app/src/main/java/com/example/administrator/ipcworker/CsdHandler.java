package com.example.administrator.ipcworker;


import android.os.Binder;
import android.os.Handler;
import android.os.Message;
import android.os.MessageQueue;
import android.os.RemoteException;
import android.util.Log;

public class CsdHandler extends Handler {

    // ICsdMessenger / IMessenger
    Object mMessenger;

    // ICsdMessenger / IMessenger
    public final Object getIMessenger() {
        return getIMessenger(true);
    }

    // ICsdMessenger / IMessenger

    /**
     * Hook the getIMessenger, for add more interface
     * @param fromSys use sys's binder
     * @return ICsdMessenger / IMessenger
     */
    public final Object getIMessenger(boolean fromSys) {
        if (fromSys) {
            return ReflectionUtil.getIMessageFromSys(this);
        } else {
            MessageQueue mQueue = ReflectionUtil.getMessageQueueFromHandler(this);
            if (mQueue == null) return null;
            synchronized (mQueue) {
                if (mMessenger != null) {
                    return mMessenger;
                }
                mMessenger = new MessengerImpl();
                return mMessenger;
            }
        }
    }

    private final class MessengerImpl extends ICsdMessenger.Stub {
        @Override
        public void send(Message msg) {
            msg.sendingUid = Binder.getCallingUid();
            CsdHandler.this.sendMessage(msg);
        }

        @Override
        public Message sendWithRsp(Message msg) throws RemoteException {
            Log.d("Tanck", "Server rsp :" + msg.what +", sync");
            Message message = Message.obtain();
            message.what = 999;
            Log.d("Tanck", "sendWithRsp to client:" + message.what + ", sync");
            return message;
        }
    }
}
