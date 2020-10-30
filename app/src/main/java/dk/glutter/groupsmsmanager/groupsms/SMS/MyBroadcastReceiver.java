package dk.glutter.groupsmsmanager.groupsms.SMS;

/**
 * Modifyed by glutter on 12-12-2019.
 */


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import static dk.glutter.groupsmsmanager.groupsms.StaticDB.activateByIncomingSms;
import static dk.glutter.groupsmsmanager.groupsms.StaticDB.currSenderNumber_;
import static dk.glutter.groupsmsmanager.groupsms.StaticDB.groupMessage_;
import static dk.glutter.groupsmsmanager.groupsms.StaticDB.isPermissionToGoogleGranted;

/**
 * Created by glutter on 02/04/15.
 */
public class MyBroadcastReceiver extends BroadcastReceiver {

    static String beskedOld = "";
    static String numberOld = "";
    String currMsg = "";
    String currNr = "";
    Context context;


    @Override
    public void onReceive(Context context, Intent intent) {

        String sds = intent.getAction();

        Toast.makeText(context.getApplicationContext(), sds,
                Toast.LENGTH_LONG).show();

        if (intent.getAction().equals(
                "android.provider.Telephony.SMS_DELIVERED")) {
            Toast.makeText(context.getApplicationContext(), "SMS_DELIVERED",
                    Toast.LENGTH_LONG).show();

        }
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED") || intent.getAction().equals("android.provider.Telephony.SMS_DELIVER")) {

            Toast.makeText(context.getApplicationContext(), "SMS_RECIVED",
                    Toast.LENGTH_LONG).show();

            SmsMessage[] msg = null;
            this.context = context;

            Bundle bundle = intent.getExtras();
            Object[] pdus = (Object[]) bundle.get("pdus");
            msg = new SmsMessage[pdus.length];
            for (int i = 0; i < pdus.length; i++) {
                msg[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                currNr = msg[i].getOriginatingAddress();
            }

            SmsMessage sms = msg[0];
            try {
                if (msg.length == 1 || sms.isReplace()) {
                    currMsg = sms.getDisplayMessageBody();
                } else {
                    StringBuilder bodyText = new StringBuilder();
                    for (int i = 0; i < msg.length; i++) {
                        bodyText.append(msg[i].getMessageBody());
                    }
                    currMsg = bodyText.toString();
                }
            } catch (Exception e) {
                Log.e("onReceive blooper", e.getMessage());
            }

            if (!beskedOld.equals(currMsg) || !numberOld.equals(currNr)) {
                Log.i("Not an old message", currMsg);
                beskedOld = currMsg;
                numberOld = currNr;
                if (!currMsg.isEmpty()) {
                    if (!isPermissionToGoogleGranted) {
                        Log.i("Not GRANTED :((", currMsg);
                        Log.i("GRANTING...", currMsg);
                        activateByIncomingSms = true;
                    }
                            groupMessage_ = currMsg;
                            currSenderNumber_ = currNr;
                            SmsHandler smsHandler = new SmsHandler();
                            smsHandler.startAppendTask(currMsg);
                    //}
                }
            }
        }
    }

}