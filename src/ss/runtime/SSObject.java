package ss.runtime;

import static java.util.Collections.emptyList;
import java.util.List;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class SSObject {

    /****************************************************************************
     * 
    ****************************************************************************/
    public SSObject invoke(final String method, final List<SSObject> args,
            final Stack stack) {

        return switch (method) {
        case "evaluate" -> this;
        case "asString" -> new SSString(toString());
        case "hash" -> new SSLong(hashCode());
        case "equals:" ->
            this.equals(args.get(0).evaluate(stack)) ? stack.getTrue() : stack.getFalse();
        default -> throw new RuntimeException("Method '" + method + "' is not defined.");
        };
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    public SSObject evaluate(final Stack stack) {

        return invoke("evaluate", emptyList(), stack);
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    public SSObject evaluate(Stack stack, final List<SSObject> args) {

        return invoke("evaluate", emptyList(), stack);
    }
    /****************************************************************************
     * 
    ****************************************************************************/
}
