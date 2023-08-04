package ss.runtime;

import java.util.List;
import java.util.Map;

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

        this.value = value;
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    private SSLong(final Map<String, SSBlock> methods, final Map<String, SSObject> fields,
            final long value) {

        super(methods, fields);
        this.value = value;
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    protected SSObject doClone() {

        return new SSLong(this.methods, this.fields, this.value);
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    @Override
    public SSObject invoke(final Stack stack, final String method,
            final List<SSObject> args) {

        return switch (method) {
        case "plus:" -> add(stack, args);
        case "minus:" -> substract(stack, args);
        case "multipliedBy:" -> multiply(stack, args);
        case "dividedBy:" -> divide(stack, args);
        case "isGreaterThan:" -> stack.get(this.value > evaluateFirst(args, stack));
        case "isLessThan:" -> stack.get(this.value < evaluateFirst(args, stack));
        case "isGreaterOrEqualTo:" -> stack.get(this.value >= evaluateFirst(args, stack));
        case "isLessOrEqualTo:" -> stack.get(this.value <= evaluateFirst(args, stack));
        default -> super.invoke(stack, method, args);
        };
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    private SSObject add(final Stack stack, final List<SSObject> args) {

        final var arg = args.get(0).evaluate(stack.pushNewFrame());

        if (arg instanceof SSLong l) {
            return new SSLong(this.value + l.value);
        } else if (arg instanceof SSDouble d) {
            return new SSDouble(this.value + d.value);
        } else {
            throw new RuntimeException(
                    "Cannot cast " + arg.getClass() + " to SSLong or SSDouble");
        }
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    private SSObject substract(final Stack stack, final List<SSObject> args) {

        final var arg = args.get(0).evaluate(stack.pushNewFrame());

        if (arg instanceof SSLong l) {
            return new SSLong(this.value - l.value);
        } else if (arg instanceof SSDouble d) {
            return new SSDouble(this.value - d.value);
        } else {
            throw new RuntimeException(
                    "Cannot cast " + arg.getClass() + " to SSLong or SSDouble");
        }
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    private SSObject multiply(final Stack stack, final List<SSObject> args) {

        final var arg = args.get(0).evaluate(stack.pushNewFrame());

        if (arg instanceof SSLong l) {
            return new SSLong(this.value * l.value);
        } else if (arg instanceof SSDouble d) {
            return new SSDouble(this.value * d.value);
        } else {
            throw new RuntimeException(
                    "Cannot cast " + arg.getClass() + " to SSLong or SSDouble");
        }
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    private SSObject divide(final Stack stack, final List<SSObject> args) {

        final var arg = args.get(0).evaluate(stack.pushNewFrame());

        if (arg instanceof SSLong l) {
            return new SSLong(this.value / l.value);
        } else if (arg instanceof SSDouble d) {
            return new SSDouble(this.value / d.value);
        } else {
            throw new RuntimeException(
                    "Cannot cast " + arg.getClass() + " to SSLong or SSDouble");
        }
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

        return getClass() == o.getClass() && this.value == ((SSLong) o).value;
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    public final long value;
}
