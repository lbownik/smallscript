package ss.runtime;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class SSNewVariableAssignment implements SSObject {

    /****************************************************************************
     * 
    ****************************************************************************/
    public SSNewVariableAssignment(final String variableName, final SSObject arg) {

        this.variableName = variableName;
        this.arg = arg;
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    @Override
    public SSObject evaluate(final Stack stack) {

        var value = this.arg.evaluate(stack.pushNewFrame());

        stack.addVariable(this.variableName, value);

        return value;
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    @Override
    public String toString() {

        return "!" + this.variableName + " = " + this.arg + "\n";
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    private final String variableName;
    private final SSObject arg;
}
