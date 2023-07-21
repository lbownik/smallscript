package ss.runtime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
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

        var stack = Stack.create().addVariable("a", SSTrue.instance());

        assertSame(SSTrue.instance(), stack.getVariable("a"));

        stack = stack.pushNewFrame();

        assertSame(SSTrue.instance(), stack.getVariable("a"));
    }

    /****************************************************************************
     * 
     ****************************************************************************/
    @Test
    public void variableOnStack_canBeChanged() throws Exception {

        var stack = Stack.create().addVariable("a", SSTrue.instance());
        stack.setVariable("a", SSFalse.instance());

        assertSame(SSFalse.instance(), stack.getVariable("a"));
        
        var topFrame = stack.pushNewFrame();

        stack = topFrame.setVariable("a", SSNull.instance());

        assertSame(SSNull.instance(), stack.getVariable("a"));
    }

    /****************************************************************************
     * 
     ****************************************************************************/
    @Test
    public void addVariable_throwsException_whenVariableAlreadyExistsInTheSameFrame()
            throws Exception {

        var stack = Stack.create().addVariable("a", SSTrue.instance());

        try {
            stack.addVariable("a", SSFalse.instance());
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
        var topLevelFrame = stack.pushNewFrame().addVariable("a", SSFalse.instance());

        assertSame(SSFalse.instance(), topLevelFrame.getVariable("a"));

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

        var stack = Stack.create().addVariable("a", SSTrue.instance());
        var topLevelFrame = stack.pushNewFrame().addVariable("a", SSFalse.instance());

        assertSame(SSTrue.instance(), stack.getVariable("a"));
        assertSame(SSFalse.instance(), topLevelFrame.getVariable("a"));
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