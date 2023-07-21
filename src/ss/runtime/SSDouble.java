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
    public SSObject invoke(final String method, final List<SSObject> args,
            final Stack stack) {

        return switch (method) {
        case "+" -> new SSDouble(this.value + evaluateFirst(args, stack));
        case "-" -> new SSDouble(this.value - evaluateFirst(args, stack));
        case "*" -> new SSDouble(this.value * evaluateFirst(args, stack));
        case "/" -> new SSDouble(this.value / evaluateFirst(args, stack));
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
    private static double evaluateFirst(final List<SSObject> args, final Stack stack) {

        return ((SSDouble) args.get(0).evaluate(stack.pushNewFrame())).value;
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

        return o != null && getClass() == o.getClass()
                && Double.compare(this.value, ((SSDouble) o).value) == 0;

    }

    /****************************************************************************
     * 
    ****************************************************************************/
    public final double value;
}
