package dk.glutter.groupsmsmanager.groupsms.API;


import android.icu.util.Calendar;

import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetResponse;
import com.google.api.services.sheets.v4.model.ClearValuesRequest;
import com.google.api.services.sheets.v4.model.DeleteDimensionRequest;
import com.google.api.services.sheets.v4.model.DimensionRange;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.security.Timestamp;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static dk.glutter.groupsmsmanager.groupsms.StaticDB.applicationName_;
import static dk.glutter.groupsmsmanager.groupsms.StaticDB.contactSheetId;
import static dk.glutter.groupsmsmanager.groupsms.StaticDB.contactsSheetRange;
import static dk.glutter.groupsmsmanager.groupsms.StaticDB.currentCountryCode;
import static dk.glutter.groupsmsmanager.groupsms.StaticDB.currentTimeStamp_;
import static dk.glutter.groupsmsmanager.groupsms.StaticDB.enableUpdateData_;
import static dk.glutter.groupsmsmanager.groupsms.StaticDB.enableUpdateUI_;
import static dk.glutter.groupsmsmanager.groupsms.StaticDB.groupMessageOld_;
import static dk.glutter.groupsmsmanager.groupsms.StaticDB.groupMessage_;
import static dk.glutter.groupsmsmanager.groupsms.StaticDB.groupsSheetRange;
import static dk.glutter.groupsmsmanager.groupsms.StaticDB.mService_;
import static dk.glutter.groupsmsmanager.groupsms.StaticDB.messageLOGSheetRange;
import static dk.glutter.groupsmsmanager.groupsms.StaticDB.messagesSheetRange;
import static dk.glutter.groupsmsmanager.groupsms.StaticDB.pmdbSheetRange;
import static dk.glutter.groupsmsmanager.groupsms.StaticDB.resign;
import static dk.glutter.groupsmsmanager.groupsms.StaticDB.selectedGroupForGroupMessageSheetRange;
import static dk.glutter.groupsmsmanager.groupsms.StaticDB.signup;
import static dk.glutter.groupsmsmanager.groupsms.StaticDB.spreadsheetId;
import static dk.glutter.groupsmsmanager.groupsms.StaticDB.updateDataRefreshRate_;
import static dk.glutter.groupsmsmanager.groupsms.StaticDB.updateUIRefreshRate_;

/**
 * Created by u321424 on 07-12-2016.
 */

public class SheetsHandler {



    /**
     * Timestamp is added in the next column
     * | value || timestamp |
     * @return AppendValuesResponse
     */
    public static AppendValuesResponse appendValue(String sheetId, String range, String value)
    {
        Date currentTime = new Date();
        
        List<Object> results = new ArrayList<>();
        results.add(value);
        results.add(" : " + currentTime.toString());

        List<List<Object>> resultsInResults = new ArrayList<>();
        resultsInResults.add(results);

        ValueRange response = new ValueRange();

        response.setRange(range);
        response.setValues(resultsInResults);

        List<List<Object>> values = response.getValues();

        ValueRange valueRange = new ValueRange();
        valueRange.setValues(values);
        try {
            return mService_.spreadsheets().values().append(sheetId, range, valueRange).setValueInputOption("RAW").execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Delete a value from sheet
     * | value || timestamp |
     * @return AppendValuesResponse
     */
    public static void deleteValue(String sheetId, String range)
    {
        ClearValuesRequest clear = new ClearValuesRequest();

        try {
            mService_.spreadsheets().values().clear(sheetId, range, clear).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Array list of values are added next incrementing to column, example:
     * | value1 || timestamp || other value || etc. value || and so on value ||
     * @return AppendValuesResponse - List of Append Values
     */
    public static AppendValuesResponse appendValues(String sheetId, String range, ArrayList<Object> valueList)
    {
        List<List<Object>> resultsInResults = new ArrayList<>();
        resultsInResults.add(valueList);

        ValueRange response = new ValueRange();

        response.setRange(range);
        response.setValues(resultsInResults);

        List<List<Object>> values = response.getValues();

        ValueRange valueRange = new ValueRange();
        valueRange.setValues(values);
        try {
            return mService_.spreadsheets().values().append(sheetId, range, valueRange).setValueInputOption("USER_ENTERED").execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Array list of values are added next incrementing to column, example:
     * | value1 || timestamp || other value || etc. value || and so on value ||
     * @return AppendValuesResponse - List of Append Values
     */
    public static UpdateValuesResponse updateValues(String sheetId, String range, ArrayList<Object> valueList)
    {
        List<List<Object>> resultsInResults = new ArrayList<>();
        resultsInResults.add(valueList);

        ValueRange response = new ValueRange();

        response.setRange(range);
        response.setValues(resultsInResults);

        List<List<Object>> values = response.getValues();

        ValueRange valueRange = new ValueRange();
        valueRange.setValues(values);
        try {
            //return mService_.spreadsheets().values().append(spreadsheetId, range, valueRange).setValueInputOption("RAW").execute();
            return mService_.spreadsheets().values().update(sheetId, range, valueRange).setValueInputOption("RAW").execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Fetch a list of names and majors of students in a sample spreadsheet:
     * https://docs.google.com/spreadsheets/................/edit
     * @return ArrayList<Object> with column[columnNumber]
     * @throws IOException
     * @param range example = "Contact!A1:F"
     */
    public static Object getColumnsLastObject(String spreadSheetId, String range) throws IOException {

        Object value = "";
        ValueRange response = mService_.spreadsheets().values().get(spreadSheetId, range).execute();
        List<List<Object>> values = response.getValues();
        if (values != null) {
            for (List row : values) {
                value = row;
            }
        }
        return value;
    }


    /**
     * Gets a list of objects of contacts from a spreadsheet:
     * https://docs.google.com/spreadsheets/d/....................../edit
     * @return List<MyContact>  Returns list of all pmdb parameter Objects
     * @throws IOException
     * @param range example = "pmdb!A1:A99"
     */
    public static List<Object> updateParametersInStaticDB(String spreadSheetId, String range) throws IOException {

        ValueRange response = mService_.spreadsheets().values().get(spreadSheetId, range).execute();
        List<List<Object>> values = response.getValues();

        List<Object> prameterValues = new ArrayList<>();

        if (values != null) {
            for (List row : values) {

                try {
                    Object pmName = row.get(0);
                    Object pmValue = row.get(1);
                    if (!pmValue.toString().isEmpty()) {
                        if (pmName.toString().equals("applicationName_"))
                            applicationName_ = pmValue.toString();
                        if (pmName.toString().equals("groupMessage_"))
                            groupMessage_ = pmValue.toString();
                        if (pmName.toString().equals("groupMessageOld_"))
                            groupMessageOld_ = pmValue.toString();
                        if (pmName.toString().equals("currentTimeStamp_"))
                            currentTimeStamp_ = pmValue.toString();
                        if (pmName.toString().equals("enableUpdateUI_"))
                            enableUpdateUI_ = Boolean.valueOf(pmValue.toString());
                        if (pmName.toString().equals("enableUpdateData_"))
                            enableUpdateData_ = Boolean.valueOf(pmValue.toString());
                        if (pmName.toString().equals("updateUIRefreshRate_"))
                            updateUIRefreshRate_ = Long.valueOf((String) pmValue);
                        if (pmName.toString().equals("updateDataRefreshRate_"))
                            updateDataRefreshRate_ = Long.valueOf((String) pmValue);
                        if (pmName.toString().equals("currentCountryCode"))
                            currentCountryCode = pmValue.toString();

                        //---------- Update ID's
                        if (pmName.toString().equals("spreadsheetId"))
                            spreadsheetId = pmValue.toString();
                        if (pmName.toString().equals("contactSheetId"))
                            contactSheetId = (int) pmValue;

                        //---------- Update Sheets Range
                        if (pmName.toString().equals("contactsSheetRange"))
                            contactsSheetRange = pmValue.toString();
                        if (pmName.toString().equals("pmdbSheetRange"))
                            pmdbSheetRange = pmValue.toString();
                        if (pmName.toString().equals("groupsSheetRange"))
                            groupsSheetRange = pmValue.toString();
                        if (pmName.toString().equals("messagesSheetRange"))
                            messagesSheetRange = pmValue.toString();
                        if (pmName.toString().equals("selectedGroupForGroupMessageSheetRange"))
                            selectedGroupForGroupMessageSheetRange = pmValue.toString();
                        if (pmName.toString().equals("messageLOGSheetRange"))
                            messageLOGSheetRange = pmValue.toString();

                        //---------- Update Sms API
                        if (pmName.toString().equals("signup"))
                            signup = pmValue.toString();
                        if (pmName.toString().equals("resign"))
                            resign = pmValue.toString();

                    }
                }catch (Exception r)
                {
                }
            }
        }
        return prameterValues;
    }

    /**
     *
     * @param spreadSheetId - gets from separate Tab where you have all your groups listed
     * @param range - is probably "Groups!A:A10" or something like that
     * @return
     * @throws IOException
     */
    public static List<String> getAllMessages(String spreadSheetId, String range) throws IOException
    {
        List<String> myGroups = new ArrayList<>();
        ValueRange response = mService_.spreadsheets().values().get(spreadSheetId, range).execute();
        List<List<Object>> values = response.getValues();
        if (values != null) {
            for (List row : values) {
                try {
                    //myGroups.add(row.get(0));
                    String gr = new String((String) row.get(0));
                    myGroups.add(gr);
                }catch (Exception r)
                {
                    r.printStackTrace();
                    return null;
                }
            }
        }
        return myGroups;
    }

}
