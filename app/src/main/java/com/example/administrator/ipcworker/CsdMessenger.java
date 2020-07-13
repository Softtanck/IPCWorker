package com.example.administrator.ipcworker;

import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;

public class CsdMessenger implements Parcelable {
    private final ICsdMessenger mTarget;

    /**
     * Create a new Messenger pointing to the given Handler.  Any Message
     * objects sent through this Messenger will appear in the Handler as if
     * {@link Handler#sendMessage(Message) Handler.sendMessage(Message)} had
     * been called directly.
     *
     * @param target The Handler that will receive sent messages.
     */
    public CsdMessenger(CsdHandler target) {
        mTarget = (ICsdMessenger) target.getIMessenger(false);
    }

    /**
     * Send a Message to this Messenger's Handler.
     *
     * @param message The Message to send.  Usually retrieved through
     *                {@link Message#obtain() Message.obtain()}.
     * @throws RemoteException Throws DeadObjectException if the target
     *                         Handler no longer exists.
     */
    public void send(Message message) throws RemoteException {
        mTarget.send(message);
    }

    public Message sendWithRsp(Message message) throws RemoteException {
        return mTarget.sendWithRsp(message);
    }

    /**
     * Retrieve the IBinder that this Messenger is using to communicate with
     * its associated Handler.
     *
     * @return Returns the IBinder backing this Messenger.
     */
    public IBinder getBinder() {
        return mTarget.asBinder();
    }

    public IBinder getSysBinder(CsdHandler target) {
        return ((IInterface) target.getIMessenger()).asBinder();
    }

    /**
     * Comparison operator on two Messenger objects, such that true
     * is returned then they both point to the same Handler.
     */
    public boolean equals(Object otherObj) {
        if (otherObj == null) {
            return false;
        }
        try {
            return mTarget.asBinder().equals(((CsdMessenger) otherObj)
                    .mTarget.asBinder());
        } catch (ClassCastException e) {
        }
        return false;
    }

    public int hashCode() {
        return mTarget.asBinder().hashCode();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeStrongBinder(mTarget.asBinder());
    }

    public static final Parcelable.Creator<CsdMessenger> CREATOR
            = new Parcelable.Creator<CsdMessenger>() {
        public CsdMessenger createFromParcel(Parcel in) {
            IBinder target = in.readStrongBinder();
            return target != null ? new CsdMessenger(target) : null;
        }

        public CsdMessenger[] newArray(int size) {
            return new CsdMessenger[size];
        }
    };

    /**
     * Convenience function for writing either a Messenger or null pointer to
     * a Parcel.  You must use this with {@link #readMessengerOrNullFromParcel}
     * for later reading it.
     *
     * @param messenger The Messenger to write, or null.
     * @param out       Where to write the Messenger.
     */
    public static void writeMessengerOrNullToParcel(CsdMessenger messenger,
                                                    Parcel out) {
        out.writeStrongBinder(messenger != null ? messenger.mTarget.asBinder()
                : null);
    }

    /**
     * Convenience function for reading either a Messenger or null pointer from
     * a Parcel.  You must have previously written the Messenger with
     * {@link #writeMessengerOrNullToParcel}.
     *
     * @param in The Parcel containing the written Messenger.
     * @return Returns the Messenger read from the Parcel, or null if null had
     * been written.
     */
    public static CsdMessenger readMessengerOrNullFromParcel(Parcel in) {
        IBinder b = in.readStrongBinder();
        return b != null ? new CsdMessenger(b) : null;
    }

    /**
     * Create a Messenger from a raw IBinder, which had previously been
     * retrieved with {@link #getBinder}.
     *
     * @param target The IBinder this Messenger should communicate with.
     */
    public CsdMessenger(IBinder target) {
        mTarget = ICsdMessenger.Stub.asInterface(target);
    }
}
