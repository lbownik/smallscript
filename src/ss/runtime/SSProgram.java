package ss.runtime;

import static java.util.Collections.emptyList;

import java.util.List;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class SSProgram extends SSBlock {

    /****************************************************************************
     * 
    ****************************************************************************/
    public SSProgram(final List<SSObject> statements, final List<String> argumentNames) {

        super(statements, argumentNames);
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    public SSObject evaluate(final Stack stack) {

        return invoke("evaluate", emptyList(), stack);
    }
    /****************************************************************************
     * 
    ****************************************************************************/
}
