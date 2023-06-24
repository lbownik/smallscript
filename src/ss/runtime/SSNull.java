package ss.runtime;

import java.util.List;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com {
 ************************************************************************/
public final class SSNull extends SSObject {

	/****************************************************************************
	 * 
	****************************************************************************/
	public static SSNull instance() {

		return instance;
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	public SSObject invoke(final String method, final List<SSObject> args) {

		return switch (method) {
		case "size" -> new SSLong(0);
		case "asString" -> new SSString(toString());
		case "hash" -> new SSLong(hashCode());
		case "equals" -> this.equals(args.get(0)) ? SSTrue.instance() : SSFalse.instance();
		default -> this;
		};
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	private SSNull() {

	}

	/****************************************************************************
	 * 
	****************************************************************************/
	@Override
	public String toString() {

		return "null";
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	private final static SSNull instance = new SSNull();
}
