package ss.runtime;

import java.util.List;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class SSVariable extends SSObject {

	/****************************************************************************
	 * 
	****************************************************************************/
	public SSVariable(final String variableName) {
		
		this.variableName = variableName;
	}
	/****************************************************************************
	 * 
	****************************************************************************/
	@Override
	public SSObject invoke(final String method, final List<SSObject> args,
			final Stack stack) {

		return stack.getVariable(this.variableName).invoke(method, args, stack);
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	@Override
	public String toString() {

		return "variable";
	}
	/****************************************************************************
	 * 
	****************************************************************************/
	private final String variableName;
}
