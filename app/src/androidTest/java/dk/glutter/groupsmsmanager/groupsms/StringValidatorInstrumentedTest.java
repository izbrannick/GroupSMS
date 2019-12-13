package dk.glutter.groupsmsmanager.groupsms;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static dk.glutter.groupsmsmanager.groupsms.StaticDB.myGroups_;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class StringValidatorInstrumentedTest {

    public ArrayList myGroups;
    public Context appContext;
    public StringValidator stringValidator;

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
    }

    @Test
    public void useAppContext() throws Exception {

        assertEquals("dk.glutter.groupsmsmanager.groupsms", appContext.getPackageName());

    }
    @Test
    public void test_1_foreignNumber() throws Exception
    {
        assertFalse(stringValidator.isForeignNumber("004577889944"));
        assertFalse(stringValidator.isForeignNumber("+4577889944"));
        assertFalse(stringValidator.isForeignNumber("77889944"));



        assertTrue(stringValidator.isForeignNumber("004177889944"));
        assertTrue(stringValidator.isForeignNumber("004777889944"));
        assertTrue(stringValidator.isForeignNumber("004077889944"));
        assertTrue(stringValidator.isForeignNumber("+4077889944"));
        assertTrue(stringValidator.isForeignNumber("4077889944"));
        assertTrue(stringValidator.isForeignNumber("9944"));
        assertTrue(stringValidator.isForeignNumber("0000"));
        assertTrue(stringValidator.isForeignNumber("0"));
        assertTrue(stringValidator.isForeignNumber(""));
        assertTrue(stringValidator.isForeignNumber(null));
    }
    @Test
    public void test_2_formatNumber() throws Exception
    {
        assertEquals("77885522", StringValidator.formatNumber("004577885522"));
        assertEquals("77885522", StringValidator.formatNumber("+4577885522"));
        assertEquals("77885522", StringValidator.formatNumber("123456789077885522"));
        assertEquals("77885522", StringValidator.formatNumber("00123456789077885522"));
        assertEquals("77885522", StringValidator.formatNumber("+123456789077885522"));
    }
    @Test
    public void test_3_isSignup() throws Exception
    {
        assertTrue(StringValidator.isSignup("Tilmeld 0"));
        assertTrue(StringValidator.isSignup("tilmeld 0"));
        assertTrue(StringValidator.isSignup("tilmeld gruppe1"));
        assertTrue(StringValidator.isSignup("TILMELD gruppe1"));
        assertTrue(StringValidator.isSignup("TILMELD mig til gruppen"));
        assertTrue(StringValidator.isSignup("TILMELD mig til gruppen sdlk lsdkjfoeut3489 wæjøaeg'aådpfg'søog´4'gsé4g´soe'´sdfåg'åo1'å½o'åp½o20å349+20"));

        assertFalse(StringValidator.isSignup(null));
        assertFalse(StringValidator.isSignup(""));
        assertFalse(StringValidator.isSignup("TILMELD"));
        assertFalse(StringValidator.isSignup("TILMELD "));
        assertFalse(StringValidator.isSignup("Tilmeld"));
        assertFalse(StringValidator.isSignup("Tilmeld "));
        assertFalse(StringValidator.isSignup("tilmeld"));
        assertFalse(StringValidator.isSignup("tilmeld "));
        assertFalse(StringValidator.isSignup("TILMELDE"));
        assertFalse(StringValidator.isSignup("Afmeld"));
        assertFalse(StringValidator.isSignup("TILMELDE mig til gruppen lækæ kælkæalk sæda k 'daælsdk'ad la'sl kd'asldpwapod aæld29u1209 u40u3402u3"));
    }
    @Test
    public void test_4_isResign() throws Exception
    {
        assertTrue(StringValidator.isResign("Afmeld fghjkl hjkl"));
        assertTrue(StringValidator.isResign("afmeld fghjkl hjkl"));
        assertTrue(StringValidator.isResign("afmeld fghjkl"));
        assertTrue(StringValidator.isResign("afmeld 0"));
        assertTrue(StringValidator.isResign("afmeld gruppe1"));
        assertTrue(StringValidator.isResign("AFMELD gruppe1"));
        assertTrue(StringValidator.isResign("AFMELD mig til gruppen"));
        assertTrue(StringValidator.isResign("AFMELD mig til gruppen sdlk lsdkjfoeut3489 wæjøaeg'aådpfg'søog´4'gsé4g´soe'´sdfåg'åo1'å½o'åp½o20å349+20"));

        assertFalse(StringValidator.isResign(null));
        assertFalse(StringValidator.isResign(""));
        assertFalse(StringValidator.isResign("AFMELD"));
        assertFalse(StringValidator.isResign("AFMELD "));
        assertFalse(StringValidator.isResign("Afmeld"));
        assertFalse(StringValidator.isResign("Afmeld "));
        assertFalse(StringValidator.isResign("afmeld"));
        assertFalse(StringValidator.isResign("afmeld "));
        assertFalse(StringValidator.isResign("AFMELDE"));
        assertFalse(StringValidator.isResign("Afmeld"));
        assertFalse(StringValidator.isResign("AFMELDE mig til gruppen lækæ kælkæalk sæda k 'daælsdk'ad la'sl kd'asldpwapod aæld29u1209 u40u3402u3"));
    }

    @Test
    public void test_5_isGroupMessage()
    {
        assertTrue(myGroups_.size() == 3);

        assertTrue(stringValidator.isGroupMessage("Foo this is a group message"));
        assertTrue(stringValidator.isGroupMessage("foo this is a group message"));
        assertTrue(stringValidator.isGroupMessage("FOO this is a group message"));

    }

    @Test
    public void test_6_getCurrentGroup()
    {
        assertTrue(myGroups_.size() == 3);

        String grIDFoo = myGroups_.get(0).getUniqueID();
        String grIDGASik = myGroups_.get(1).getUniqueID();
        MyGroup currentGrFoo =  stringValidator.getCurrentGroup("Foo this is a group message");
        MyGroup currentGrGASik =  stringValidator.getCurrentGroup("GASIK this is a group message");

        assertFalse(currentGrFoo == null);
        assertFalse(currentGrGASik == null);
        assertTrue(currentGrFoo.getUniqueID().equals(grIDFoo));
        assertTrue(currentGrGASik.getUniqueID().equals(grIDGASik));

    }
}