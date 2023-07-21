package ss.runtime;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class SSAssignment extends SSObject {

	/****************************************************************************
	 * 
	****************************************************************************/
	public SSAssignment(final String variableName, final SSObject arg) {

		this.variableName = variableName;
		this.arg = arg;
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	public SSObject evaluate(final Stack stack) {

		if (this.variableName.startsWith(":")) {
			stack.addVariable(variableName.substring(1), this.arg);
		} else {
			stack.setVariable(variableName, arg);
		}
		return this.arg;
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	@Override
	public String toString() {

		return this.variableName + " = " + this.arg;
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	private final String variableName;
	private final SSObject arg;
}
