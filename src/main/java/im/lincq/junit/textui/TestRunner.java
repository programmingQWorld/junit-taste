package im.lincq.junit.textui;

import im.lincq.junit.framework.Test;
import im.lincq.junit.framework.TestFailure;
import im.lincq.junit.framework.TestResult;
import im.lincq.junit.runner.BaseTestRunner;
import im.lincq.junit.runner.StandardTestSuiteLoader;
import im.lincq.junit.runner.Version;

import java.io.PrintStream;
import java.text.NumberFormat;
import java.util.Enumeration;

public class TestRunner extends BaseTestRunner {

    PrintStream fWriter = System.out;

    public TestRunner () {
        // NOTE: 实现接口的命名方式.
        fTestLoader= new StandardTestSuiteLoader();
    }

    public TestRunner (PrintStream writer) {
        this();
        fWriter = writer;
    }

    /**
     * Runs a single test and collects its results.
     * This method can be used to start a test run
     * from your program.
     * */
    static public void run (Test suite) {
        TestRunner aTestRunner = new TestRunner();
        aTestRunner.doRun(suite, false);
    }

    protected TestResult doRun (Test suite, boolean wait) {
        TestResult result = createTestResult();
        result.addListener(this);
        long startTime = System.currentTimeMillis();
        suite.run(result);
        long endTime = System.currentTimeMillis();
        long runTime = endTime-startTime;
        writer().println();
        writer().println("Time: " + elapsedTimeAsString(runTime));
        print(result);
        writer().println();

        if (wait) {
            writer().println("RETURN to continue");
            try {
                System.in.read();
            } catch (Exception e) {
            }
        }
        return result;
    }

    /**
     * Creates the TestResult to be used for the test run.
     * @return
     */
    protected TestResult createTestResult () {
        return new TestResult();
    }


    /**
     * Prints the header of the report
     */
    public void printHeader (TestResult result) {
        if (result.wasSuccessful()) {
            writer().println();
            writer().println("OK");
            writer().println(" (" + result.runCount() + " tests)");
        } else {
            writer().println();
            writer().println("FAILURES!!!");
            writer().println("Test Results:");
            writer().println("(Run: " + result.runCount() +")" +
                             " (Failures: " + result.failureCount() + ")" +
                             " (Errors:" + result.errorCount()+ ")");
        }
    }


    /**
     * Prints the errors to the standard output
     * */
    public void printErrors(TestResult result) {
        // TODO: 思考： 为什么这里不使用三目运算符来提取 was && were 呢？
        if (result.errorCount() != 0) {
            if (result.errorCount() == 1)
                writer().println("There was " + result.errorCount() + " error:");
            else
                writer().println("There were " + result.errorCount() + " error:");
            int i = 1;
            for (Enumeration e = result.errors(); e.hasMoreElements(); i++) {
                TestFailure failure = (TestFailure)e.nextElement();
                writer().println(i+") " + failure.failedTest());
                failure.thrownException().printStackTrace(writer());
            }
        }
    }

    public void printFailures (TestResult result) {
        if (result.failureCount() == 1)
            writer().println("There was " + result.failureCount() + " failure:");
        else
            writer().println("There were " + result.failureCount() + " failures:");
        int i = 1;
        for (Enumeration e = result.failures(); e.hasMoreElements(); i++) {
            TestFailure failure = (TestFailure)e.nextElement();
            writer().println(i + ") " + failure.failedTest());
            Throwable t = failure.thrownException();

            if (t.getMessage() != null)
                writer().println("\""+ truncate(t.getMessage()) +"\"");
            else {
                writer().println();
                failure.thrownException().printStackTrace();
            }

        }
    }

    /**
     * Prints faulures to the standard output
     */
    public synchronized void print(TestResult result) {
        printHeader(result);
        printErrors(result);
        printFailures(result);
    }

    protected PrintStream writer () {
        return fWriter;
    }

    public void addError(Test test, Throwable t) {
        writer().print("E");
    }

    public void addFailure(Test test, Throwable t) {
        writer().print("F");
    }

    public void endTest(Test test) {

    }

    public void startTest(Test test) {

    }

    protected TestResult start(String args[]) throws Exception {
        String testCase = "";
        boolean wait= false;
        for (int i= 0; i < args.length; i++) {
            if (args[i].equals("-wait"))
                wait = true;
            else if (args[i].equals("-c"))
                testCase= extractClassName(args[++i]);
            else if (args[i].equals("-v"))
                System.out.println("JUnit " + Version.id() + " by Kent Beck and Erich Gamma");
        }

        if (testCase.equals(""))
            throw new Exception("Usage: TestRunner [-wait] testCaseName, where name is the name of the TestCase class");

        try {
            Test suite = getTest(testCase);
            return doRun(suite, wait);
        } catch (Exception e) {
            throw new Exception("Could not create and run with the run test suite:" + e);
        }
    }

    public static void main (String[] args) {
        TestRunner aTestRunner = new TestRunner();
        try {
            TestResult r = aTestRunner.start(args);
            if (!r.wasSuccessful())
                System.exit(-1);
            System.exit(0);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(-2);
        }
    }
}
