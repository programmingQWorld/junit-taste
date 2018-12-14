package im.lincq.junit.framework;

/**
 * A TestFailure collects a failed test together with the caught exception.
 */
public class TestFailure {
    protected Test fFailedTest;
    protected Throwable fThrownException;



    public TestFailure (Test failedTest, Throwable thrownException) {
        fFailedTest = failedTest;
        fThrownException = thrownException;
    }

    public Test failedTest() {
        return fFailedTest;
    }

    public Throwable thrownException () {
        return fThrownException;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(fFailedTest + ": " + fThrownException.getMessage());
        return buffer.toString();
    }
}
