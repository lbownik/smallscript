package ss.runtime;

import java.util.HashMap;
import java.util.Map;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class Stack {

	public int depth = 0;

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
	public void setVariable(final String name, final SSObject value) {

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

		// System.out.println("push new frame");
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
			this.depth = previousFrame.depth + 1;
		}

		/*************************************************************************
		 * 
		*************************************************************************/
		@Override
		public void addVariable(final String name, final SSObject value) {

			if (this.variables.put(name, value) != null) {
				throw new RuntimeException(
						"Variable '" + name + "' already exists in this scope.");
			}

			// System.out.println("depth=" + depth + "ad variable: " + name + " = " +
			// value.toString() + "stack frame: " + this.variables);
		}

		/*************************************************************************
		 * 
		*************************************************************************/
		@Override
		public void setVariable(final String name, final SSObject value) {

			if (this.variables.put(name, value) == null) {
				throw new RuntimeException("Variable '" + name + "' does not exist.");
			}

			// System.out.println("depth=" + depth + "ad variable: " + name + " = " +
			// value.toString() + "stack frame: " + this.variables);
		}

		/*************************************************************************
		 * 
		*************************************************************************/
		@Override
		public SSObject getVariable(final String name) {

			SSObject value = this.variables.get(name);
			if (value != null) {
				return value;
			} else {
				value = this.previousFrame.getVariable(name);
				if (value != null) {
					return value;
				} else {
					throw new RuntimeException("Variable '" + name + "' does not exist.");
				}
			}
		}

		/*************************************************************************
		 * 
		*************************************************************************/
		private final Map<String, SSObject> variables = new HashMap<>();
		private final Stack previousFrame;
	}
}
