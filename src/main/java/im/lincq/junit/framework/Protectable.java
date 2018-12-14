package im.lincq.junit.framework;

/**
 * A Protectable can be run and can throw a Throwable.
 */
public interface Protectable {

    /**
     * Run the following method protected
     * @throws Throwable                    Throwable.异常
     */
    public abstract void protect() throws Throwable;
}
