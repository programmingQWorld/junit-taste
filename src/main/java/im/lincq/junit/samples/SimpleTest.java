package im.lincq.junit.samples;

import im.lincq.junit.framework.Test;
import im.lincq.junit.framework.TestCase;
import im.lincq.junit.framework.TestSuite;
import im.lincq.junit.textui.TestRunner;

public class SimpleTest extends TestCase {

    protected int fValue1;
    protected int fValue2;
    protected void setUp() {
        fValue1= 2;
        fValue2= 3;
    }

    /**
     * Constructs a test case with the given name.
     * 构造指定名称的测试用例
     *
     * @param name      测试用例名称
     */
    public SimpleTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(SimpleTest.class);
    }
    public void testAdd () {
        System.out.println("+lincq-testing... testAdd()");
        double result = fValue1 + fValue2;
        // forced failure result==5
        assert(result == 6);
    }

    public void testDivideByZero() {
        System.out.println("+lincq-testing... testDivideByZero()");
        int zero= 0;
        //int result= 8/zero;
        int result= 8/1;
    }
    public void testEquals() {
        System.out.println("+lincq-testing... testEquals()");
        assertEquals(12, 12);
        assertEquals(12L, 12L);
        assertEquals(new Long(12), new Long(12));

        //assertEquals("Size", 12, 13);
        assertEquals("Size", 12, 12);
        //assertEquals("Capacity", 12.0, 11.99, 0.0);
        assertEquals("Capacity", 12.0, 12, 0.0);
    }


    public static void main (String[] args) {
        TestRunner.run(suite());
    }
}
