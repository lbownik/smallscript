package ss.runtime;

import java.util.List;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com {
 ******************************************************************************/
public final class SSFalse extends SSDynamicObject {

	/****************************************************************************
	 * 
	****************************************************************************/
	public static SSFalse instance() {

		return instance;
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	private SSFalse() {

	}

	/****************************************************************************
	 * 
	****************************************************************************/
	@Override
	public SSObject invoke(final Stack stack, final String method, final List<SSObject> args) {

		return switch (method) {
			case "not" -> stack.getTrue();
			case "and:" -> this;
			case "or:", "xor:" -> args.get(0).evaluate(stack.pushNewFrame());
			case "ifTrue:" -> stack.getNull();
			case "ifFalse:" -> args.get(0).evaluate(stack.pushNewFrame());
			case "ifTrue:ifFalse:" -> args.get(1).evaluate(stack.pushNewFrame());
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
	private final static SSFalse instance = new SSFalse();
	public final static String name = "false";
}
