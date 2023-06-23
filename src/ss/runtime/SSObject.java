package ss.runtime;

import java.util.List;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class SSObject {

	/****************************************************************************
	 * 
	****************************************************************************/
	public SSObject invoke(String method, List<SSObject> args) {

		return switch (method) {
		case "value" -> this;
		case "size" -> new SSLong(size());
		case "asString" -> new SSString(toString());
		case "hash" -> new SSLong(hashCode());
		case "equals" -> this.equals(args.get(0)) ? SSTrue.instance() : SSFalse.instance();
		default -> throw new IllegalArgumentException(method);
		};
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	@Override
	public String toString() {

		return "object";
	}
	/****************************************************************************
	 * 
	****************************************************************************/
	protected int size() {
		
		return 1;
	}
	/****************************************************************************
	 * 
	****************************************************************************/
}
