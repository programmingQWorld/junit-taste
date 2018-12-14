package im.lincq.junit.samples;

import im.lincq.junit.framework.Test;
import im.lincq.junit.framework.TestSuite;
import im.lincq.junit.textui.TestRunner;

public class AllTest {
    public static void main (String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite () {
        TestSuite suite = new TestSuite("All JUnit Tests");
        suite.addTest(SimpleTest.suite());
        return suite;
    }
}
