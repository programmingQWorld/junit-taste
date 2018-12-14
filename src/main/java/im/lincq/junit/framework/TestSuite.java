package im.lincq.junit.framework;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Enumeration;
import java.util.Vector;

/**
 * 测试套件类
 */
public class TestSuite implements Test {

    private Vector fTest = new Vector(10);
    private String fName;

    /**
     * Constructs an empty TestSuite.
     */
    public TestSuite() {}

    /**
     * Constructs a TestSuite from the given class. Adds all the methods
     * starting with "test" as test cases to the suite.
     * @param theClass
     */
    public TestSuite(final Class theClass) {
        fName= theClass.getName();

        Constructor constructor = getConstructor(theClass);
        if (!Modifier.isPublic(theClass.getModifiers())) {
            addTest( warning("Class " + theClass.getName() + " is not public ") );
            return;
        }
        if (constructor == null) {
            addTest( warning("Class " + theClass.getName() + " has no public constructor TestCasse(String name)") );
            return;
        }

        Class superClass = theClass;
        Vector names = new Vector();
        while (Test.class.isAssignableFrom(superClass)) {
            Method[] methods = superClass.getDeclaredMethods();
            for (int i=0; i< methods.length; i++) {
                addTestMethod(methods[i], names, constructor);
            }
            superClass = superClass.getSuperclass();
        }
        if (fTest.size() == 0) {
            addTest( warning("No tests found in " + theClass.getName()) );
        }
    }


    /**
     * 获取方法名称，如果Vector集合中不包含方法名称，return
     * 检查方法是否有public修饰符修饰
     *   有：
     *       将方法名称添加至集合
     *       创建对象数组，通过构造器构造实例对象.并且转型为Test, 调用addTest方法
     *       过程中若出现异常，添加 warning 信息（“Cannot instantiate test case: ” + name）;
     *   没有：
     *       查看方法对象m是否测试方法. 如果是--> warning("Test method isn't public: " + m.getName());
     *
     * 小结：此函数的执行过程是：将指定的方法添加到【测试方法容器中】
     * */
    private void addTestMethod(Method m, Vector names, Constructor constructor) {

        String name = m.getName();
        if (names.contains(name))
            return;
        if (isPublicTestMethod(m)) {
            names.addElement(name);
            // args: 构造器所需参数
            Object[] args = new Object[] {name};
            try {
                // ?
                addTest((Test)constructor.newInstance(args));
            } catch (InstantiationException
                    | IllegalAccessException
                    | InvocationTargetException e) {
                e.printStackTrace();
            }
        } else { // almost a test method
            if (isTestMehtod(m)) {
                addTest(warning("Test method isn't public : " + m.getName()));
            }
        }
    }

    public TestSuite (String name) {
        fName = name;
    }




    private Constructor getConstructor (Class theClass) {
        Class[] args = {String.class};
        Constructor c = null;
        try {
            c = theClass.getConstructor(args);
        } catch (NoSuchMethodException e) {
        }
        return c;
    }
    /**
     * 获取方法对象描述的方法名称
     * 获取方法对象m描述的形式参数列表类型
     * 获取方法对象m描述的返回返回值类型
     * 对比：参数长度为0 && 方法名称前缀为"test" && 返回类型为空类型
     *
     * 小结：由此可以清楚了解到当前版本junit中，作为测试方法的条件.
     */
    private boolean isTestMehtod (Method m) {

        String name = m.getName();
        Class[] parameters = m.getParameterTypes();
        Class returnType = m.getReturnType();
        return parameters.length == 0 && name.startsWith("test") && returnType.equals(Void.TYPE);
    }

    /**
     * 返回是否为测试方法 && 并且方法对象m由public修饰
     * */
    private boolean isPublicTestMethod (Method m) {
        return isTestMehtod(m) && Modifier.isPublic(m.getModifiers());
    }

    /**
     * Add a test to the suit.
     */
    public void addTest (Test test) {
        fTest.addElement(test);
    }

    public int countTestCases() {
        return 0;
    }

    /**
     * Runs the tests and collects their result in a TestResult
     * 执行测试用例（多个）并且讲结果收集在TestResult中.
     * @param result
     */
    public void run(TestResult result) {
        for (Enumeration e = tests(); e.hasMoreElements(); ) {
            if (result.shouldStop() )
                break;
            Test test = (Test)e.nextElement();
            runTest(test, result);
        }
    }

    public void runTest(Test test, TestResult result) {
        test.run(result);
    }


    /**
     * Retruns the tests as an enumeration.
     * 返回包含所有测试用例的枚举
     * @return
     */
    public Enumeration tests() {
        return fTest.elements();
    }

    /**
     * Returns a test which iwll fail and log  a warning message.
     * @param message
     * @return
     */
    private Test warning (final String message) {
        return new TestCase("warning") {
            protected void runTest() {
                fail(message);
            }
        };
    }
}
