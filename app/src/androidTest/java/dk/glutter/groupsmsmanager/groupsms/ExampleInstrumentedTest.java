package dk.glutter.groupsmsmanager.groupsms;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import dk.glutter.groupsmsmanager.groupsms.API.SheetsHandler;
import dk.glutter.groupsmsmanager.groupsms.SMS.SmsHandler;
import dk.glutter.groupsmsmanager.groupsms.SMS.StringValidator;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("dk.glutter.groupsmsmanager.groupsms", appContext.getPackageName());
    }

    public static void test_1_foreignNumber()
    {
        StringValidator stringValidator = new StringValidator();

        assertFalse(stringValidator.isForeignNumber("004577889944"));
        assertFalse(stringValidator.isForeignNumber("+4577889944"));
        assertFalse(stringValidator.isForeignNumber("77889944"));


        assertTrue(stringValidator.isForeignNumber("004177889944"));
        assertTrue(stringValidator.isForeignNumber("004777889944"));
        assertTrue(stringValidator.isForeignNumber("004077889944"));
        assertTrue(stringValidator.isForeignNumber("+4077889944"));
        assertTrue(stringValidator.isForeignNumber("4077889944"));
        assertTrue(stringValidator.isForeignNumber("9944"));
    }
}
