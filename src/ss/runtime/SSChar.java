package ss.runtime;

import java.util.List;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com {
 ************************************************************************/
public final class SSChar extends SSObject {

	/****************************************************************************
	 * 
	****************************************************************************/
	public SSChar(final Character value) {

		this.value = value;
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	public SSChar(final char value) {

		this.value = value;
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	public SSObject invoke(final String method, final List<SSObject> args,final Stack stack) {

		return switch (method) {
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
	private static char evaluateFirst(final List<SSObject> args,final Stack stack) {

		return ((SSChar) args.get(0).evaluate(stack.pushNewFrame())).value;
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
			return this.value.equals(((SSChar) o).value);
		} else {
			return false;
		}
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	private final Character value;
}
