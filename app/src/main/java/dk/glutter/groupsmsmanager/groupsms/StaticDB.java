package dk.glutter.groupsmsmanager.groupsms;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luther on 10/11/2016.
 * This is SIMPLE Static DB - KISS
 */

public class StaticDB {
    public static String applicationName_ = "groupsmsmanager";
    public static String spreadsheetName = "groupsmsmanager_this_is_A_tryout"; // Name of the spread sheet, Drive api uses this name to find file in google drive
    public static String spreadsheetId = "1cl23PLE6F8-2Ky8Y2nb8USJeZjdHcbK7sHX78M_X3Mc"; // glutterdroid
    //public static String spreadsheetId = "1YJx7n2c8iqNXkcxe8eDh1y_ew9_gMOy3pKCTAFXlbI8"; // LME
    //public static String spreadsheetId = "1S6zauh24Rba2udATaZaUPp2RIok4KyW_AxBC1soZAC8"; // TEST
    public static int contactSheetId = 0;
    public static List<MyGroup> myGroups_;
    public static List<MyContact> myContacts_;
    public static String groupMessage_ = "";
    public static String groupMessageOld_ = "";
    public static com.google.api.services.sheets.v4.Sheets mService_ = null;
    public static String currentTimeStamp_;
    public static boolean enableUpdateUI_ = true;
    public static boolean enableUpdateData_ = true;
    public static boolean isPermissionToGoogleGranted = false;
    public static long updateUIRefreshRate_ = 3000; /// how often mobile synchronizes with online sheets - refresh rate
    public static long updateDataRefreshRate_ = 2000; /// how often mobile synchronizes with online sheets - refresh rate
    public static String currSenderNumber_= "004566554477";
    public static String contactsSheetRange = "Contact!A2:H";
    public static String pmdbSheetRange = "pmdb!A2:C99";
    public static String groupsSheetRange = "Groups!A1:A99";
    public static String messagesSheetRange = "SendMessage!B2:B2";
    public static String selectedGroupForGroupMessageSheetRange = "SendMessage!A2:A";
    public static String messageLOGSheetRange = "MessageLOG!A1:A";
    public static String signup = "Tilmeld";
    public static String resign = "Afmeld";
    public static ArrayList<String> words; /// [0]Signup [1]Group Name [2]Name
    public static String currentCountryCode = "45"; // + // 00 //
}