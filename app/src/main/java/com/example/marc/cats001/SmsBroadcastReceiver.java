package com.example.marc.cats001;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;
import android.telephony.SmsManager;

import java.text.SimpleDateFormat;
@SuppressWarnings("deprecation")
public class SmsBroadcastReceiver extends BroadcastReceiver {
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    public static final String SMS_BUNDLE = "pdus";
    String TAG = "marclog";
    SmsMessage smsMessage;
    public void onReceive(Context context, Intent intent) {
        Bundle intentExtras = intent.getExtras();
        String format = intentExtras.getString("format");
        if (intentExtras != null) {
            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
            String smsMessageStr = "";
            for (int i = 0; i < sms.length; ++i) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    smsMessage = SmsMessage.createFromPdu((byte[]) sms[i], format);
                } else {
                    smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);
                }
                String smsBody = smsMessage.getMessageBody().toString();
                String address = smsMessage.getOriginatingAddress();
                //Long messageDate = smsMessage.getTimestampMillis();
                //java.util.Date d = new java.util.Date(messageDate);
                //String itemDateStr = new SimpleDateFormat("dd-MMM HH:mm:ss").format(d);
                //smsMessageStr += "SMS From: " + address + "\n";
                //smsMessageStr += itemDateStr + "\n";
                //smsMessageStr += smsBody + "\n";
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(address, null, "got it: " + smsBody, null, null);
                Intent intent2 = new Intent();
                intent2.setAction(Intent.ACTION_MAIN);
                intent2.addCategory(Intent.CATEGORY_LAUNCHER);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK +
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED +
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD +
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON +
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
                intent2.putExtra("msg", smsBody);
                intent2.setComponent(new ComponentName("com.example.marc.abc001", "com.example.marc.abc001.MainActivity"));
                Log.d(TAG, "Calling startActivity now");
                try {
                    context.startActivity(intent2);
                } catch (Exception e) {
                    Log.d(TAG, "Error on startActivity: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            //Toast.makeText(context, smsMessageStr, Toast.LENGTH_SHORT).show();
            //MainActivity inst = MainActivity.instance();
            //inst.updateList(smsMessageStr);
        }
    }
}