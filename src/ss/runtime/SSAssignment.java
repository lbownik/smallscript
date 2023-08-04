package ss.runtime;

import java.util.function.BiConsumer;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class SSAssignment implements SSObject {

    /****************************************************************************
     * 
    ****************************************************************************/
    public SSAssignment(final String variableName, final SSObject arg) {

        if (variableName.startsWith("!")) {
            this.variableName = variableName.substring(1);
            this.setVariable = (stack, value) -> stack.addVariable(this.variableName,
                    value);
        } else {
            this.variableName = variableName;
            this.setVariable = (stack, value) -> stack.setVariable(this.variableName,
                    value);
        }
        this.arg = arg;
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    @Override
    public SSObject evaluate(final Stack stack) {

        var value = this.arg.evaluate(stack.pushNewFrame());

        this.setVariable.accept(stack, value);

        return value;
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    @Override
    public String toString() {

        return this.variableName + " = " + this.arg;
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    private final String variableName;
    private final SSObject arg;
    private final BiConsumer<Stack, SSObject> setVariable;
}
