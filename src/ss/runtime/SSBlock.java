package ss.runtime;

import static java.util.Collections.emptyList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private SSBlock(final Map<String, SSBlock> methods,
            final Map<String, SSObject> fields, final List<SSObject> statements,
            final List<String> argumentNames) {

        super(methods, fields);
        this.statements = statements;
        this.argumentNames = argumentNames;
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    protected SSObject doClone() {

        return new SSBlock(this.methods, this.fields, this.statements,
                this.argumentNames);
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    @Override
    public SSObject invoke(final Stack stack, final String method,
            final List<SSObject> args) {

        if (method.startsWith("execute")) {
            return execute(stack, args);
        } else {
            return switch (method) {
            case "whileTrue:" -> whileTrue(stack, args);
            default -> super.invoke(stack, method, args);
            };
        }
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    private SSObject whileTrue(final Stack stack, final List<SSObject> args) {

        var result = stack.getNull();
        final var block = args.get(0);

        for (;;) {
            if (execute(stack.pushNewFrame(), emptyList()).equals(stack.getTrue())) {
                result = block.invoke(stack.pushNewFrame(), "execute");
            } else {
                return result;
            }
        }
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    private void initiateLocalVariables(final Stack stack, final List<SSObject> args) {

        for (int index = 0; index < this.argumentNames.size(); ++index) {
            stack.addVariable(this.argumentNames.get(index),
                    args.get(index).evaluate(stack));
        }
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    private SSObject execute(Stack stack, final List<SSObject> args) {

        initiateLocalVariables(stack, args);

        var result = stack.getNull();

        for (final SSObject statement : this.statements) {
            result = statement.evaluate(stack);
        }

        return result;
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    @Override
    public String toString() {

        var result = new StringBuilder('\n').append(this.argumentNames).append("|\n");

        this.statements.forEach(s -> result.append(s).append("\n"));

        return result.toString();
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    private final List<SSObject> statements;
    private final List<String> argumentNames;
}
