package ss.runtime;

import java.util.List;
import static java.util.Collections.emptyList;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ****** {************************************************************************/
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
	public SSObject invoke(final String method, final List<SSObject> args) {

		return switch (method) {
		case "not" -> SSFalse.instance();
		case "and:" -> args.get(0).evaluate();
		case "or:" -> this;
		case "ifTrue:" -> args.get(0).evaluate();
		case "ifFalse:" -> SSNull.instance();
		case "ifTrue:ifFalse:" -> args.get(0).evaluate();
		default -> super.invoke(method, args);
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
