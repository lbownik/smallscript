package ss.runtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class SSDynamicObject implements SSObject {

    /****************************************************************************
     * 
    ****************************************************************************/
    public SSDynamicObject() {

        this(new HashMap<>(), new HashMap<>());
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    private SSDynamicObject(final Map<String, SSBlock> methods,
            final Map<String, SSObject> fields) {

        this.methods = new HashMap<>(methods);
        this.fields = new HashMap<>(fields);
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    private SSDynamicObject copy() {

        return new SSDynamicObject(this.methods, this.fields);
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    public SSObject invoke(final Stack stack, final String method,
            final List<SSObject> args) {

        final var block = this.methods.get(method);
        if (block != null) {
            final ArrayList<SSObject> nArgs = new ArrayList<>();
            nArgs.add(this);
            nArgs.addAll(args);
            return block.invoke(stack, "execute", nArgs);
        } else {
            if (this.fields.containsKey(method)) {
                return this.fields.get(method);
            } else if (method.endsWith(":") && this.fields
                    .containsKey(method.substring(0, method.length() - 1))) {
                return setField(method.substring(0, method.length() - 1), args.get(0));
            } else {
                return switch (method) {
                case "addMethod::using:" ->
                    addMethod(args.get(0).toString(), (SSBlock) args.get(1));
                case "addField:" -> addField(args.get(0).toString(), stack);
                case "copy" -> copy();
                case "execute" -> evaluate(stack);
                case "asString" -> new SSString(toString());
                case "hash" -> new SSLong(hashCode());
                case "equals:" -> stack.get(this.equals(args.get(0).evaluate(stack)));
                case "isNotEqualTo:" ->
                    stack.get(!this.equals(args.get(0).evaluate(stack)));
                default ->
                    throw new RuntimeException("Method '" + method + "' is not defined.");
                };
            }
        }
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    private SSObject addMethod(final String name, final SSBlock body) {

        this.methods.put(name, body);
        return this;
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    private SSObject addField(final String name, final Stack stack) {

        return setField(name, stack.getNull());
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    private SSObject setField(final String name, final SSObject value) {

        this.fields.put(name, value);
        return this;
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    private final Map<String, SSBlock> methods;
    private final Map<String, SSObject> fields;
}
