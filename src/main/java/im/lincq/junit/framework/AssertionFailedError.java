package im.lincq.junit.framework;

/**
 * paochu zhezhong yichang, yinggai shi bijiao weixiande yichang, zhongzhi zhengge yingyong
 */
public class AssertionFailedError extends Error {

    public AssertionFailedError () {}
    public AssertionFailedError (String message) {
        super(message);
    }
}
