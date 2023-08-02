package ss.runtime;

import java.util.List;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com {
 ******************************************************************************/
public final class SSNull extends SSDynamicObject {

	/****************************************************************************
	 * 
	****************************************************************************/
	public static SSNull instance() {

		return instance;
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	@Override
	public SSObject invoke(final Stack stack, final String method,
			final List<SSObject> args) {

		return switch (method) {
			case "asString" -> new SSString(toString());
			case "hash" -> new SSLong(0);
			case "equals:" ->
				this.equals(args.get(0).evaluate(stack)) ? stack.getTrue()
						: stack.getFalse();
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

		return name;
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	private final static SSNull instance = new SSNull();
	public final static String name = "null";
}
