package im.lincq.junit.framework;

public interface Test {
    /**
     * Count the number of test cases that will be run by this test.
     * 测试用例个数
     */
    public abstract int countTestCases();

    /**
     * Runs a test and collects its result in a TestResult instance.
     * 执行一个测试用例并且收集它的运行结果。
     * */
    public abstract void run (TestResult result);
}
