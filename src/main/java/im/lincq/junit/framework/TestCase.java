package im.lincq.junit.framework;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class TestCase extends Assert implements Test {
    /**
     * the name of the test case;
     * 测试用例名称
     */
    private final String fName;

    /**
     * Constructs a test case with the given name.
     * 构造指定名称的测试用例
     * @param name
     */
    public TestCase(String name) {
        fName = name;
    }

    public int countTestCases() {
        return 1;
    }

    /**
     * Cretes a default TestResult object
     * @return 默认的TestResult实例，用来收藏测试结果对象.
     */
    private TestResult createResult() {
        return new TestResult();
    }

    /**
     * Get the name of the test case.
     * @return
     */
    public String name () {
        return fName;
    }

    public TestResult run() {
        TestResult result = createResult();
        run(result);
        return result;
    }

    public void run (TestResult result) {
        result.run(this);
    }

    /**
     * Runs the bare test sequence.
     * @throws Throwable if any exception is thrown
     */
    public void runBare() throws Throwable {
        setUp();
        try {
            runTest();
        }
        finally {
            tearDown();
        }
    }

    /**
     * Sets up the fixture(固定物), for example, open a network connection.
     * This method is called before a test is executed;
     * @throws Exception
     */
    protected void setUp () throws Exception {}

    /**
     * Sets up the fixture(固定物), for example, close a network connection.
     * This method is called after a test is executed.
     * @throws Exception
     */
    protected void tearDown () throws Exception {}

    protected void runTest() throws Throwable {
        Method runMethod= null;
        try {
            runMethod = getClass().getMethod(fName, new Class[0]);
        } catch (NoSuchMethodException e) {
            fail("Method \"" + fName + "\" now found");
        }
        if (runMethod != null && !Modifier.isPublic(runMethod.getModifiers())) {
            fail("Method \""+ fName +"\" should be public");
        }

        try {
            runMethod.invoke(this, new Class[0]);
        } catch (InvocationTargetException e) {
            // TODO: qu liaoojie fillInStackTrace() fangfa de zuoyong
            e.fillInStackTrace();
            // TODO: qu liaoojie getTargetException() fangfa de zuoyong
            throw e.getTargetException();
        } catch (IllegalAccessException e) {
            e.fillInStackTrace();
            throw e;
        }
    }

    /**
     * Returns a string representation of the test case
     */
    public String toString() {
        return name()+"("+getClass().getName()+")";
    }
}
