package ss.runtime;

import java.util.List;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class SSExpression extends SSObject {

    /****************************************************************************
     * 
    ****************************************************************************/
    public SSExpression(final SSObject object, final String method,
            final List<SSObject> args) {

        this.object = object;
        this.method = method;
        this.args = args;
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    @Override
    public SSObject invoke(final String method, final List<SSObject> args,
            final Stack stack) {

        return switch (method) {
        case "evaluate" -> evaluateThis(stack);
        default -> super.invoke(this.method, this.args, stack);
        };
    }

    /****************************************************************************
    * 
    ****************************************************************************/
    public SSObject evaluate(final Stack stack) {

        return invoke("evaluate", this.args, stack);
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    private SSObject evaluateThis(final Stack stack) {

        return this.object.evaluate(stack.pushNewFrame()).invoke(this.method, this.args,
                stack);
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    @Override
    public String toString() {

        return this.object + " " + this.method + " " + this.args;
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    private final SSObject object;
    private final String method;
    private final List<SSObject> args;
}
