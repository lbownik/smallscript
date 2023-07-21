package ss.runtime;

import static java.util.Collections.emptyList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class SSDynamicObject extends SSObject {

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
    @Override
    public SSObject invoke(final String method, final List<SSObject> args,
            final Stack stack) {

        final var block = this.methods.get(method);
        if (block != null) {
            return block.evaluate(stack, args);
        } else {
            if (this.fields.containsKey(method)) {
                return this.fields.get(method);
            } else if (method.endsWith(":") && this.fields
                    .containsKey(method.substring(0, method.length() - 1))) {
                return setField(method.substring(0, method.length() - 1), args.get(0));
            } else {
                return switch (method) {
                case "addMethod:using:" ->
                    addMethod(args.get(0).toString(), (SSBlock) args.get(1));
                case "addField:" -> addField(args.get(0).toString(), stack);
                case "copy" -> copy();
                default -> super.invoke(method, args, stack);
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
    @Override
    public SSObject evaluate(final Stack stack) {

        return invoke("evaluate", emptyList(), stack);
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    @Override
    public SSObject evaluate(Stack stack, final List<SSObject> args) {

        return invoke("evaluate", emptyList(), stack);
    }

    /****************************************************************************
     * 
    ****************************************************************************/
    private final Map<String, SSBlock> methods;
    private final Map<String, SSObject> fields;
}
