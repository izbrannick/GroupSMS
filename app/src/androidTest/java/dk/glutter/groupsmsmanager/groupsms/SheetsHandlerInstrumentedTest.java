package dk.glutter.groupsmsmanager.groupsms;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import dk.glutter.groupsmsmanager.groupsms.API.SheetsHandler;

import static dk.glutter.groupsmsmanager.groupsms.StaticDB.myGroups_;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class SheetsHandlerInstrumentedTest {

    public ArrayList myGroups;
    public Context appContext;
    public StringValidator stringValidator;
    public SheetsHandler sheetsHandler;

    String test_spreadsheetId = "1S6zauh24Rba2udATaZaUPp2RIok4KyW_AxBC1soZAC8";
    String test_spreadsheetArkId = "1991659608";

    /**
     * Creates initial test dependencies
     */
    @Before
    public void setUp()
    {
        // Context of the app under test.
        appContext = InstrumentationRegistry.getTargetContext();

        stringValidator = new StringValidator();
        myGroups = new ArrayList<>();

        MyGroup groupFoo = new MyGroup("foo");
        MyGroup groupGASik = new MyGroup("GASik");
        MyGroup groupBAZ = new MyGroup("BAZ");

        myGroups.add(groupFoo);
        myGroups.add(groupGASik);
        myGroups.add(groupBAZ);

        myGroups_ = myGroups;


        sheetsHandler = new SheetsHandler();
    }

    @Test
    public void test_1_appendToSheets() throws Exception {


        //Object appendValuesResponse1 = sheetsHandler.getColumnsLastObject(test_spreadsheetId,"Contact!A1:F");
        //AppendValuesResponse appendValuesResponse2 = sheetsHandler.appendValue(test_spreadsheetId, test_spreadsheetArkId, "This is test append from Integration test");
    }
}