package ss.runtime;

import java.util.List;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com {
 ******************************************************************************/
public final class SSDynamicObjectFactory extends SSDynamicObject {

    /****************************************************************************
     * 
    ****************************************************************************/
    protected SSObject doClone() {

        return new SSDynamicObjectFactory();
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    @Override
    public SSObject invoke(final Stack stack, final String method,
            final List<SSObject> args) {

        return switch (method) {
        case "new" -> new SSDynamicObject();
        default -> super.invoke(stack, method, args);
        };
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    @Override
    public String toString() {

        return "Object";
    }
    /****************************************************************************
     * 
    ****************************************************************************/
}
