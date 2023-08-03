package ss.runtime;

import java.util.List;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com {
 ******************************************************************************/
public final class SSTrue extends SSDynamicObject {

	/****************************************************************************
	 * 
	****************************************************************************/
	public static SSTrue instance() {

		return instance;
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	private SSTrue() {

	}

	/****************************************************************************
	 * 
	****************************************************************************/
	@Override
	public SSObject invoke(final Stack stack, final String method,
			final List<SSObject> args) {

		return switch (method) {
			case "not" -> stack.getFalse();
			case "and:" -> args.get(0).evaluate(stack.pushNewFrame());
			case "or:" -> this;
			case "ifTrue:" -> args.get(0).evaluate(stack.pushNewFrame());
			case "ifFalse:" -> stack.getNull();
			case "ifTrue::ifFalse:" -> args.get(0).evaluate(stack.pushNewFrame());
			default -> super.invoke(stack, method, args);
		};
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	@Override
	public String toString() {

		return name;
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	private final static SSTrue instance = new SSTrue();
	public final static String name = "true";
}
