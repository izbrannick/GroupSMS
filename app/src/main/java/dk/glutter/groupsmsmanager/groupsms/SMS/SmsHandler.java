package dk.glutter.groupsmsmanager.groupsms.SMS;

/**
 * Created by izbrannick on 06-02-2015.
 */


import android.os.AsyncTask;
import android.telephony.SmsManager;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dk.glutter.groupsmsmanager.groupsms.API.SheetsHandler;
import dk.glutter.groupsmsmanager.groupsms.MyContact;
import dk.glutter.groupsmsmanager.groupsms.MyGroup;

import static dk.glutter.groupsmsmanager.groupsms.StaticDB.contactsSheetRange;
import static dk.glutter.groupsmsmanager.groupsms.StaticDB.currSenderNumber_;
import static dk.glutter.groupsmsmanager.groupsms.StaticDB.groupMessage_;
import static dk.glutter.groupsmsmanager.groupsms.StaticDB.messageLOGSheetRange;
import static dk.glutter.groupsmsmanager.groupsms.StaticDB.messagesSheetRange;
import static dk.glutter.groupsmsmanager.groupsms.StaticDB.myContacts_;
import static dk.glutter.groupsmsmanager.groupsms.StaticDB.spreadsheetId;
import static dk.glutter.groupsmsmanager.groupsms.StaticDB.words;

/**
 * Created by u321424 on 07-11-2016.
 */

public class SmsHandler {

    private SmsManager smsManager;
    String message;

    public SmsHandler()
    {
        this.smsManager = SmsManager.getDefault();
    }

    public void startAppendTask(String msg)
    {
        message = msg;
        new LongOperationAppend().execute(currSenderNumber_, groupMessage_, msg);
    }

    private class LongOperationAppend extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            Log.d("LongOperation", "doInBackground");

            String senderNumber = params[0];
            String message = params[1];
            String groupName = params[2];

            // ------ APPEND
            try {
                SheetsHandler.appendValue(spreadsheetId, messageLOGSheetRange, message);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("onPostExecute", result);
        }

        @Override
        protected void onPreExecute() {
            Log.d("LongOperation", "PreExecute");
        }

        int m = 0;

        @Override
        protected void onProgressUpdate(Void... values) {
            m += 1;
            Log.d("LongOperation", " onProgressUpdate ");
        }

    }
}