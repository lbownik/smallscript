package ss.runtime;

import java.util.List;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com {
 ******************************************************************************/
public final class SSFalse extends SSDynamicObject {

    /****************************************************************************
     * 
    ****************************************************************************/
    protected SSObject doClone() {

        return this;
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    @Override
    public SSObject invoke(final Stack stack, final String method,
            final List<SSObject> args) {

        return switch (method) {
        case "not" -> stack.getTrue();
        case "and:" -> this;
        case "or:", "xor:" -> args.get(0).evaluate(stack.pushNewFrame());
        case "ifTrue:" -> stack.getNull();
        case "ifFalse:" -> args.get(0).evaluate(stack.pushNewFrame());
        case "ifTrue::ifFalse:" -> args.get(1).evaluate(stack.pushNewFrame());
        default -> super.invoke(stack, method, args);
        };
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    @Override
    public String toString() {

        return name;
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    @Override
    public int hashCode() {

        return 1;
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    @Override
    public boolean equals(final Object obj) {

        return getClass().equals(obj.getClass());
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    public final static String name = "false";
}
