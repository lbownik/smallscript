package ss.runtime;

import java.io.IOException;

import ss.parser.Parser;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class Interpreter {

    /****************************************************************************
     * 
    ****************************************************************************/
    public SSObject exacute(final String program, final Stack stack) throws IOException {

        return new Parser().parse(program).toSSObject().evaluate(stack);
    }
    /****************************************************************************
     * 
    ****************************************************************************/
}
