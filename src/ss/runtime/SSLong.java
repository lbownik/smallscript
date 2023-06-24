package ss.runtime;

import java.util.List;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com {
 ************************************************************************/
public final class SSLong extends SSObject {

	/****************************************************************************
	 * 
	****************************************************************************/
	public SSLong(final Long value) {

		this.value = value;
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	public SSLong(final int value) {

		this.value = Long.valueOf(value);
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	public SSObject invoke(final String method, final List<SSObject> args) {

		return switch (method) {
		case "+" -> new SSLong(this.value + evaluateFirst(args));
		case "-" -> new SSLong(this.value - evaluateFirst(args));
		case "*" -> new SSLong(this.value * evaluateFirst(args));
		case "/" -> new SSLong(this.value / evaluateFirst(args));
		case "==" -> toBool(this.value == evaluateFirst(args));
		case "!=" -> toBool(this.value != evaluateFirst(args));
		case ">" -> toBool(this.value > evaluateFirst(args));
		case "<" -> toBool(this.value < evaluateFirst(args));
		case ">=" -> toBool(this.value >= evaluateFirst(args));
		case "<=" -> toBool(this.value <= evaluateFirst(args));
		default -> super.invoke(method, args);
		};
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	private static long evaluateFirst(final List<SSObject> args) {

		return ((SSLong) args.get(0).evaluate()).value;
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	private static SSObject toBool(final boolean condition) {

		return condition ? SSTrue.instance() : SSFalse.instance();
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	@Override
	public String toString() {

		return this.value.toString();
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	@Override
	public int hashCode() {

		return this.value.hashCode();
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	@Override
	public boolean equals(final Object o) {

		if (getClass() == o.getClass()) {
			return this.value.equals(((SSLong) o).value);
		} else {
			return false;
		}
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	public final Long value;
}
