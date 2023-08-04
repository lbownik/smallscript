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

        switch (size()) {
        case 1:
            return get(0).toSSObject();
        case 2:
            if (isAssignment()) {
                throw new RuntimeException("Syntax error. Missing assignment value.");
            } else {
                return new SSExpression(get(0).toSSObject(), (String) get(1).value());
            }
        default:
            if (isAssignment()) {
                return new SSAssignment(get(0).value().toString(),
                        subSentence(2).toSSObject());
            } else {
                return createExpression(get(0).toSSObject(), 1);
            }
        }
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    private boolean isAssignment() {

        return get(0) instanceof Symbol s0 && s0.isVariable()
                && get(1) instanceof Symbol s1 && s1.isAssignment();
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    private Sentence subSentence(final int from) {

        final var sentence = new Sentence();
        stream().skip(from).forEach(sentence::add);

        return sentence;
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    private SSObject createExpression(final SSObject subject, int index) {

        if (index == size()) {
            return subject;
        } else if (get(index) instanceof Symbol s) {
            if (s.isMethodWithNoArgs()) {
                return createExpression(new SSExpression(subject, s.toString()),
                        index + 1);
            } else if (s.isMethodWithArgs()) {
                final StringBuilder methodName = new StringBuilder(s.value());
                final ArrayList<SSObject> args = new ArrayList<>();
                args.add(get(++index).toSSObject());
                while (++index < size()) {
                    if (get(index) instanceof Symbol ns) {
                        if (ns.isMethodContinuation()) {
                            methodName.append(ns.value());
                            if (ns.isMethodWithArgs()) {
                                if (++index < size()) {
                                    args.add(get(index).toSSObject());
                                } else {
                                    throw new RuntimeException(
                                            "Syntax error: unfinished expression.");
                                }
                            }
                        } else {
                            break;
                        }
                    } else {
                        throw new RuntimeException(
                                "Syntax error: " + subject + "[" + get(index) + "]");
                    }
                }
                return createExpression(
                        new SSExpression(subject, methodName.toString(), args), index);

            } else {
                return null;
            }
        } else {
            throw new RuntimeException(
                    "Syntax error: " + subject + "[" + get(index) + "]");
        }
    }

    /****************************************************************************
     * 
    ****************************************************************************/
}
