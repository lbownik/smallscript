package ss.runtime;

import static java.util.Collections.emptyList;
import java.util.List;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class SSBlock extends SSDynamicObject {

    /****************************************************************************
     * 
    ****************************************************************************/
    public SSBlock(final List<SSObject> statements, final List<String> argumentNames) {

        this.statements = statements;
        this.argumentNames = argumentNames;
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    @Override
    public SSObject invoke(final String method, final List<SSObject> args,
            final Stack stack) {

        return switch (method) {
        case "evaluate", "evaluateWith:", "evaluateWith:and:", "evaluateWith:and:and:",
                "evaluateWith:and:and:and:", "evaluateWith:and:and:and:and:",
                "evaluateWith:and:and:and:and:and:" ->
            evaluate(stack, args);
        case "whileTrue:" -> whileTrue(args, stack);
        default -> super.invoke(method, args, stack);
        };
    }
    
    /****************************************************************************
     * 
    ****************************************************************************/
    private SSObject whileTrue(final List<SSObject> args,
            final Stack stack) {
        
        var result = stack.getNull();
        
        for(;;) {
        
            var r = evaluate(stack, emptyList());
            if(r.equals(stack.getTrue())) {
            result = args.get(0).evaluate(stack, emptyList());
            } else {
                break;
            }
        }
        return result;
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    public SSObject evaluate(Stack stack, final List<SSObject> args) {

        stack = stack.pushNewFrame();
        initiateLocalVariables(stack, args);

        SSObject result = stack.getNull();

        for (final SSObject statement : this.statements) {
            result = statement.evaluate(stack);
        }

        return result;
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    private void initiateLocalVariables(final Stack stack, final List<SSObject> args) {

        for (int index = 0; index < this.argumentNames.size(); ++index) {
            stack.addVariable(this.argumentNames.get(index), args.get(index));
        }
    }

    /****************************************************************************
    * 
    ****************************************************************************/
    public SSObject evaluate(final Stack stack) {

        return this;
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    @Override
    public String toString() {

        final StringBuilder sb = new StringBuilder();

        sb.append('\n').append(this.argumentNames).append("|\n");
        this.statements.forEach(sb::append);

        return sb.toString();
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    protected int size() {

        return this.statements.size();
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    private final List<SSObject> statements;
    private final List<String> argumentNames;
}
