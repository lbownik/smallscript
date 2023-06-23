package ss.parser;

import java.util.ArrayList;
import java.util.Collections;

import ss.runtime.SSExpression;
import ss.runtime.SSNull;
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

		return "Sequence";
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	public SSObject toSSObject() {

		return switch (size()) {
		case 1 -> get(0).toSSObject();
		case 2 -> new SSExpression(get(0).toSSObject(), (String) get(1).value(), Collections.emptyList());
		default -> throw new RuntimeException("not implemented");
		};
	}
}
