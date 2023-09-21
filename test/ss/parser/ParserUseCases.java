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
package ss.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;

import org.junit.Ignore;
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

      // assertUnexpected(".", '.');
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
      assertStringEquals("!@#$%^&*()_-+=~`.,;:'[]{}|/?",
            "\"!@#$%^&*()_-+=~`.,;:'[]{}|/?\";");
      assertStringEquals("ą", "\"\\u0105\";");
      assertStringEquals("ą", "\"\\u0105\";");
      assertStringEquals("bąb", "\"b\\u0105b\";");
      assertStringEquals("ą", "\"\\u0105\";");
      assertStringEquals("bśb", "\"b\\u015Bb\";");
      assertStringEquals("bśb", "\"b\\u015bb\";");
      assertStringEquals(
            "http://feedburner.google.com/fb/a/mailverify?uri=JavaCodeGeeks&loc=en_US",
            "\"http://feedburner.google.com/fb/a/mailverify?uri=JavaCodeGeeks&loc=en_US\";");
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
   public void returnsSymbol_ForProperInput() throws Exception {

      Block program;
      // -----------------------------------------------------------------------
      program = parse("bfnrt;");

      assertEquals(1, program.size());
      {
         Sentence expresson = (Sentence) program.get(0);

         assertEquals(1, expresson.size());
         assertEquals("bfnrt", ((Symbol) expresson.get(0)).value());
      }
      // -----------------------------------------------------------------------
      program = parse("!bfnrt;");

      assertEquals(1, program.size());
      {
         Sentence expresson = (Sentence) program.get(0);

         assertEquals(1, expresson.size());
         assertEquals("!bfnrt", ((Symbol) expresson.get(0)).value());
      }
      // -----------------------------------------------------------------------
      program = parse("abcśżą123!@#$%&*+=;<>?/:;");

      assertEquals(2, program.size());
      {
         Sentence expression = (Sentence) program.get(0);

         assertEquals(1, expression.size());
         assertEquals("abcśżą123!@#$%&*+=", ((Symbol) expression.get(0)).value());
      }
      {
         Sentence expression = (Sentence) program.get(1);

         assertEquals(1, expression.size());
         assertEquals("<>?/:", ((Symbol) expression.get(0)).value());
      }
      // -----------------------------------------------------------------------
      program = parse(":abcśżą123!@#$%&*+= <>?/:;");

      assertEquals(1, program.size());
      {
         Sentence expression = (Sentence) program.get(0);

         assertEquals(2, expression.size());
         assertEquals(":abcśżą123!@#$%&*+=", ((Symbol) expression.get(0)).value());
         assertEquals("<>?/:", ((Symbol) expression.get(1)).value());
      }
   }
   /****************************************************************************
   * 
   ****************************************************************************/
   @Ignore
   @Test
   public void throwsRuntimeException_forMalformedSymbol() throws Exception {

      assertSyntaxException("!!;", "!!");
      assertSyntaxException("!1;", "!1");
      assertSyntaxException("1a;", "1a");
   }
   /****************************************************************************
   * 
   ****************************************************************************/
   @Test
   public void returnsSequence_ForExpressionInBrackets() throws Exception {

      Block program;
      // -----------------------------------------------------------------------
      program = parse("();");

      assertEquals(1, program.size());
      {
         Sentence expression = (Sentence) program.get(0);

         assertEquals(0, ((Sentence) expression.get(0)).size());
      }
      // -----------------------------------------------------------------------
      program = parse("(set add: \"abc\" or: 1);");

      assertEquals(1, program.size());
      {
         Sentence expression = (Sentence) program.get(0);

         assertEquals(1, expression.size());
         {
            Sentence bracketExpr = (Sentence) expression.get(0);

            assertEquals(5, bracketExpr.size());
            assertEquals("set", bracketExpr.get(0).value());
            assertEquals("add:", bracketExpr.get(1).value());
            assertEquals("abc", bracketExpr.get(2).value());
            assertEquals("or:", bracketExpr.get(3).value());
            assertEquals(Long.valueOf(1), bracketExpr.get(4).value());
         }
      }
      // -----------------------------------------------------------------------
      program = parse("set add: (2 + 3 round) or: 1;#comment");

      assertEquals(1, program.size());
      {
         Sentence expression = (Sentence) program.get(0);

         assertEquals(5, expression.size());
         assertEquals("set", expression.get(0).value());
         assertEquals("add:", expression.get(1).value());
         assertEquals("or:", expression.get(3).value());
         assertEquals(Long.valueOf(1), expression.get(4).value());
         {
            Sentence bracketExp = (Sentence) expression.get(2);

            assertEquals(4, bracketExp.size());
            assertEquals(Long.valueOf(2), bracketExp.get(0).value());
            assertEquals("+", bracketExp.get(1).value());
            assertEquals(Long.valueOf(3), bracketExp.get(2).value());
            assertEquals("round", bracketExp.get(3).value());
         }
      }
      // -----------------------------------------------------------------------
      program = parse("set (2)or: 1;");

      assertEquals(1, program.size());
      {
         Sentence expression = (Sentence) program.get(0);

         assertEquals(4, expression.size());
         assertEquals("set", expression.get(0).value());
         {
            Sentence bracketExp = (Sentence) expression.get(1);

            assertEquals(1, bracketExp.size());
            assertEquals(Long.valueOf(2), bracketExp.get(0).value());
         }
         assertEquals("or:", expression.get(2).value());
         assertEquals(Long.valueOf(1), expression.get(3).value());
      }
   }
   /****************************************************************************
   * 
   ****************************************************************************/
   @Test
   public void returnsBlock_ForExpressionInBraces() throws Exception {

      Block program;
      // -----------------------------------------------------------------------
      program = parse("{};");

      assertEquals(1, program.size());
      {
         Sentence expression = (Sentence) program.get(0);

         assertEquals(1, expression.size());
         assertEquals(0, ((Block) expression.get(0)).size());
      }
      // -----------------------------------------------------------------------
      program = parse("{true ;};");

      assertEquals(1, program.size());
      {
         Sentence expression = (Sentence) program.get(0);

         assertEquals(1, expression.size());
         {
            Block block = (Block) expression.get(0);

            assertEquals(1, block.size());
            {
               Sentence blkExpression = (Sentence) block.get(0);

               assertEquals(1, blkExpression.size());
               assertEquals("true", blkExpression.get(0).value());
            }
         }
      }
   // -----------------------------------------------------------------------
      program = parse("{true};");

      assertEquals(1, program.size());
      {
         Sentence expression = (Sentence) program.get(0);

         assertEquals(1, expression.size());
         {
            Block block = (Block) expression.get(0);

            assertEquals(1, block.size());
            {
               Sentence blkExpression = (Sentence) block.get(0);

               assertEquals(1, blkExpression.size());
               assertEquals("true", blkExpression.get(0).value());
            }
         }
      }
      program = parse("{true   };");

      assertEquals(1, program.size());
      {
         Sentence expression = (Sentence) program.get(0);

         assertEquals(1, expression.size());
         {
            Block block = (Block) expression.get(0);

            assertEquals(1, block.size());
            {
               Sentence blkExpression = (Sentence) block.get(0);

               assertEquals(1, blkExpression.size());
               assertEquals("true", blkExpression.get(0).value());
            }
         }
      }
      // -----------------------------------------------------------------------
      program = parse("{set add: \"abc\" or: 1;};");

      assertEquals(1, program.size());
      {
         Sentence expression = (Sentence) program.get(0);

         assertEquals(1, expression.size());
         {
            Block block = (Block) expression.get(0);

            assertEquals(1, block.size());
            {
               Sentence blkExpression = (Sentence) block.get(0);

               assertEquals(5, blkExpression.size());
               assertEquals("set", blkExpression.get(0).value());
               assertEquals("add:", blkExpression.get(1).value());
               assertEquals("abc", blkExpression.get(2).value());
               assertEquals("or:", blkExpression.get(3).value());
               assertEquals(Long.valueOf(1), blkExpression.get(4).value());
            }
         }
      }
      // -----------------------------------------------------------------------
      program = parse("set add: {:a | a round } or: 1;");

      assertEquals(1, program.size());
      {
         Sentence expression = (Sentence) program.get(0);
         assertEquals(5, expression.size());
         assertEquals("set", expression.get(0).value());
         assertEquals("add:", expression.get(1).value());
         {
            Block block = (Block) expression.get(2);

            assertEquals(1, block.size());
            {
               Sentence blkExpression = (Sentence) block.get(0);

               assertEquals(4, blkExpression.size());
               assertEquals(":a", blkExpression.get(0).value());
               assertEquals("|", blkExpression.get(1).value());
               assertEquals("a", blkExpression.get(2).value());
               assertEquals("round", blkExpression.get(3).value());
            }
         }
         assertEquals("or:", expression.get(3).value());
         assertEquals(Long.valueOf(1), expression.get(4).value());

      }
      // -----------------------------------------------------------------------
      program = parse("set {2;}or: 1;");

      assertEquals(1, program.size());
      {
         Sentence expression = (Sentence) program.get(0);
         assertEquals(4, expression.size());
         assertEquals("set", expression.get(0).value());
         {
            Block block = (Block) expression.get(1);

            assertEquals(1, block.size());
            {
               Sentence blkSequence = (Sentence) block.get(0);

               assertEquals(1, blkSequence.size());
               assertEquals(Long.valueOf(2), blkSequence.get(0).value());
            }
         }
         assertEquals("or:", expression.get(2).value());
         assertEquals(Long.valueOf(1), expression.get(3).value());
      }
   }
   /****************************************************************************
   * 
   ****************************************************************************/
   @Test
   public void returnsParseTree_forProperProgram() throws Exception {

      Block program = parse("""
            # comment
            !MyClass = Object subClass: "MyClass";

            MyClass addField "value"; #another comment
            MyClass addMethod: "method1" using: {:this :param1 |
            	param1 > 0 ifTrue: 0 ifFalse: 1 ;
            };

            stdout print (MyClass new method1 3);
            """);

      assertEquals(4, program.size());
      {
         Sentence expression = (Sentence) program.get(0);

         assertEquals(5, expression.size());
         assertEquals("!MyClass", expression.get(0).value());
         assertEquals("=", expression.get(1).value());
         assertEquals("Object", expression.get(2).value());
         assertEquals("subClass:", expression.get(3).value());
         assertEquals("MyClass", expression.get(4).value());
      }
      {
         Sentence expression = (Sentence) program.get(1);

         assertEquals(3, expression.size());
         assertEquals("MyClass", expression.get(0).value());
         assertEquals("addField", expression.get(1).value());
         assertEquals("value", expression.get(2).value());
      }
      {
         Sentence expression = (Sentence) program.get(2);

         assertEquals(5, expression.size());
         assertEquals("MyClass", expression.get(0).value());
         assertEquals("addMethod:", expression.get(1).value());
         assertEquals("method1", expression.get(2).value());
         assertEquals("using:", expression.get(3).value());
         {
            Block block = (Block) expression.get(4);

            assertEquals(1, block.size());
            {
               Sentence blkExpression = (Sentence) block.get(0);

               assertEquals(10, blkExpression.size());
               assertEquals(":this", blkExpression.get(0).value());
               assertEquals(":param1", blkExpression.get(1).value());
               assertEquals("|", blkExpression.get(2).value());
               assertEquals("param1", blkExpression.get(3).value());
               assertEquals(">", blkExpression.get(4).value());
               assertEquals(Long.valueOf(0), blkExpression.get(5).value());
               assertEquals("ifTrue:", blkExpression.get(6).value());
               assertEquals(Long.valueOf(0), blkExpression.get(7).value());
               assertEquals("ifFalse:", blkExpression.get(8).value());
               assertEquals(Long.valueOf(1), blkExpression.get(9).value());
            }
         }
      }
      {
         Sentence expression = (Sentence) program.get(3);

         assertEquals(3, expression.size());
         assertEquals("stdout", expression.get(0).value());
         assertEquals("print", expression.get(1).value());
         {
            Sentence bracketExp = (Sentence) expression.get(2);

            assertEquals(4, bracketExp.size());
            assertEquals("MyClass", bracketExp.get(0).value());
            assertEquals("new", bracketExp.get(1).value());
            assertEquals("method1", bracketExp.get(2).value());
            assertEquals(Long.valueOf(3), bracketExp.get(3).value());
         }
      }
   }
   /****************************************************************************
   * 
   ****************************************************************************/
   @Test
   public void returnsParseTree_forProperProgramWithComments() throws Exception {

      Block program = parse("""
            true; # comment
            false;
            """);

      assertEquals(2, program.size());
      {
         Sentence expression = (Sentence) program.get(0);

         assertEquals(1, expression.size());
         assertEquals("true", expression.get(0).value());

         expression = (Sentence) program.get(1);

         assertEquals(1, expression.size());
         assertEquals("false", expression.get(0).value());
      }
   }
   /****************************************************************************
   * 
   ****************************************************************************/

   private void assertUnexpected(final String str, final char unexpectedChar)
         throws IOException {

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
   private void assertLongEquals(final long expected, final String str)
         throws Exception {

      Block program = parse(str);

      assertEquals(1, program.size());

      Sentence expression = (Sentence) program.get(0);

      assertEquals(1, expression.size());
      assertEquals(new LongConstant(expected), expression.get(0));
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   private void assertDoubleEquals(final double expected, final String str)
         throws Exception {

      Block program = parse(str);

      assertEquals(1, program.size());

      Sentence expression = (Sentence) program.get(0);

      assertEquals(1, expression.size());
      assertEquals(new DoubleConstant(expected), expression.get(0));
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   private void assertStringEquals(final String expected, final String str)
         throws Exception {

      Block program = parse(str);

      assertEquals(1, program.size());

      Sentence expression = (Sentence) program.get(0);

      assertEquals(1, expression.size());
      assertEquals(new StringConstant(expected), expression.get(0));
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   private void assertEmptySequence(final String str) throws Exception {

      assertTrue(((Block) parse(str)).isEmpty());
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
   private void assertSyntaxException(final String str, final String symbol)
         throws IOException {

      try {
         Object result = parse(str);
         fail("EOF failed. Result: ".concat(result.toString()));
      } catch (final SyntaxException e) {
         assertEquals(symbol, e.symbol);
      }
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
}