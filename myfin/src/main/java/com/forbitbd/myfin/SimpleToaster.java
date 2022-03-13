package com.forbitbd.myfin;

import android.content.Context;
import android.widget.Toast;

public class SimpleToaster {

    public static void SimpleToast(Context context,String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
