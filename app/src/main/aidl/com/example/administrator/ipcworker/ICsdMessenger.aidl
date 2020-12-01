// ICsdMessenger.aidl
package com.example.administrator.ipcworker;

import android.os.Parcelable;
// Declare any non-default types here with import statements

interface ICsdMessenger {
  oneway void send(in Message msg);
  Message sendWithRsp(in Message msg);


  /// test 2

  // master mreged


  /// test 2
}
