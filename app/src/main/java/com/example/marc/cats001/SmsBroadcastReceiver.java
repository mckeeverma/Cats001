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
import java.util.Date;
@SuppressWarnings("deprecation")
public class SmsBroadcastReceiver extends BroadcastReceiver {
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    public static final String SMS_BUNDLE = "pdus";
    String TAG = "marclog";
    SmsMessage smsMessage;
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Start of onReceive");
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
                Log.d(TAG, "smsBody before change...: " + smsBody);
                if (smsBody.substring(0,4).equalsIgnoreCase("cat ")) {
                    smsBody = smsBody.substring(4);
                    Log.d(TAG, "smsBody after change....: " + smsBody);
                } else {
                    Log.d(TAG, "first 4 characters of text message not 'cat ', so ignoring the message");
                    continue;
                }
                if (smsBody.equalsIgnoreCase("Check email for the picture")) {
                    continue;
                }
                //Long messageDate = smsMessage.getTimestampMillis();
                //java.util.Date d = new java.util.Date(messageDate);
                //String itemDateStr = new SimpleDateFormat("dd-MMM HH:mm:ss").format(d);
                //smsMessageStr += "SMS From: " + address + "\n";
                //smsMessageStr += itemDateStr + "\n";
                //smsMessageStr += smsBody + "\n";
                Intent intent2 = new Intent();
                intent2.setAction(Intent.ACTION_MAIN);
                intent2.addCategory(Intent.CATEGORY_LAUNCHER);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK +
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED +
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD +
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON +
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFilename = "IMG_" + timeStamp + ".jpg";
                //------------------------------------------------------------
                //--- the following is a way to send information to an activity
                Bundle extras = new Bundle();
                extras.putString("image_filename", imageFilename);
                extras.putString("send_email_to_this_address", smsBody);
                intent2.putExtras(extras);
                //------------------------------------------------------------
                intent2.setComponent(new ComponentName("com.example.marc.abc001", "com.example.marc.abc001.MainActivity"));
                Log.d(TAG, "Calling startActivity now");
                try {
                    context.startActivity(intent2);
                } catch (Exception e) {
                    Log.d(TAG, "Error on startActivity: " + e.getMessage());
                    e.printStackTrace();
                }
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(address, null, "Check email for the picture", null, null);
                try {
                    Thread.sleep(10000);
                } catch (Exception e) {
                    Log.d(TAG, "Error on thread sleep: " + e.getMessage());
                }
            }
            //Toast.makeText(context, smsMessageStr, Toast.LENGTH_SHORT).show();
            //MainActivity inst = MainActivity.instance();
            //inst.updateList(smsMessageStr);
        }
    }
}