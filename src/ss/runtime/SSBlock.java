package ss.runtime;

import java.util.List;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class SSBlock extends SSObject {

	/****************************************************************************
	 * 
	****************************************************************************/
	public SSBlock(final List<SSObject> statements) {

		this.statements = statements;
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	@Override
	public SSObject invoke(final String method, final List<SSObject> args,
			final Stack stack) {

		return switch (method) {
			case "value" -> evaluateThis(stack);
			default -> super.invoke(method, args, stack);
		};
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	private SSObject evaluateThis(final Stack stack) {

		SSObject result = SSNull.instance();

		for (final SSObject statement : this.statements) {
			result = statement.evaluate(stack.pushNewFrame());
		}

		return result;
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	@Override
	public String toString() {

		return "block";
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	protected int size() {

		return this.statements.size();
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	private final List<SSObject> statements;
}
