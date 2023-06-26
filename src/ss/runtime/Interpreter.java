package ss.runtime;

import java.io.IOException;

import ss.parser.Parser;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class Interpreter {

	public SSObject exacute(final String program) throws IOException {

		final var stack = Stack.create();
		stack.addVariable("null", SSNull.instance());
		stack.addVariable("true", SSTrue.instance());
		stack.addVariable("false", SSFalse.instance());
		
		return new Parser().parse(program).toSSObject().evaluate(stack);
	}
	/****************************************************************************
	 * 
	****************************************************************************/
}
