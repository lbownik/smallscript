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
	public SSObject invoke(final String method, final List<SSObject> args) {

		return switch (method) {
		case "+" -> new SSDouble(this.value + evaluateFirst(args));
		case "-" -> new SSDouble(this.value - evaluateFirst(args));
		case "*" -> new SSDouble(this.value * evaluateFirst(args));
		case "/" -> new SSDouble(this.value / evaluateFirst(args));
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
	private static double evaluateFirst(final List<SSObject> args) {

		return ((SSDouble) args.get(0).evaluate()).value;
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
