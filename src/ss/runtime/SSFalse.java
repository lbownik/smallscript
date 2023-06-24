package ss.runtime;

import java.util.List;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ****** {************************************************************************/
public final class SSFalse extends SSObject {

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
	public SSObject invoke(final String method, final List<SSObject> args) {

		return switch (method) {
		case "not" -> SSTrue.instance();
		case "and:" -> this;
		case "or:", "xor:" -> args.get(0).evaluate();
		case "ifTrue:" -> SSNull.instance();
		case "ifFalse:" -> args.get(0).evaluate();
		case "ifTrue:ifFalse:" -> args.get(1).evaluate();
		default -> super.invoke(method, args);
		};
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	@Override
	public String toString() {

		return "false";
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	private final static SSFalse instance = new SSFalse();
}
