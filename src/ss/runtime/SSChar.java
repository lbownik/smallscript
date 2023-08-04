package ss.runtime;

import java.util.List;
import java.util.Map;

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
    private SSChar(final Map<String, SSBlock> methods, final Map<String, SSObject> fields,
            final char value) {

        super(methods, fields);
        this.value = value;
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    protected SSObject doClone() {

        return new SSChar(this.methods, this.fields, this.value);
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    @Override
    public SSObject invoke(final Stack stack, final String method,
            final List<SSObject> args) {

        return switch (method) {
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
    private static char evaluateFirst(final List<SSObject> args, final Stack stack) {

        return ((SSChar) args.get(0).evaluate(stack.pushNewFrame())).value;
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

        return getClass() == o.getClass() && this.value == ((SSChar) o).value;

    }

    /****************************************************************************
     * 
    ****************************************************************************/
    private final char value;
}
