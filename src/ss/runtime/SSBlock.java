package ss.runtime;

import static java.util.Collections.emptyList;
import java.util.List;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class SSBlock extends SSObject{

	/****************************************************************************
	 * 
	****************************************************************************/
	public SSBlock(final List<SSObject> statements) {

		this.statements = statements;
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	public SSObject invoke(final String method, final List<SSObject> args) {

		return switch (method) {
		case "value" -> evaluateThis();
		default -> super.invoke(method, args);
		};
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	private SSObject evaluateThis() {
		
		SSObject result = SSNull.instance();
		
		for(final var statement : this.statements) {
			result = statement.evaluate();
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
