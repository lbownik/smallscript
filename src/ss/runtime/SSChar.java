package ss.runtime;

import java.util.List;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com {
 ******************************************************************************/
public final class SSChar extends SSDynamicObject {

    /****************************************************************************
     * 
    ****************************************************************************/
    public SSChar(final char value) {

        this.value = value;
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    @Override
    public SSObject invoke(final String method, final List<SSObject> args,
            final Stack stack) {

        return switch (method) {
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
    private static char evaluateFirst(final List<SSObject> args, final Stack stack) {

        return ((SSChar) args.get(0).evaluate(stack.pushNewFrame())).value;
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

        return Character.toString(this.value);
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    @Override
    public int hashCode() {

        return Character.hashCode(this.value);
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    @Override
    public boolean equals(final Object o) {

        return o != null && getClass() == o.getClass()
                && this.value == ((SSChar) o).value;

    }

    /****************************************************************************
     * 
    ****************************************************************************/
    private final char value;
}
