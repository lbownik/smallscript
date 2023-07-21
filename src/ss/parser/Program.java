package ss.parser;

import static java.util.Collections.emptyList;

import ss.runtime.SSObject;
import ss.runtime.SSProgram;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public final class Program extends Block {


    /****************************************************************************
     * 
    ****************************************************************************/
    @Override
    public SSObject toSSObject() {

        return new SSProgram(stream().map(Expression::toSSObject).toList(), emptyList());
    }
    /****************************************************************************
     * 
    ****************************************************************************/

}
