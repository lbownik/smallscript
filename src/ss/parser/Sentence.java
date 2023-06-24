package ss.parser;

import static java.util.Collections.emptyList;

import java.util.ArrayList;

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
	@Override
	public String toString() {

		return "Sentence";
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	public SSObject toSSObject() {

		return switch (size()) {
		case 1 -> get(0).toSSObject();
		case 2 -> createUnaryExpression();
		default -> createNAryExpression();
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
	private SSExpression createNAryExpression() {

		final SSObject subject = get(0).toSSObject();
		final StringBuilder methodName = new StringBuilder();
		final ArrayList<SSObject> arguments = new ArrayList<>();
		for(int index = 1; index < size(); index +=2) {
			final Expression method = get(index);
			if(method instanceof Symbol s) {
				if (s.isMethodWithArgs()) {
					methodName.append(method);
					arguments.add(get(index+1).toSSObject());
				} else if (s.isNoArgMethod()) {
					return new SSExpression(subject, methodName.toString(), arguments);
				} else {
					new RuntimeException("Syntax error: " + subject + "[" + method + "]");
				}
			} else {
				throw new RuntimeException("Syntax error: " + subject + "[" + method + "]");
			}
		}
		return new SSExpression(subject, methodName.toString(), arguments);
	}
}
