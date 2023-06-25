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
	public SSObject invoke(final String method, final List<SSObject> args,
			final Stack stack) {

		return switch (method) {
			case "value" -> this;
			case "size" -> SSLong.one();
			case "asString" -> new SSString(toString());
			case "hash" -> new SSLong(hashCode());
			case "equals" ->
				this.equals(args.get(0)) ? SSTrue.instance() : SSFalse.instance();
			default -> throw new IllegalArgumentException(method);
		};
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	public SSObject evaluate(final Stack stack) {

		return invoke("value", emptyList(), stack);
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
