package dk.glutter.groupsmsmanager.groupsms;

/**
 * Created by luther on 19/11/2016.
 */

import android.app.IntentService;
import android.content.Intent;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import dk.glutter.groupsmsmanager.groupsms.API.SheetsHandler;

import static dk.glutter.groupsmsmanager.groupsms.StaticDB.*;

public class UpdateService extends IntentService implements Runnable {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public UpdateService() {
        super("UpdateService");
    }

    /**
     * This service keeps values in StaticDB updated. Every StaticDB.updateRefreshRate
     */
    public void startUpdating()
    {
        enableUpdateData_ = true;
    }
    public void stopUpdating()
    {
        enableUpdateData_ = false;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        run();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        enableUpdateData_ = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        enableUpdateData_ = false;
    }

    @Override
    public void run() {

        while (enableUpdateData_) {
            try {
                SheetsHandler.updateParametersInStaticDB(spreadsheetId, pmdbSheetRange);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // -- Update timestamp
            currentTimeStamp_ = getCurrentTimeStamp();

            // Update Message From Sheets
            try {
                String selectedGroup = SheetsHandler.getColumnsLastObject(spreadsheetId, selectedGroupForGroupMessageSheetRange).toString().replace("[", "").replace("]", "");
                String msg = SheetsHandler.getColumnsLastObject(spreadsheetId, messagesSheetRange).toString().replace("[", "").replace("]", "");
                if (!msg.isEmpty())
                {
                    groupMessage_ = selectedGroup + " " + msg;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(updateDataRefreshRate_);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @return yyyy-MM-dd HH:mm:ss formate date as string
     */
    public static String getCurrentTimeStamp() {
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String currentDateTime = dateFormat.format(new Date()); // Find todays date

            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }
}