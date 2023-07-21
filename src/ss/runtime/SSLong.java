package ss.runtime;

import java.util.List;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com {
 ******************************************************************************/
public final class SSLong extends SSDynamicObject {

    /****************************************************************************
     * 
    ****************************************************************************/
    public SSLong(final long value) {

        this.value = value;
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    public SSLong(final int value) {

        this.value = Long.valueOf(value);
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    public static SSLong zero() {

        return zero;
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    public static SSLong one() {

        return one;
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    @Override
    public SSObject invoke(final String method, final List<SSObject> args,
            final Stack stack) {

        return switch (method) {
        case "+" -> new SSLong(this.value + evaluateFirst(args, stack));
        case "-" -> new SSLong(this.value - evaluateFirst(args, stack));
        case "*" -> new SSLong(this.value * evaluateFirst(args, stack));
        case "/" -> new SSLong(this.value / evaluateFirst(args, stack));
        case "==" -> toBool(this.value == evaluateFirst(args, stack), stack);
        case "!=" -> toBool(this.value != evaluateFirst(args, stack), stack);
        case ">" -> toBool(this.value > evaluateFirst(args, stack), stack);
        case "<" -> toBool(this.value < evaluateFirst(args, stack), stack);
        case ">=" -> toBool(this.value >= evaluateFirst(args, stack), stack);
        case "<=" -> toBool(this.value <= evaluateFirst(args, stack), stack);
        default -> super.invoke(method, args, stack);
        };
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    private static long evaluateFirst(final List<SSObject> args, final Stack stack) {

        return ((SSLong) args.get(0).evaluate(stack.pushNewFrame())).value;
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    private static SSObject toBool(final boolean condition, final Stack stack) {

        return condition ? stack.getTrue() : stack.getFalse();
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    @Override
    public String toString() {

        return Long.toString(this.value);
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    @Override
    public int hashCode() {

        return Long.hashCode(this.value);
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    public int intValue() {

        return (int) this.value;
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    @Override
    public boolean equals(final Object o) {

        return o != null && getClass() == o.getClass()
                && this.value == ((SSLong) o).value;
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    public final long value;

    private final static SSLong zero = new SSLong(0);
    private final static SSLong one = new SSLong(1);
}
