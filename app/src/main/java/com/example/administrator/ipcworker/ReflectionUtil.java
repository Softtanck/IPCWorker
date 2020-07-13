package com.example.administrator.ipcworker;

import android.os.Handler;
import android.os.MessageQueue;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtil {
    public static MessageQueue getMessageQueueFromHandler(CsdHandler targetHandler) {
        try {
            Field mQueueField = Handler.class.getDeclaredField("mQueue");
            mQueueField.setAccessible(true);
            return (MessageQueue) mQueueField.get(targetHandler);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getIMessageFromSys(CsdHandler csdHandler) {
        try {
            Method getIMessengerMethod = Handler.class.getDeclaredMethod("getIMessenger");
            getIMessengerMethod.setAccessible(true);
            return getIMessengerMethod.invoke(csdHandler);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
