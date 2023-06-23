package ss.runtime;

import java.util.List;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ****** {************************************************************************/
public final class SSString extends SSObject {

	/****************************************************************************
	 * 
	****************************************************************************/
	public SSString(final String value) {

		this.value = value;
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	public SSObject invoke(final String method, final List<SSObject> args) {

		return switch (method) {
		case "+" -> new SSString(this.value.concat(((SSString) args.get(0)).value));
		case "size" -> new SSLong(size());
		case "at:" -> new SSChar(this.value.charAt(((SSLong)args.get(0)).value.intValue()));
		default -> super.invoke(method, args);
		};
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	@Override
	public String toString() {

		return this.value;
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
			return this.value.equals(((SSString) o).value);
		} else {
			return false;
		}
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	protected int size() {

		return this.value.length();
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	private final String value;
}
