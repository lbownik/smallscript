//------------------------------------------------------------------------------
//Copyright 2014 Lukasz Bownik
//
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
//------------------------------------------------------------------------------
package ss.runtime;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import ss.parser.Block;
import ss.parser.Parser;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class InterpreterUseCases {

	/****************************************************************************
	* 
	****************************************************************************/
	@Test
	public void returnsSSLong_forProperExpression() throws Exception {

		assertResultEquals(new SSLong(1), "1;");
		assertResultEquals(new SSLong(3), "1;2;3;");
		assertResultEquals(new SSLong(1), "1 size;");
		assertResultEquals(new SSLong(2), "1 + 1;");
		assertResultEquals(new SSLong(4), "1 + 1; 2 * 2;");
		assertResultEquals(new SSLong(4), "(2 * 1) + 2;");
		assertResultEquals(new SSLong(6), "2 * (1 + 2);");
		assertResultEquals(new SSLong(4), "{2 * 1;} + 2;");
		assertResultEquals(new SSLong(6), "2 * {1 + 2;};");
		assertResultEquals(new SSLong(2), "(2 > 1) ifTrue: 2;");
		assertResultEquals(SSNull.instance(), "(2 > 1) ifFalse: 2;");
		assertResultEquals(new SSLong(2), "(2 > 1) ifTrue: 2 ifFalse: 3;");
		assertResultEquals(new SSLong(7), "((2 > 1) ifTrue: 2 ifFalse: 3) + 5;");
		assertResultEquals(new SSLong(8), "(2 < 1) ifTrue: 2 ifFalse: (3 + 5);");
		assertResultEquals(new SSLong(3), "(2 < 1) ifTrue: 2 ifFalse: 3 value;");
		assertResultEquals(new SSLong(3), "(2 < 1) ifTrue: {2;} ifFalse: {3;} value;");
		assertResultEquals(new SSLong(18), "((2 * 2) + 2) * 3;");
	}

	/****************************************************************************
	 * 
	 ***************************************************************************/
	private Block parse(final String str) throws IOException {

		return new Parser().parse(str);
	}

	/****************************************************************************
	 * 
	 ***************************************************************************/
	private void assertResultEquals(final SSObject o, final String program)
			throws IOException {

		assertEquals(o, parse(program).toSSObject().evaluate(Stack.create()));
	}
}
