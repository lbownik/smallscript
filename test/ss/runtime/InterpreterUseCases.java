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
	public void test1() throws Exception {

		var program = parse("1;");

		assertEquals(new SSLong(1), program.toSSObject().evaluate());

		program = parse("1;2;3;");

		assertEquals(new SSLong(3), program.toSSObject().evaluate());

		program = parse("1 size;");

		assertEquals(new SSLong(1), program.toSSObject().evaluate());
		
		program = parse("1 + 1;");
		SSObject o = program.toSSObject();

		assertEquals(new SSLong(2),o.evaluate());
		
		program = parse("1 + 1; 2 * 2;");
		o = program.toSSObject();
		
		
		assertEquals(new SSLong(4),o.evaluate());
	}

	/****************************************************************************
	 * 
	 ***************************************************************************/
	private Block parse(final String str) throws IOException {

		return new Parser().parse(str);
	}
}
