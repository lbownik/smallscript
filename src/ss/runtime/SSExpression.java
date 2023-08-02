package ss.runtime;

import java.util.List;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class SSExpression implements SSObject {

	/****************************************************************************
	 * 
	****************************************************************************/
	public SSExpression(final SSObject object, final String method,
			final List<SSObject> args) {

		this.object = object;
		this.method = method;
		this.args = args;
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	@Override
	public SSObject evaluate(final Stack stack) {

		return this.object.invoke(stack, this.method, this.args);
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	@Override
	public String toString() {

		return this.object + " " + this.method + " " + this.args;
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	private final SSObject object;
	private final String method;
	private final List<SSObject> args;
}
