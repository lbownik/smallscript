package ss.runtime;

import java.util.List;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com {
 ******************************************************************************/
public final class SSNull extends SSDynamicObject {

    /****************************************************************************
     * 
    ****************************************************************************/
    public static SSNull instance() {

        return instance;
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    @Override
    public SSObject invoke(final Stack stack, final String method,
            final List<SSObject> args) {

        return switch (method) {
        case "asString" -> new SSString(toString());
        case "hash" -> new SSLong(hashCode());
        case "equals:" -> stack.get(this.equals(args.get(0).evaluate(stack)));
        default -> this;
        };
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    private SSNull() {

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

        return 0;
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    private final static SSNull instance = new SSNull();
    public final static String name = "null";
}
