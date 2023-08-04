package ss.runtime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class StackUseCases {

    /****************************************************************************
     * 
     ****************************************************************************/
    @Test
    public void variableOnStack_canBeRetrieved() throws Exception {

        var stack = Stack.create().addVariable("a", new SSTrue());

        assertEquals(new SSTrue(), stack.getVariable("a"));

        stack = stack.pushNewFrame();

        assertEquals(new SSTrue(), stack.getVariable("a"));
    }

    /****************************************************************************
     * 
     ****************************************************************************/
    @Test
    public void variableOnStack_canBeChanged() throws Exception {

        var stack = Stack.create().addVariable("a", new SSTrue());
        stack.setVariable("a", new SSFalse());

        assertEquals(new SSFalse(), stack.getVariable("a"));
        
        var topFrame = stack.pushNewFrame();

        stack = topFrame.setVariable("a", SSNull.instance());

        assertEquals(SSNull.instance(), stack.getVariable("a"));
        assertEquals("{}", topFrame.toString());
    }

    /****************************************************************************
     * 
     ****************************************************************************/
    @Test
    public void addVariable_throwsException_whenVariableAlreadyExistsInTheSameFrame()
            throws Exception {

        var stack = Stack.create().addVariable("a", new SSTrue());

        try {
            stack.addVariable("a", new SSFalse());
            fail("Expected exception");
        } catch (RuntimeException e) {
            assertEquals("Variable 'a' already exists in this scope.", e.getMessage());
        }
    }

    /****************************************************************************
     * 
     ****************************************************************************/
    @Test
    public void addVariable_addsVriableToTopLevelFrame() throws Exception {

        var stack = Stack.create();
        var topLevelFrame = stack.pushNewFrame().addVariable("a", new SSFalse());

        assertEquals(new SSFalse(), topLevelFrame.getVariable("a"));

        try {
            stack.getVariable("a");
            fail("Expected exception");
        } catch (RuntimeException e) {
            assertEquals("Variable 'a' does not exist.", e.getMessage());
        }
    }

    /****************************************************************************
     * 
     ****************************************************************************/
    @Test
    public void addVariable_shadowsVariablesAlreadyExistingOnStack() throws Exception {

        var stack = Stack.create().addVariable("a", new SSTrue());
        var topLevelFrame = stack.pushNewFrame().addVariable("a", new SSFalse());

        assertEquals(new SSTrue(), stack.getVariable("a"));
        assertEquals(new SSFalse(), topLevelFrame.getVariable("a"));
    }

    /****************************************************************************
     * 
     ****************************************************************************/
    @Test
    public void setVariable_throwsException_forEmptyStack() throws Exception {

        try {
            Stack.create().setVariable("a", SSNull.instance());
            fail("Expected exception");
        } catch (RuntimeException e) {
            assertEquals("Variable 'a' does not exist.", e.getMessage());
        }
    }

    /****************************************************************************
     * 
     ****************************************************************************/
    @Test
    public void getVariable_throwsException_forEmptyStack() throws Exception {

        try {
            Stack.create().getVariable("a");
            fail("Expected exception");
        } catch (RuntimeException e) {
            assertEquals("Variable 'a' does not exist.", e.getMessage());
        }
    }
}