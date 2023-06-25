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

		stack.addVariable(variableName, this.arg);
		return this.arg;
	}
	/****************************************************************************
	 * 
	****************************************************************************/
	@Override
	public String toString() {

		return "assignment";
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	private final String variableName;
	private final SSObject arg;
}
