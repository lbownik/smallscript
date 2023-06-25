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
public class Sentence implements Expression {

	/****************************************************************************
	 * 
	****************************************************************************/
	public Sentence(final List<Expression> expressions) {

		this.expressions = expressions;
	}

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
	public int size() {

		return this.expressions.size();
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	public Expression get(final int index) {

		return this.expressions.get(index);
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	public SSObject toSSObject() {

		return switch (this.expressions.size()) {
			case 1 -> this.expressions.get(0).toSSObject();
			case 2 -> createUnaryExpression();
			default -> createNAryExpressionOrAssignment();
		};
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	private SSExpression createUnaryExpression() {

		return new SSExpression(this.expressions.get(0).toSSObject(),
				(String) this.expressions.get(1).value(), emptyList());
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	private SSObject createNAryExpressionOrAssignment() {

		if (this.expressions.get(0) instanceof Symbol s0 && s0.isVariable()&& 
				this.expressions.get(1) instanceof Symbol s1 && s1.isAssignment()) {
			return new SSAssignment(s0.value(), this.expressions.get(2).toSSObject());
		} else {
			return createNAryExpression();
		}
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	private SSExpression createNAryExpression() {

		final SSObject subject = this.expressions.get(0).toSSObject();
		final StringBuilder methodName = new StringBuilder();
		final ArrayList<SSObject> arguments = new ArrayList<>();
		for (int index = 1; index < this.expressions.size(); index += 2) {
			final Expression method = this.expressions.get(index);
			if (method instanceof Symbol s) {
				if (s.isMethodWithArgs()) {
					methodName.append(method);
					arguments.add(this.expressions.get(index + 1).toSSObject());
				} else if (s.isNoArgMethod()) {
					return new SSExpression(subject, methodName.toString(),
							arguments);
				} else {
					new RuntimeException(
							"Syntax error: " + subject + "[" + method + "]");
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
	private final List<Expression> expressions;
}
