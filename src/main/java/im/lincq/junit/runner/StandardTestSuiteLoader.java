package im.lincq.junit.runner;

public class StandardTestSuiteLoader implements TestSuiteLoader {
    public Class load(String suiteClassName) throws ClassNotFoundException {
        return Class.forName(suiteClassName);
    }

    public Class reload(Class aClass) throws ClassNotFoundException {
        // NOTE: 没有看出来reload的方式呀.
        return aClass;
    }
}
