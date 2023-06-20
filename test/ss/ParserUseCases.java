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
package ss;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.junit.Test;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class ParserUseCases {

	/****************************************************************************
	* 
	****************************************************************************/
	@Test
	public void throwsNullPointer_ForNullArgument() throws Exception {

		try {
			new Parser().parse((Reader) null);
			fail("NullPointertException failed.");
		} catch (final NullPointerException e) {
			assertTrue(true);
		}
		try {
			new Parser().parse((String) null);
			fail("NullPointertException failed.");
		} catch (final NullPointerException e) {
			assertTrue(true);
		}
	}

	/****************************************************************************
	* 
	****************************************************************************/
	@Test
	public void returnsEmptySequence_ForEmptyInput() throws Exception {

		assertEmptySequence("");
		assertEmptySequence("   \t\n\r\f\b");
	}

	/****************************************************************************
	* 
	****************************************************************************/
	@Test
	public void returnsEmptySequence_ForComments() throws Exception {

		assertEmptySequence("#dlfkdlfkdlfkdkfl\n#ldkflfk");
		assertEmptySequence("   #abc\n\n#def\n");
	}

	/****************************************************************************
	* 
	****************************************************************************/
	@Test
	public void returnsEmptySequence_ForSemicolon() throws Exception {

		assertEmptySequence(";");
		assertEmptySequence(";;");
		assertEmptySequence(";\n;;\n;");
	}

	/****************************************************************************
	* 
	****************************************************************************/
	@Test
	public void returnsInteger_ForProperInput() throws Exception {

		assertLongEquals(0L, "0;");
		assertLongEquals(0L, "-0;");

		assertLongEquals(0L, "  0  ;");
		assertLongEquals(0L, "  -0  ;");

		assertLongEquals(1L, "1;");
		assertLongEquals(-1L, "-1;");

		assertLongEquals(123478900001L, "123478900001;");
		assertLongEquals(-123478900001L, "-123478900001;");
	}

	/****************************************************************************
	* 
	****************************************************************************/
	@Test
	public void throwsEOF_ForUnfinishedBlock() throws Exception {

		assertEOF("{");
		assertEOF("{ \b\t\r\n\f");
		assertEOF("{\"a\"");
		assertEOF("{\"a\" \b\t\r\n\f");
		assertEOF("{a:");
		assertEOF("{a: \b\t\r\n\f");
		assertEOF("{a: 1");
	}

	/****************************************************************************
	* 
	****************************************************************************/
	@Test
	public void throwsEOF_ForUnfinishedExpressionInBrackets() throws Exception {

		assertEOF("(");
		assertEOF("( \b\t\r\n\f");
		assertEOF("(null:");
		assertEOF("(null \b\t\r\n\f");
		assertEOF("(null 1 \b\t\r\n\f");
	}

	/****************************************************************************
	* 
	****************************************************************************/
	@Test
	public void throwsEOF_ForUnfinishedInteger() throws Exception {

		assertEOF("-");
	}

	/****************************************************************************
	* 
	****************************************************************************/
	@Test
	public void returnsDouble_ForProperInput() throws Exception {

		assertDoubleEquals(1.0, "1.0;");
		assertDoubleEquals(-1.0, "-1.0;");

		assertDoubleEquals(0.0, "0.0;");
		assertDoubleEquals(-0.0, "-0.0;");

		assertDoubleEquals(123243324.43434, "123243324.43434;");
		assertDoubleEquals(-123243324.43434, "-123243324.43434;");
	}

	/****************************************************************************
	* 
	****************************************************************************/
	@Test
	public void throwsEOF_ForUnfinishedDouble() throws Exception {

		assertEOF("1.");
	}

	/****************************************************************************
	* 
	****************************************************************************/
	@Test
	public void throwsUnexpected_ForUnexpectedCharacterInDouble() throws Exception {

		//assertUnexpected(".", '.');
		assertUnexpected("-.]", '.');
		assertUnexpected("1.]", ']');
	}

	/****************************************************************************
	* 
	****************************************************************************/
	@Test
	public void returnsString_ForProperInput() throws Exception {

		assertStringEquals("", "\"\";");
		assertStringEquals(" ", "\" \";");
		assertStringEquals("\b", "\"\b\";");
		assertStringEquals("\f", "\"\f\";");
		assertStringEquals("\r", "\"\r\";");
		assertStringEquals("\n", "\"\n\";");
		assertStringEquals("\t", "\"\t\";");

		assertStringEquals("\\", "\"\\\\\";");
		assertStringEquals("\b", "\"\\b\";");
		assertStringEquals("\f", "\"\\f\";");
		assertStringEquals("\r", "\"\\r\";");
		assertStringEquals("\n", "\"\\n\";");
		assertStringEquals("\t", "\"\\t\";");
		assertStringEquals("\b\f\n\r\t", "\"\\b\\f\\n\\r\\t\";");

		assertStringEquals("bfnrt", "\"bfnrt\";");
		assertStringEquals("'", "\"'\";");
		assertStringEquals("\"", "\"\\\"\";");
		assertStringEquals("/", "\"\\/\";");

		assertStringEquals("abcśżą", "\"abcśżą\";");
		assertStringEquals("1234567890", "\"1234567890\";");
		assertStringEquals("!@#$%^&*()_-+=~`.,;:'[]{}|/?", "\"!@#$%^&*()_-+=~`.,;:'[]{}|/?\";");
		assertStringEquals("ą", "\"\\u0105\";");
		assertStringEquals("ą", "\"\\u0105\";");
		assertStringEquals("bąb", "\"b\\u0105b\";");
		assertStringEquals("ą", "\"\\u0105\";");
		assertStringEquals("bśb", "\"b\\u015Bb\";");
		assertStringEquals("bśb", "\"b\\u015bb\";");
		assertStringEquals("http://feedburner.google.com/fb/a/mailverify?uri=JavaCodeGeeks&loc=en_US",
				"\"http://feedburner.google.com/fb/a/mailverify?uri=JavaCodeGeeks&loc=en_US\";");
	}

	/****************************************************************************
	* 
	****************************************************************************/
	@Test
	public void returnsCharacter_ForProperInput() throws Exception {

		assertCharacterEquals(' ', "' ';");
		assertCharacterEquals('\b', "'\b\';");
		assertCharacterEquals('\f', "'\f';");
		assertCharacterEquals('\r', "'\r';");
		assertCharacterEquals('\n', "'\n';");
		assertCharacterEquals('\t', "'\t';");
		assertCharacterEquals('\'', "'\'';");
		assertCharacterEquals('ś', "'ś';");
	}

	/****************************************************************************
	* 
	****************************************************************************/
	@Test
	public void throwsEOF_ForUnfinishedCharacter() throws Exception {

		assertEOF("'");
		assertEOF("' ");
		assertEOF("'\b");
		assertEOF("'ś");
	}

	/****************************************************************************
	* 
	****************************************************************************/
	@Test
	public void returnsSymbol_ForProperInput() throws Exception {

		Sequence s;

		s = parse("bfnrt;");
		assertEquals(1, s.size());
		s = (Sequence) s.get(0);
		assertEquals(1, s.size());
		assertEquals("bfnrt", ((Symbol) s.get(0)).value());

		s = parse("abcśżą123!@#$%&*+=;<>?/:::;");
		assertEquals(2, s.size());
		Sequence s1 = (Sequence) s.get(0);
		assertEquals(1, s1.size());
		assertEquals("abcśżą123!@#$%&*+=", ((Symbol) s1.get(0)).value());
		Sequence s2 = (Sequence) s.get(1);
		assertEquals(1, s2.size());
		assertEquals("<>?/:::", ((Symbol) s2.get(0)).value());

		s = parse("abcśżą123!@#$%&*+= <>?/:::;");
		assertEquals(1, s.size());
		s = (Sequence) s.get(0);
		assertEquals(2, s.size());
		assertEquals("abcśżą123!@#$%&*+=", ((Symbol) s.get(0)).value());
		assertEquals("<>?/:::", ((Symbol) s.get(1)).value());
	}

	/****************************************************************************
	* 
	****************************************************************************/
	@Test
	public void returnsSequence_ForExpressionInBrackets() throws Exception {

		Sequence s;

		s = parse("();");
		assertEquals(1, s.size());
		s = (Sequence) s.get(0);
		assertEquals(0, ((Sequence) s.get(0)).size());

		s = parse("(set add: \"abc\" or: 1);");
		assertEquals(1, s.size());
		s = (Sequence) s.get(0);
		assertEquals(1, s.size());
		s = (Sequence) s.get(0);
		assertEquals(5, s.size());
		assertTrue(s.get(0) instanceof Symbol);
		assertEquals("set", s.get(0).value());
		assertTrue(s.get(1) instanceof Symbol);
		assertEquals("add:", s.get(1).value());
		assertTrue(s.get(2) instanceof StringConstant);
		assertEquals("abc", s.get(2).value());
		assertTrue(s.get(3) instanceof Symbol);
		assertEquals("or:", s.get(3).value());
		assertTrue(s.get(4) instanceof LongConstant);
		assertEquals(Long.valueOf(1), s.get(4).value());

		s = parse("set add: (2 + 3 round) or: 1;#comment");
		assertEquals(1, s.size());
		s = (Sequence) s.get(0);
		assertEquals(5, s.size());
		assertTrue(s.get(0) instanceof Symbol);
		assertEquals("set", s.get(0).value());
		assertTrue(s.get(1) instanceof Symbol);
		assertEquals("add:", s.get(1).value());
		assertTrue(s.get(2) instanceof Sequence);
		assertTrue(s.get(3) instanceof Symbol);
		assertEquals("or:", s.get(3).value());
		assertTrue(s.get(4) instanceof LongConstant);
		assertEquals(Long.valueOf(1), s.get(4).value());

		s = (Sequence) s.get(2);
		assertEquals(4, s.size());
		assertTrue(s.get(0) instanceof LongConstant);
		assertEquals(Long.valueOf(2), s.get(0).value());
		assertTrue(s.get(1) instanceof Symbol);
		assertEquals("+", s.get(1).value());
		assertTrue(s.get(2) instanceof LongConstant);
		assertEquals(Long.valueOf(3), s.get(2).value());
		assertTrue(s.get(3) instanceof Symbol);
		assertEquals("round", s.get(3).value());

		s = parse("set (2)or: 1;");
		assertEquals(1, s.size());
		s = (Sequence) s.get(0);
		assertEquals(4, s.size());
		assertTrue(s.get(0) instanceof Symbol);
		assertEquals("set", s.get(0).value());
		assertTrue(s.get(1) instanceof Sequence);
		assertTrue(s.get(2) instanceof Symbol);
		assertEquals("or:", s.get(2).value());
		assertTrue(s.get(3) instanceof LongConstant);
		assertEquals(Long.valueOf(1), s.get(3).value());
		
		s = (Sequence) s.get(1);
		assertEquals(1, s.size());
		assertTrue(s.get(0) instanceof LongConstant);
		assertEquals(Long.valueOf(2), s.get(0).value());
		
	}

	/****************************************************************************
	* 
	****************************************************************************/
	@Test
	public void returnsBlock_ForExpressionInBraces() throws Exception {

		Sequence s;
		Block b;

		s = parse("{};");
		assertEquals(1, s.size());
		s = (Sequence) s.get(0);
		assertEquals(1, s.size());
		assertEquals(0, ((Block) s.get(0)).size());

		s = parse("{set add: \"abc\" or: 1;};");
		assertEquals(1, s.size());
		s = (Sequence) s.get(0);
		assertEquals(1, s.size());
		b = (Block)s.get(0);
		assertEquals(1, b.size());
		s = (Sequence) b.get(0);
		assertEquals(5, s.size());
		assertTrue(s.get(0) instanceof Symbol);
		assertEquals("set", s.get(0).value());
		assertTrue(s.get(1) instanceof Symbol);
		assertEquals("add:", s.get(1).value());
		assertTrue(s.get(2) instanceof StringConstant);
		assertEquals("abc", s.get(2).value());
		assertTrue(s.get(3) instanceof Symbol);
		assertEquals("or:", s.get(3).value());
		assertTrue(s.get(4) instanceof LongConstant);
		assertEquals(Long.valueOf(1), s.get(4).value());

		s = parse("set add: {:a | a round;} or: 1;");
		
		assertEquals(5, s.size());
		assertTrue(s.get(0) instanceof Symbol);
		assertEquals("set", s.get(0).value());
		assertTrue(s.get(1) instanceof Symbol);
		assertEquals("add:", s.get(1).value());
		assertTrue(s.get(2) instanceof Block);
		b = (Block)s.get(2);
		assertEquals(1, b.size());
		
		
		assertTrue(s.get(3) instanceof Symbol);
		assertEquals("or:", s.get(3).value());
		assertTrue(s.get(4) instanceof LongConstant);
		assertEquals(Long.valueOf(1), s.get(4).value());

		s = (Sequence) s.get(2);
		assertEquals(4, s.size());
		assertTrue(s.get(0) instanceof Symbol);
		assertEquals(":a", s.get(0).value());
		assertTrue(s.get(1) instanceof VariableBlockSeparator);
		assertTrue(s.get(2) instanceof Symbol);
		assertEquals("a", s.get(2).value());
		assertTrue(s.get(3) instanceof Symbol);
		assertEquals("round", s.get(3).value());

		s = parse("set {2;}or: 1;");
		assertEquals(4, s.size());
		assertTrue(s.get(0) instanceof Symbol);
		assertEquals("set", s.get(0).value());
		assertTrue(s.get(1) instanceof Block);
		assertTrue(s.get(2) instanceof Symbol);
		assertEquals("or:", s.get(2).value());
		assertTrue(s.get(3) instanceof LongConstant);
		assertEquals(Long.valueOf(1), s.get(3).value());
	}

	/****************************************************************************
	* 
	****************************************************************************/
	@Test
	public void throwsEOF_ForUnfinishedString() throws Exception {

		assertEOF("\"");
		assertEOF("\" ");
		assertEOF("\" ");
		assertEOF("\"\b");
		assertEOF("\"\f");
		assertEOF("\"\r");
		assertEOF("\"\n");
		assertEOF("\"\t");
		assertEOF("\"\\\\]");
		assertEOF("\"\\b");
		assertEOF("\"\\f");
		assertEOF("\"\\r");
		assertEOF("\"\\n");
		assertEOF("\"\\t");
		assertEOF("\"\\u");

		assertEOF("\"\\");
		assertEOF("\"\\\\");
		assertEOF("\"\\/");
		assertEOF("\"\\\"");

		assertEOF("\"\\u0");
		assertEOF("\"\\u00");
		assertEOF("\"\\u000");
		assertEOF("\"\\u0000");

		assertEOF("\"b");
		assertEOF("\"ś");
		assertEOF("\"!");
		assertEOF("\"'");
	}

	/****************************************************************************
	* 
	****************************************************************************/
	@Test
	public void throwsUnexpected_ForUnexpectedCharacterInString() throws Exception {

		assertUnexpected("\"\\x", 'x');
		assertUnexpected("\"\\:", ':');
		assertUnexpected("\"\\ ", ' ');
		assertUnexpected("\"\\ś", 'ś');

		assertUnexpected("\"\\ux", 'x');
		assertUnexpected("\"\\u0x", 'x');
		assertUnexpected("\"\\u00x", 'x');
		assertUnexpected("\"\\u000x", 'x');
	}

	/****************************************************************************
	* 
	****************************************************************************/
	@Test
	public void returnsParseTree_forProperProgram() throws Exception {

		Sequence s = parse("# comment\n" + ":MyClass = Object subClass: \"MyClass\";\n" + "\n"
				+ "MyClass addField \"value\"; #another comment\n"
				+ "MYClass addMethod: \"method1\" using: {:self :param1 |\n"
				+ "     ^ param1 > 0 ifTrue: 0 ifFalse: 1 ;\n" + "};\n" + "\n" + "stdout print (MyClass method1 3);");

		assertEquals(4, s.size());
	}

	/****************************************************************************
	* 
	****************************************************************************/

	private void assertUnexpected(final String str, final char unexpectedChar) throws IOException {

		try {
			Object result = parse(str);
			fail("Unexpected character failed. Result: ".concat(result.toString()));
		} catch (final Parser.UnexpectedCharacterException e) {
			assertEquals(unexpectedChar, e.character);
		}
	}

	/****************************************************************************
	 * 
	 ***************************************************************************/
	private void assertLongEquals(final long expected, final String str) throws Exception {

		Sequence s = parse(str);
		assertEquals(1, s.size());
		
		s = (Sequence) s.get(0);
		assertEquals(1, s.size());
		assertEquals(Long.valueOf(expected), s.get(0).value());
	}

	/****************************************************************************
	 * 
	 ***************************************************************************/
	private void assertDoubleEquals(final double expected, final String str) throws Exception {

		Sequence s = parse(str);
		assertEquals(1, s.size());
		
		s = (Sequence) s.get(0);
		assertEquals(1, s.size());
		assertEquals(Double.valueOf(expected), s.get(0).value());
	}

	/****************************************************************************
	 * 
	 ***************************************************************************/
	private void assertStringEquals(final String expected, final String str) throws Exception {

		Sequence s = parse(str);
		assertEquals(1, s.size());
		
		s = (Sequence) s.get(0);
		assertEquals(1, s.size());
		assertEquals(expected, s.get(0).value());
	}

	/****************************************************************************
	 * 
	 ***************************************************************************/
	private void assertCharacterEquals(final Character expected, final String str) throws Exception {

		Sequence s = parse(str);
		assertEquals(1, s.size());
		s = (Sequence) s.get(0);
		assertEquals(1, s.size());
		assertEquals(expected, ((CharacterConstant) s.get(0)).value());
	}

	/****************************************************************************
	 * 
	 ***************************************************************************/
	private void assertEmptySequence(final String str) throws Exception {

		assertTrue(((Sequence) parse(str)).isEmpty());
	}

	/****************************************************************************
	 * 
	 ***************************************************************************/
	private void assertEOF(final String str) throws IOException {

		try {
			Object result = parse(str);
			fail("EOF failed. Result: ".concat(result.toString()));
		} catch (final EOFException e) {
			assertTrue(true);
		}
	}

	/****************************************************************************
	 * 
	 ***************************************************************************/
	private Sequence parse(final String str) throws IOException {

		return new Parser().parse(str);
	}
}