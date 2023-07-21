package ss.parser;

import static java.util.Collections.emptyList;

import java.util.ArrayList;
import java.util.List;

import ss.runtime.SSAssignment;
import ss.runtime.SSExpression;
import ss.runtime.SSObject;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class Sentence extends ArrayList<Expression> implements Expression {

    /****************************************************************************
     * 
    ****************************************************************************/
    @Override
    public Sentence value() {

        return this;
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    int findVariableBlockSaperator() {

        for (int index = 0; index < size(); ++index) {
            final Expression e = get(index);
            if (e instanceof Symbol s && s.isVariableBlockSaperator()) {
                return index;
            }
        }
        return -1;
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    List<String> getVariableDeclarations() {

        final int index = findVariableBlockSaperator();
        if (index > -1) {
            return subList(0, index).stream().map(e -> e.toString().substring(1))
                    .toList();
        } else {
            return emptyList();
        }
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    void removeVariableDeclarations() {

        final int index = findVariableBlockSaperator();
        if (index > -1) {
            removeRange(0, index + 1);
        }
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    public SSObject toSSObject() {

        return switch (size()) {
        case 1 -> get(0).toSSObject();
        case 2 -> createUnaryExpression();
        default -> createNAryExpressionOrAssignment();
        };
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    private SSExpression createUnaryExpression() {

        return new SSExpression(get(0).toSSObject(), (String) get(1).value(),
                emptyList());
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    private boolean firstExpressionIsVariable() {

        return get(0) instanceof Symbol s0 && s0.isVariable();
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    private boolean secondExpressionIsAssignment() {

        return get(1) instanceof Symbol s1 && s1.isAssignment();
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    private SSObject createNAryExpressionOrAssignment() {

        if (firstExpressionIsVariable() && secondExpressionIsAssignment()) {
            return new SSAssignment(get(0).value().toString(), get(2).toSSObject());
        } else {
            return createNAryExpression();
        }
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    private SSExpression createNAryExpression() {

        final SSObject subject = get(0).toSSObject();
        final StringBuilder methodName = new StringBuilder();
        final ArrayList<SSObject> arguments = new ArrayList<>();

        for (int index = 1; index < size(); index += 2) {
            final Expression method = get(index);
            if (method instanceof Symbol s) {
                if (s.isMethodWithArgs()) {
                    methodName.append(method);
                    arguments.add(get(index + 1).toSSObject());
                } else if (s.isNoArgMethod()) {
                    return new SSExpression(subject, methodName.toString(), arguments);
                } else {
                    new RuntimeException("Syntax error: " + subject + "[" + method + "]");
                }
            } else {
                throw new RuntimeException(
                        "Syntax error: " + subject + "[" + method + "]");
            }
        }
        return new SSExpression(subject, methodName.toString(), arguments);
    }

    /****************************************************************************
     * 
    ****************************************************************************/
}
