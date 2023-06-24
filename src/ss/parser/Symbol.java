package ss.parser;

import ss.runtime.SSNull;
import ss.runtime.SSObject;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public final class Symbol implements Expression {

	/****************************************************************************
	 * 
	****************************************************************************/
	public Symbol(final String value) {

		this.value = value;
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	@Override
	public String value() {

		return this.value;
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	public boolean isVariableDeclaration() {

		return this.value.startsWith(":");
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	public boolean isVariable() {

		return isVariableDeclaration() || isNoArgMethod();
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	public boolean isMethod() {

		return !isVariableDeclaration();
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	public boolean isMethodWithArgs() {

		return this.value.endsWith(":") || this.value.equals("+")
				|| this.value.equals("-") || this.value.equals("*")
				|| this.value.equals("/") || this.value.equals("==")
				|| this.value.equals("!=")|| this.value.equals("!=")
				|| this.value.equals(">") || this.value.equals("<")
				|| this.value.equals(">=")|| this.value.equals("<=");
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	public boolean isNoArgMethod() {

		return !isMethodWithArgs() && ! isAssignment();
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	public boolean isAssignment() {

		return this.value.equals("=");
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

		if (o != null && getClass() == o.getClass()) {
			return this.value.equals(((Symbol) o).value);
		} else {
			return false;
		}
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	public SSObject toSSObject() {

		return SSNull.instance();
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	private final String value;
}
