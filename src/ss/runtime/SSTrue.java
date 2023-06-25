package ss.runtime;

import java.util.List;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com {
 ******************************************************************************/
public final class SSTrue extends SSObject {

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
	public SSObject invoke(final String method, final List<SSObject> args,
			final Stack stack) {

		return switch (method) {
			case "not" -> SSFalse.instance();
			case "and:" -> args.get(0).evaluate(stack.pushNewFrame());
			case "or:" -> this;
			case "ifTrue:" -> args.get(0).evaluate(stack.pushNewFrame());
			case "ifFalse:" -> SSNull.instance();
			case "ifTrue:ifFalse:" -> args.get(0).evaluate(stack.pushNewFrame());
			default -> super.invoke(method, args, stack);
		};
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	@Override
	public String toString() {

		return "true";
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	private final static SSTrue instance = new SSTrue();
}
