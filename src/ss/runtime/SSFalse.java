package ss.runtime;

import static java.util.Collections.emptyList;

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
		case "or:" -> args.get(0) == this ? this : SSTrue.instance();
		case "ifTrue:" -> SSNull.instance();
		case "ifFalse:" -> args.get(0).invoke("value", emptyList());
		case "ifTrue:ifFalse:" -> args.get(1).invoke("value", emptyList());
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
