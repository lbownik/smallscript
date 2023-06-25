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
	public static SSLong zero() {

		return zero;
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	public static SSLong one() {

		return one;
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	@Override
	public SSObject invoke(final String method, final List<SSObject> args,
			final Stack stack) {

		return switch (method) {
			case "+" -> new SSLong(this.value + evaluateFirst(args, stack));
			case "-" -> new SSLong(this.value - evaluateFirst(args, stack));
			case "*" -> new SSLong(this.value * evaluateFirst(args, stack));
			case "/" -> new SSLong(this.value / evaluateFirst(args, stack));
			case "==" -> toBool(this.value == evaluateFirst(args, stack));
			case "!=" -> toBool(this.value != evaluateFirst(args, stack));
			case ">" -> toBool(this.value > evaluateFirst(args, stack));
			case "<" -> toBool(this.value < evaluateFirst(args, stack));
			case ">=" -> toBool(this.value >= evaluateFirst(args, stack));
			case "<=" -> toBool(this.value <= evaluateFirst(args, stack));
			default -> super.invoke(method, args, stack);
		};
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	private static long evaluateFirst(final List<SSObject> args,
			final Stack stack) {

		return ((SSLong) args.get(0).evaluate(stack.pushNewFrame())).value;
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

	private final static SSLong zero = new SSLong(0);
	private final static SSLong one = new SSLong(1);
}
