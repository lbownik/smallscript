package ss.runtime;

import java.util.List;
import static java.util.Collections.emptyList;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public interface SSObject {

    /****************************************************************************
     * Calls method of the encompassed object performing necessary computations
     * if needed.
     * @param stack a clean stack frame
     ****************************************************************************/
    default public SSObject invoke(final Stack stack, final String method,
            final List<SSObject> args) {

        return evaluate(stack.pushNewFrame()).invoke(stack.pushNewFrame(), method, args);
    }

    /****************************************************************************
    * Calls method of the encompassed object performing necessary computations
    * if needed.
    * @param stack a clean stack frame
    ****************************************************************************/
    default public SSObject invoke(final Stack stack, final String method) {

        return evaluate(stack.pushNewFrame()).invoke(stack.pushNewFrame(), method,
                emptyList());
    }

    /****************************************************************************
     * Returns an object which can accept method calls performing necessary
     * computations if needed.
     * @param stack a clean stack frame
     ****************************************************************************/
    default public SSObject evaluate(final Stack stack) {

        return this;
    }

    /****************************************************************************
     * 
    ****************************************************************************/
}
