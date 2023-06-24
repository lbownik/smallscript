package ss.parser;

import static java.util.Arrays.asList;
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
		case 2 -> new SSExpression(get(0).toSSObject(), (String) get(1).value(), emptyList());
		case 3 -> new SSExpression(get(0).toSSObject(), (String) get(1).value(), asList(get(2).toSSObject()));
		default -> throw new RuntimeException("not implemented");
		};
	}
}
