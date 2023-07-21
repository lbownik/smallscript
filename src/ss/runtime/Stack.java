package ss.runtime;

import java.util.HashMap;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public interface Stack {

    /****************************************************************************
     * 
    ****************************************************************************/
    default public Stack addVariable(final String name, final SSObject value) {

        throw new RuntimeException("Stack not initialized.");
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    default public Stack setVariable(final String name, final SSObject value) {

        throw new RuntimeException("Variable '" + name + "' does not exist.");
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    default public SSObject getVariable(final String name) {

        throw new RuntimeException("Variable '" + name + "' does not exist.");
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    default public SSObject getTrue() {

        return getVariable(SSTrue.name);
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    default public SSObject getFalse() {

        return getVariable(SSFalse.name);
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    default public SSObject getNull() {

        return getVariable(SSNull.name);
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    default public Stack pushNewFrame() {

        return new Frame(this);
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    public static Stack create() {

        final var stack = new Stack() {
        }.pushNewFrame();
        stack.addVariable("null", SSNull.instance());
        stack.addVariable("true", SSTrue.instance());
        stack.addVariable("false", SSFalse.instance());
        stack.addVariable("object", new SSDynamicObject());
        return stack;
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    public static class Frame extends HashMap<String, SSObject> implements Stack {

        /*************************************************************************
         * 
        *************************************************************************/
        private Frame(final Stack previousFrame) {

            this.previousFrame = previousFrame;
        }

        /*************************************************************************
         * 
        *************************************************************************/
        @Override
        public Stack addVariable(final String name, final SSObject value) {

            if (put(name, value) == null) {
                return this;
            } else {
                throw new RuntimeException(
                        "Variable '" + name + "' already exists in this scope.");
            }
        }

        /*************************************************************************
         * 
        *************************************************************************/
        @Override
        public Stack setVariable(final String name, final SSObject value) {

            final var previousValue = replace(name, value);
            return previousValue != null ? this
                    : this.previousFrame.setVariable(name, value);
        }

        /*************************************************************************
         * 
        *************************************************************************/
        @Override
        public SSObject getVariable(final String name) {

            final var value = get(name);
            return value != null ? value : this.previousFrame.getVariable(name);
        }

        /*************************************************************************
         * 
        *************************************************************************/
        private final Stack previousFrame;
    }
}
