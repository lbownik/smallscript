package ss.runtime;

import java.util.List;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class SSExpression extends SSObject {

	/****************************************************************************
	 * 
	****************************************************************************/
	public SSExpression(final SSObject object, final String method, final List<SSObject> args) {

		this.object = object;
		this.method = method;
		this.args = args;
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	public SSObject invoke(final String method, final List<SSObject> args) {

		return switch (method) {
		case "value" -> this.object.invoke(this.method, this.args);
		default -> super.invoke(this.method, this.args);
		};
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	@Override
	public String toString() {

		return "expression";
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	private final SSObject object;
	private final String method;
	private final List<SSObject> args;
}
