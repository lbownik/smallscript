package ss.runtime;

import java.util.List;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com {
 ************************************************************************/
public final class SSDouble extends SSObject {

	/****************************************************************************
	 * 
	****************************************************************************/
	public SSDouble(final Double value) {

		this.value = value;
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	public SSDouble(final double value) {

		this.value = Double.valueOf(value);
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	@Override
	public SSObject invoke(final String method, final List<SSObject> args,
			final Stack stack) {

		return switch (method) {
			case "+" -> new SSDouble(this.value + evaluateFirst(args, stack));
			case "-" -> new SSDouble(this.value - evaluateFirst(args, stack));
			case "*" -> new SSDouble(this.value * evaluateFirst(args, stack));
			case "/" -> new SSDouble(this.value / evaluateFirst(args, stack));
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
	private static double evaluateFirst(final List<SSObject> args,
			final Stack stack) {

		return ((SSDouble) args.get(0).evaluate(stack.pushNewFrame())).value;
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
			return this.value.equals(((SSDouble) o).value);
		} else {
			return false;
		}
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	public final Double value;
}
