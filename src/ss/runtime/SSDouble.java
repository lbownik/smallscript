package ss.runtime;

import java.util.List;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com {
 ******************************************************************************/
public final class SSDouble extends SSDynamicObject {

    /****************************************************************************
     * 
    ****************************************************************************/
    public SSDouble(final double value) {

        this.value = Double.valueOf(value);
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    @Override
    public SSObject invoke(final Stack stack, final String method,
            final List<SSObject> args) {

        return switch (method) {
        case "+" -> new SSDouble(this.value + evaluateFirst(args, stack));
        case "-" -> new SSDouble(this.value - evaluateFirst(args, stack));
        case "*" -> new SSDouble(this.value * evaluateFirst(args, stack));
        case "/" -> new SSDouble(this.value / evaluateFirst(args, stack));
        case "==" -> stack.get(this.value == evaluateFirst(args, stack));
        case "<>" -> stack.get(this.value != evaluateFirst(args, stack));
        case ">" -> stack.get(this.value > evaluateFirst(args, stack));
        case "<" -> stack.get(this.value < evaluateFirst(args, stack));
        case ">=" -> stack.get(this.value >= evaluateFirst(args, stack));
        case "<=" -> stack.get(this.value <= evaluateFirst(args, stack));
        default -> super.invoke(stack, method, args);
        };
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    private static double evaluateFirst(final List<SSObject> args, final Stack stack) {

        var first = args.get(0).evaluate(stack.pushNewFrame());

        if (first instanceof SSDouble d) {
            return d.value;
        } else if (first instanceof SSLong l) {
            return l.value;
        } else {
            throw new RuntimeException(
                    "Cannot cast " + first.getClass() + " to SSDouble.");
        }
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    @Override
    public String toString() {

        return Double.toString(this.value);
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    @Override
    public int hashCode() {

        return Double.hashCode(this.value);
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    @Override
    public boolean equals(final Object o) {

        return getClass() == o.getClass()
                && Double.compare(this.value, ((SSDouble) o).value) == 0;

    }

    /****************************************************************************
     * 
    ****************************************************************************/
    public final double value;
}
