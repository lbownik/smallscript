package ss.runtime;

import java.util.List;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com {
 ******************************************************************************/
public final class SSString extends SSDynamicObject {

    /****************************************************************************
     * 
    ****************************************************************************/
    public SSString(final String value) {

        this.value = value;
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    @Override
    public SSObject invoke(final Stack stack, final String method,
            final List<SSObject> args) {

        return switch (method) {
        case "+" -> new SSString(this.value.concat(((SSString) args.get(0)).value));
        case "size" -> new SSLong(this.value.length());
        case "at:" -> new SSChar(this.value.charAt(((SSLong) args.get(0)).intValue()));
        default -> super.invoke(stack, method, args);
        };
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    @Override
    public String toString() {

        return this.value;
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    @Override
    public int hashCode() {

        return this.value.hashCode();
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    @Override
    public boolean equals(final Object o) {

        return getClass() == o.getClass() && this.value.equals(((SSString) o).value);
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    private final String value;
}
