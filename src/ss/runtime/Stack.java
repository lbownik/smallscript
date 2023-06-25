package ss.runtime;

import java.util.HashMap;
import java.util.Map;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class Stack {

	/****************************************************************************
	 * 
	****************************************************************************/
	private Stack() {
		
	}
	/****************************************************************************
	 * 
	****************************************************************************/
	public void addVariable(final String name, final SSObject value) {

		return;
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	public SSObject getVariable(final String name) {
		
		return null;
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	public Stack pushNewFrame() {

		return new Frame(this);
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	public static Stack create() {

		return new Stack().pushNewFrame();
	}

	/****************************************************************************
	 * 
	****************************************************************************/
	private class Frame extends Stack {

		/*************************************************************************
		 * 
		*************************************************************************/
		public Frame(final Stack previousFrame) {

			this.previousFrame = previousFrame;
		}

		/*************************************************************************
		 * 
		*************************************************************************/
		public void addVariable(final String name, final SSObject value) {

			this.variables.put(name, value);
		}

		/*************************************************************************
		 * 
		*************************************************************************/
		public SSObject getVariable(final String name) {

			final SSObject value = this.variables.get(name);
			return value != null ? value : this.previousFrame.getVariable(name);
		}

		/*************************************************************************
		 * 
		*************************************************************************/
		private final Map<String, SSObject> variables = new HashMap<>();
		private final Stack previousFrame;
	}
}
