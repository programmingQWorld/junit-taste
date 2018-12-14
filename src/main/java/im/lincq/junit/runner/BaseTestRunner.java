package im.lincq.junit.runner;

import im.lincq.junit.framework.Test;
import im.lincq.junit.framework.TestListener;
import im.lincq.junit.framework.TestSuite;

import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.util.Properties;

/***
 * Base class for all test runners.
 */
public abstract class BaseTestRunner implements TestListener {
    static final String SUITE_METHODNAME = "suite";
    static Properties fPreferences;
    static int fMaxMessage = 200;

    protected TestSuiteLoader fTestLoader;

    /** Override to define how to handle a failed loading of a test suite. */
    protected abstract void runFailed(String message);

    public Test  getTest (String suiteClassName) {
        if (suiteClassName.length() <= 0) {
            clearStatus();
            return null;
        }
        Class testClass = null;
        try {
            testClass = loadSuiteClass(suiteClassName);
        } catch (NoClassDefFoundError e) {
            runFailed("Class definition \"" + suiteClassName + "\" not found");
            return null;
        } catch (Exception e) {
            runFailed("Class \"" + suiteClassName + "\" not found");
            return null;
        }
        Method suiteMethod = null;
        try {
            suiteMethod = testClass.getMethod(SUITE_METHODNAME, new Class[0]);
        } catch (Exception e) {
            clearStatus();
            return new TestSuite(testClass);
        }
        Test test = null;
        try {
            test = (Test)suiteMethod.invoke(null, new Class[0]); // static method
            if (test == null)
                return test;
        } catch (Exception e) {
            runFailed("Could not invoke the suite() method");
            return null;
        }
        clearStatus();
        return test;
    }

    protected Class loadSuiteClass(String suiteClassName) throws ClassNotFoundException {
        return fTestLoader.load(suiteClassName);
    }

    /**
     * Clears the status message
     */
    protected void clearStatus () {
    }

    /**
     * runcates a String to the maximum length.
     * 截断字符串
     */
    public static String truncate(String s) {
        if (s.length() > fMaxMessage)
            s = s.substring(0, fMaxMessage) + "...";
        return s;
    }

    /**
     * Returns the formatted string of the elapsed time
     * @param runTime
     * @return              1,543,994,500.045
     */
    public String elapsedTimeAsString (long runTime) {
        return NumberFormat.getInstance().format((double)runTime/1000);
    }

    public String extractClassName (String className) {
        if (className.startsWith("Default package for"))
            return className.substring(className.lastIndexOf(".") + 1);
        return className;
    }
}
