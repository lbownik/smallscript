package ss.parser;

import java.util.ArrayList;

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

		return SSNull.instance();
	}
}
