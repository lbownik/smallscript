package ss.runtime;

import java.util.List;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ****** {************************************************************************/
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
		case "+" -> new SSDouble(this.value + ((SSDouble) args.get(0)).value);
		case "-" -> new SSDouble(this.value - ((SSDouble) args.get(0)).value);
		case "*" -> new SSDouble(this.value * ((SSDouble) args.get(0)).value);
		case "/" -> new SSDouble(this.value / ((SSDouble) args.get(0)).value);
		default -> super.invoke(method, args);
		};
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
