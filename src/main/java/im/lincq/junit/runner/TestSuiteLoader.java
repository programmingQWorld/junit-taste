package im.lincq.junit.runner;

/**
 * An interface to define how a test suite shoule be loaded
 * 定义测试套件该如何被加载【直接加载，重新加载】
 */
public interface TestSuiteLoader {
    abstract public Class load(String suiteClassName) throws ClassNotFoundException;
    abstract public Class reload(Class aClass) throws ClassNotFoundException;
}
