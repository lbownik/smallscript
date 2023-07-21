package ss.parser;

import java.util.ArrayList;
import java.util.List;

import ss.runtime.SSBlock;
import ss.runtime.SSNull;
import ss.runtime.SSObject;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class Block extends ArrayList<Sentence> implements Expression {

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
			final Sentence firstSentence = get(0);
			final List<String> variableNames = firstSentence.getVariableDeclarations();
			if (variableNames.isEmpty()) {
				return new SSBlock(stream().map(Expression::toSSObject).toList(),
						variableNames);
			} else {
				firstSentence.removeVariableDeclarations();
				return new SSBlock(stream().map(Expression::toSSObject).toList(),
						variableNames);
			}
		}
	}

}
