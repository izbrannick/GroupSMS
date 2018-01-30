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
    List<MyContact> contactsInCurrentGroup;

    public SmsHandler()
    {
        this.smsManager = SmsManager.getDefault();
    }

    public void startSmsTask()
    {
        if (!StringValidator.isForeignNumber(currSenderNumber_)) {
            if (StringValidator.isGroupMessage(groupMessage_)) {
                // ---- Get current group ---- //
                MyGroup currentGroup = StringValidator.getCurrentGroup(groupMessage_);

                if (currentGroup != null) {
                    // ---- Find current numbers ---- //
                    if (myContacts_ != null) {
                        contactsInCurrentGroup = new ArrayList<>();
                        for (int i = 0; i < myContacts_.size(); i++) {
                            MyContact myContact = myContacts_.get(i);
                            for (int g = 0; g < myContact.getGroups().size(); g++) {
                                Object myGroup = myContact.getGroups().get(g);
                                if (myGroup.toString().equals(currentGroup.getGroupName())) {
                                    contactsInCurrentGroup.add(myContact);
                                }
                            }
                        }
                    }

                    // ---- Start Send SMS Task  ---- //
                    new LongOperationGroupMSG().execute(currSenderNumber_, groupMessage_);

                }
            }
            if (StringValidator.isSignup(groupMessage_)) {
                // Add user to google sheets
                /// words /// [0]Signup [1]Group Name [2]Name
                if (!words.isEmpty()) {
                    int size = words.size();
                    if (size > 2) {
                        MyGroup group = MyGroup.getGroupByGroupName(words.get(1));
                        if (group != null) {
                            if (size > 3) {
                                new LongOperationSignup().execute(currSenderNumber_, groupMessage_, group.getGroupName(), words.get(2) + " " + words.get(size - 1));
                            }else {
                                new LongOperationSignup().execute(currSenderNumber_, groupMessage_, group.getGroupName(), words.get(2));
                            }
                        }
                    }
                    if (size == 2) {
                        MyGroup group = MyGroup.getGroupByGroupName(words.get(1));
                        new LongOperationSignup().execute(currSenderNumber_, groupMessage_, group.getGroupName(), "Navn ikke angivet");
                    }
                }
            }
            if (StringValidator.isResign(groupMessage_))
            {
                // Remove user from google sheets or just remove group
                /// words /// [0]Signup [1]Group Name [2]Name
                if (!words.isEmpty()) {
                    if (words.size() > 1) {
                        new LongOperationResign().execute(currSenderNumber_, groupMessage_, words.get(1));
                    }
                }
            }
        }
    }

    private class LongOperationGroupMSG extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            Log.d("LongOperation", "doInBackground");

            ArrayList<String> fragmentedMessageList = smsManager.divideMessage(params[1]);

            try {
                for (int i = 0; i < contactsInCurrentGroup.size(); i++) {
                    Log.d("SEND SMS", "Sending to #" + i + " "+ contactsInCurrentGroup.get(i));
                    if (contactsInCurrentGroup.get(i).getNumberPrimary() != null) {
                        smsManager.sendMultipartTextMessage(contactsInCurrentGroup.get(i).getNumberPrimary(), null, fragmentedMessageList, null, null);
                    }
                }

            } catch (Exception e) {
                Thread.interrupted();
                Log.d("Exception", e.getMessage());
            }

            // ------ APPEND
            try {
                SheetsHandler.appendValue(spreadsheetId, messageLOGSheetRange, groupMessage_);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // ------ DELETE
            try {
                // -------- Delete message
                SheetsHandler.deleteValue(spreadsheetId, messagesSheetRange);
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
            Log.d("LongOperation", "onProgressUpdate");
        }

    }

    private class LongOperationSignup extends AsyncTask<String, Void, String> {



        @Override
        protected String doInBackground(String... params) {
            Log.d("LongOperation", "doInBackground");

            String senderNumber = params[0];
            String message = params[1];
            String groupName = params[2];
            String senderName = params[3];


            //TODO: if contact is existing contact
            int position = -3;
            try {
                position = SheetsHandler.getNumberRangePosition(spreadsheetId, contactsSheetRange, senderNumber);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // ---  IF Number already exists
            if (position > 0) {
                // ------ get contacts already existing groups
                MyContact contact = new MyContact();
                contact = contact.getContatByNumber(senderNumber);
                if (contact != null) {
                    SheetsHandler.updateContactInfo(contact, groupName, senderNumber);
                }
            }
            else {
                // ------ APPEND NEW USER TO CONTACTS SHEET LIST
                SheetsHandler.addNewContact(groupName, senderNumber, senderName);
            }
            //TODO: Send SMS response to user
            try {
                smsManager.sendTextMessage(params[0], null, "Du er nu tilmeldt. Tak :)", null, null);
            } catch (Exception e) {
                Thread.interrupted();
                Log.d("Exception", e.getMessage());
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
            Log.d("LongOperation", "onProgressUpdate");
        }

    }

    private class LongOperationResign extends AsyncTask<String, Void, String> {



        @Override
        protected String doInBackground(String... params) {
            Log.d("LongOperation", "doInBackground");

            String senderNumber = params[0];
            String message = params[1];
            String groupName = params[2];


            //TODO: if contact is existing contact
            int position = -3;
            try {
                position = SheetsHandler.getNumberRangePosition(spreadsheetId, contactsSheetRange, senderNumber);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // ---  IF Number already exists
            if (position > 0) {
                // ------ get contacts already existing groups
                MyContact contact = new MyContact();
                contact = contact.getContatByNumber(senderNumber);
                if (contact != null) {
                    SheetsHandler.removeContactsGroupOrDelete(contact, groupName, senderNumber);
                }
            }
            //TODO: Send SMS response to user
            try {
                smsManager.sendTextMessage(params[0], null, "Du er nu afmeldt. Tak at du var med i: " + groupName, null, null);
            } catch (Exception e) {
                Thread.interrupted();
                Log.d("Exception", e.getMessage());
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