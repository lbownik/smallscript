package ss.parser;

import java.util.ArrayList;

import ss.runtime.SSBlock;
import ss.runtime.SSNull;
import ss.runtime.SSObject;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public final class Block extends ArrayList<Sentence> implements Expression {

	/****************************************************************************
	 * 
	****************************************************************************/
	@Override
	public String toString() {

		return "Block";
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	public Object value() {

		return isEmpty() ? "null" : get(size() - 1).value();
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	@Override
	public SSObject toSSObject() {

		if (isEmpty()) {
			return SSNull.instance();
		} else {
			return new SSBlock(stream().map(Expression::toSSObject).toList());
		}
	}
}
