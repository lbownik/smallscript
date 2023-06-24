package ss.runtime;

import static java.util.Collections.emptyList;
import java.util.List;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class SSObject {

	/****************************************************************************
	 * 
	****************************************************************************/
	public SSObject invoke(final String method, final List<SSObject> args) {

		return switch (method) {
		case "value" -> this;
		case "size" -> new SSLong(1);
		case "asString" -> new SSString(toString());
		case "hash" -> new SSLong(hashCode());
		case "equals" -> this.equals(args.get(0)) ? SSTrue.instance() : SSFalse.instance();
		default -> throw new IllegalArgumentException(method);
		};
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	public final SSObject evaluate() {

		return invoke("value", emptyList());
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
}
