package im.lincq.junit.framework;

import java.util.Enumeration;
import java.util.Vector;

/**
 * A TestResult collects the results of executing a test case. It is an instance of the Collecting parameter pattern.
 * 收集参数模式
 * The test framework distinguishes between failures and errors.
 */
public class TestResult extends Object {
    protected Vector fFailures;
    protected Vector fErrors;
    protected Vector fListeners;
    protected int fRunTests;
    private boolean fStop;

    public TestResult () {
        fFailures = new Vector();
        fErrors = new Vector();
        fListeners = new Vector();
        fRunTests = 0;
        fStop = false;
    }

    public void startTest (Test test) {
        synchronized (this) {
            fRunTests++;
        }
    }

    /**
     * Runs a TestCase
     * 运行一次测试用例
     * @param test
     */
    protected void run (final TestCase test) {
        startTest(test);
        Protectable p = new Protectable () {
            public void protect() throws Throwable {
                test.runBare();
            }
        };

        runProtected(test, p);
        endTest(test);

    }

    public void endTest (Test test) {
        for (Enumeration e = cloneListeners().elements();e.hasMoreElements();) {
            ((TestListener)e.nextElement()).endTest(test);
        }
    }

    public void runProtected (final Test test, Protectable p) {
        try {
            p.protect();
        } catch (AssertionFailedError e) {
            addFailure(test, e);
        } catch (ThreadDeath e) { // don't catch ThreadDeath by accident
            throw e;
        } catch (Throwable e) {
            addError(test, e);
        }
    }

    /**
     * Adds an error to the list of errors. The passed in exception caused the error
     */
    public synchronized void addError (Test test, Throwable t) {
        fErrors.addElement(new TestFailure(test, t));
        for (Enumeration e = cloneListeners().elements(); e.hasMoreElements(); ) {
            ((TestListener)e.nextElement()).addError(test, t);
        }
    }

    public synchronized void stop () {
        fStop = true;
    }

    public synchronized void addFailure (Test test, AssertionFailedError t) {
        fFailures.addElement(new  TestFailure(test, t));
        // 发生了变化之后，就应该要同步监听
        for (Enumeration e = cloneListeners().elements(); e.hasMoreElements(); ) {
            ((TestListener)e.nextElement()).addFailure(test, t);
        }
    }

    /**
     * Returns a copy of the listeners.
     * 返回一个监听器的备份
     */
    public synchronized Vector cloneListeners () {
        return (Vector)fListeners.clone();
    }

    /**
     * Gets the number of run tests.
     */
    public synchronized int runCount() {
        return fRunTests;
    }

    /**
     * Returns an Enumeration for the errors
     * @return
     */
    public synchronized Enumeration errors () {
        return fErrors.elements();
    }

    /***
     * Gets the number of detected errors.
     * @return
     */
    public synchronized int errorCount () {
        return fErrors.size();
    }
    /***
     * Gets the number of detected failureCount.
     * @return
     */
    public synchronized int failureCount () {
        return fFailures.size();
    }

    public synchronized Enumeration failures () {
        return fFailures.elements();
    }

    /***
     * Register a TestListener
     * @param listener
     */
    public synchronized void addListener (TestListener listener) {
        fListeners.addElement(listener);
    }

    /**
     * Gets the number of detected errors.
     * @return
     */
    public synchronized int testFailures () {
        return errorCount();
    }

    /**
     * Gets the number of detected failures.
     * @return
     */
    public synchronized int testErrors () {
        return failureCount();
    }


    /**
     * Checks whether the test run should stop
     * 检查测试用例是否应当停止.
     * @return
     */
    public synchronized boolean shouldStop () {
        return fStop;
    }

    /**
     * Returns whether the entire test was successful or not.
     * @return
     */
    public synchronized boolean wasSuccessful () {
        return testFailures() == 0  && testErrors() == 0;
    }
}
