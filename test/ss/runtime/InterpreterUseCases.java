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
/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class InterpreterUseCases {
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void __test() throws Exception {

      assertResultEquals(new SSString("true"), "true asString;");
      assertResultEquals(new SSLong(3569038), "true asString hash;");
      assertResultEquals(new SSString("3569038"), "true asString hash asString;");
      assertResultEquals(new SSString("3569038"),
            "1 equals: 1 asString hash asString;");
      assertResultEquals(new SSString("3569038"),
            "1 asString equals: \"1\" asString hash asString;");
      assertResultEquals(new SSString("true"),
            "1 asString equals: \"1\" equals: true asString;");
      assertResultEquals(new SSString("true"),
            "1 asString equals: \"1\" ifTrue: true :ifFalse: false asString;");
      assertResultEquals(new SSString("true"),
            "!x = 1 asString equals: \"1\" ifTrue: (1 equals: 1) :ifFalse: {!x = 1; false;} asString; x;");
      assertResultEquals(new SSLong(1),
            "true addMethod: \"a::b\" :using: {!this !param | param;}; true a: 1 :b;");
      assertResultEquals(new SSString("1"),
            "true addMethod: \"a::b\" :using: {!this !param | param;}; true a: 1 :b asString;");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void nullProperValue_forAllOperations() throws Exception {

      assertResultEquals(SSNull.instance(), "null;");
      assertResultEquals(SSNull.instance(), "null evaluate;");
      assertResultEquals(new SSLong(0), "null hash;");
      assertResultEquals(new SSTrue(), "null equals: null;");
      assertResultEquals(new SSFalse(), "null equals: true;");
      assertResultEquals(new SSString("null"), "null asString;");

      assertResultEquals(SSNull.instance(), "null not;");
      assertResultEquals(SSNull.instance(), "null ifTrue: false;");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void trueReturnsProperValue_forAllOperations() throws Exception {

      assertResultEquals(new SSTrue(), "true;");
      assertResultEquals(new SSTrue(), "true execute;");
      assertResultEquals(new SSFalse(), "true not;");
      assertResultEquals(new SSTrue(), "true equals: true;");
      assertResultEquals(new SSFalse(), "true equals: false;");
      assertResultEquals(new SSString("true"), "true asString;");

      assertResultEquals(new SSTrue(), "true and: true;");
      assertResultEquals(new SSFalse(), "true and: false;");
      assertResultEquals(SSNull.instance(), "true and: null;");
      assertResultEquals(new SSLong(1), "true and: 1;");

      assertResultEquals(new SSTrue(), "true or: true;");
      assertResultEquals(new SSTrue(), "true or: false;");
      assertResultEquals(new SSTrue(), "true or: null;");
      assertResultEquals(new SSTrue(), "true or: 1;");

      assertResultEquals(new SSLong(0), "true ifTrue: 0;");
      assertResultEquals(SSNull.instance(), "true ifFalse: 0;");
      assertResultEquals(new SSLong(0), "true ifTrue: 0 :ifFalse: 1;");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void falseReturnsProperValue_forAllOperations() throws Exception {

      assertResultEquals(new SSFalse(), "false;");
      assertResultEquals(new SSFalse(), "false execute;");
      assertResultEquals(new SSTrue(), "false not;");
      assertResultEquals(new SSTrue(), "false equals: false;");
      assertResultEquals(new SSFalse(), "false equals: true;");
      assertResultEquals(new SSString("false"), "false asString;");

      assertResultEquals(new SSFalse(), "false and: false;");
      assertResultEquals(new SSFalse(), "false and: true;");
      assertResultEquals(new SSFalse(), "false and: null;");
      assertResultEquals(new SSFalse(), "false and: 1;");

      assertResultEquals(new SSTrue(), "false or: true;");
      assertResultEquals(new SSFalse(), "false or: false;");
      assertResultEquals(SSNull.instance(), "false or: null;");
      assertResultEquals(new SSLong(1), "false or: 1;");

      assertResultEquals(SSNull.instance(), "false ifTrue: 0;");
      assertResultEquals(new SSLong(0), "false ifFalse: 0;");
      assertResultEquals(new SSLong(1), "false ifTrue: 0 :ifFalse: 1;");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void stringReturnsProperValue_forAllOperations() throws Exception {

      assertResultEquals(new SSString("abc"), "\"abc\";");
      assertResultEquals(new SSLong(96354), "\"abc\" hash;");
      assertResultEquals(new SSTrue(), "\"abc\" equals: \"abc\";");
      assertResultEquals(new SSFalse(), "\"abc\" equals: \"a\";");
      assertResultEquals(new SSFalse(), "\"abc\" equals: null;");
      assertResultEquals(new SSLong(3), "\"abc\" size;");
      assertResultEquals(new SSChar('a'), "\"abc\" at: 0;");
      assertResultEquals(new SSString("abcd"), "\"abc\" concatenate: \"d\";");
   }
   /****************************************************************************
   * 
   ****************************************************************************/
   @Test
   public void returnsSSLong_forProperExpression() throws Exception {

      assertResultEquals(new SSLong(1), "1;");
      assertResultEquals(new SSLong(3), "1;2;3;");
      assertResultEquals(new SSLong(2), "1 plus: 1;");
      assertResultEquals(new SSLong(0), "1 minus: 1;");
      assertResultEquals(new SSLong(4), "1 plus: 1; 2 multipliedBy: 2;");
      assertResultEquals(new SSLong(3), "6 dividedBy: 2;");
      assertResultEquals(new SSLong(4), "(2 multipliedBy: 1) plus: 2;");
      assertResultEquals(new SSLong(6), "2 multipliedBy: (1 plus: 2);");
      assertResultEquals(new SSLong(1), "1 hash;");
      assertResultEquals(new SSLong(4), "({2 multipliedBy: 1;} execute) plus: 2;");
      assertResultEquals(new SSLong(6), "2 multipliedBy: ({1 plus: 2;} execute);");
      assertResultEquals(new SSLong(2), "(2 isGreaterThan: 1) ifTrue: 2;");
      assertResultEquals(SSNull.instance(), "(2 isGreaterThan: 1) ifFalse: 2;");
      assertResultEquals(new SSLong(2),
            "(2 isGreaterThan: 1) ifTrue: 2 :ifFalse: 3;");
      assertResultEquals(new SSLong(7),
            "((2 isGreaterThan: 1) ifTrue: 2 :ifFalse: 3) plus: 5;");
      assertResultEquals(new SSLong(8),
            "(2 isLessThan: 1) ifTrue: 2 :ifFalse: (3 plus: 5);");
      assertResultEquals(new SSLong(3),
            "(2 isLessThan: 1) ifTrue: 2 :ifFalse: 3 execute;");
      assertResultEquals(new SSLong(3),
            "((2 isLessThan: 1) ifTrue: {2;} :ifFalse: {3;}) execute;");
      assertResultEquals(new SSLong(18),
            "((2 multipliedBy: 2) plus: 2) multipliedBy: 3;");
      assertResultEquals(new SSLong(2), "!var = 2;");
      assertResultEquals(new SSLong(2), "true ifTrue: 2;");
      assertResultEquals(new SSLong(3), "!var = 2; var = 3; var;");

      assertResultEquals(new SSTrue(), "1 equals: 1;");
      assertResultEquals(new SSFalse(), "1 equals: 2;");
      assertResultEquals(new SSFalse(), "1 equals: null;");
      assertResultEquals(new SSFalse(), "1 isNotEqualTo: 1;");
      assertResultEquals(new SSTrue(), "1 isNotEqualTo: 2;");
      assertResultEquals(new SSTrue(), "1 isNotEqualTo: null;");
      assertResultEquals(new SSTrue(), "1 isLessOrEqualTo: 1;");
      assertResultEquals(new SSTrue(), "1 isLessOrEqualTo: 2;");
      assertResultEquals(new SSFalse(), "1 isLessOrEqualTo: 0;");
      assertResultEquals(new SSTrue(), "2 isGreaterOrEqualTo: 1;");
      assertResultEquals(new SSTrue(), "2 isGreaterOrEqualTo: 2;");
      assertResultEquals(new SSFalse(), "0 isGreaterOrEqualTo: 2;");
      assertResultEquals(new SSTrue(), "1 isGreaterThan: 0;");
      assertResultEquals(new SSFalse(), "1 isGreaterThan: 1;");
      assertResultEquals(new SSTrue(), "0 isLessThan: 1;");
      assertResultEquals(new SSFalse(), "1 isLessThan: 1;");

      assertResultEquals(new SSString("1"), "1 asString;");
   }
   /****************************************************************************
   * 
   ****************************************************************************/
   @Test
   public void double_forProperExpression() throws Exception {

      assertResultEquals(new SSDouble(1.0), "1.0;");
      assertResultEquals(new SSDouble(3.0), "1.0;2.0;3.0;");
      assertResultEquals(new SSDouble(2.1), "1.0 plus: 1.1;");
      assertResultEquals(new SSLong(0), "1 minus: 1;");
      assertResultEquals(new SSDouble(4.0), "1.0 plus: 1.0; 2.0 multipliedBy: 2.0;");
      assertResultEquals(new SSDouble(3.0), "6.0 dividedBy: 2;");
      assertResultEquals(new SSDouble(3.0), "6 dividedBy: 2.0;");
      assertResultEquals(new SSDouble(4.0), "(2.0 multipliedBy: 1.0) plus: 2.0;");
      assertResultEquals(new SSDouble(6.0), "2.0 multipliedBy: (1.0 plus: 2.0);");
      assertResultEquals(new SSLong(1072693248), "1.0 hash;");
      assertResultEquals(new SSDouble(4.0),
            "({2.0 multipliedBy: 1.0;} execute) plus: 2.0;");
      assertResultEquals(new SSDouble(6.0),
            "2.0 multipliedBy: ({1.0 plus: 2.0;} execute);");
      assertResultEquals(new SSLong(2), "(2.0 isGreaterThan: 1.0) ifTrue: 2;");
      assertResultEquals(SSNull.instance(), "(2.0 isGreaterThan: 1.0) ifFalse: 2;");
      assertResultEquals(new SSLong(2),
            "(2.0 isGreaterThan: 1.0) ifTrue: 2 :ifFalse: 3;");
      assertResultEquals(new SSLong(7),
            "((2.0 isGreaterThan: 1.0) ifTrue: 2 :ifFalse: 3) plus: 5;");
      assertResultEquals(new SSLong(8),
            "(2.0 isLessThan: 1.0) ifTrue: 2 :ifFalse: (3 plus: 5);");
      assertResultEquals(new SSLong(3),
            "(2.0 isLessThan: 1.0) ifTrue: 2 :ifFalse: 3 execute;");
      assertResultEquals(new SSLong(3),
            "2.0 isLessThan: 1.0 ifTrue: {2;} :ifFalse: {3;} execute;");
      assertResultEquals(new SSLong(18),
            "((2 multipliedBy: 2) plus: 2) multipliedBy: 3;");
      assertResultEquals(new SSLong(2), "!var = 2;");
      assertResultEquals(new SSLong(2), "true ifTrue: 2;");
      assertResultEquals(new SSLong(3), "!var = 2; var = 3; var;");

      assertResultEquals(new SSTrue(), "1.0 equals: 1.0;");
      assertResultEquals(new SSFalse(), "1.0 equals: 2.0;");
      assertResultEquals(new SSFalse(), "1.0 equals: null;");
      assertResultEquals(new SSFalse(), "1.0 isNotEqualTo: 1.0;");
      assertResultEquals(new SSTrue(), "1.0 isNotEqualTo: 2.0;");
      assertResultEquals(new SSTrue(), "1.0 isLessOrEqualTo: 1.0;");
      assertResultEquals(new SSTrue(), "1.0 isLessOrEqualTo: 2.0;");
      assertResultEquals(new SSFalse(), "1.0 isLessOrEqualTo: 0.0;");
      assertResultEquals(new SSTrue(), "2.0 isGreaterOrEqualTo: 1.0;");
      assertResultEquals(new SSTrue(), "2.0 isGreaterOrEqualTo: 2.0;");
      assertResultEquals(new SSFalse(), "0.0 isGreaterOrEqualTo: 2.0;");
      assertResultEquals(new SSTrue(), "1.0 isGreaterThan: 0.0;");
      assertResultEquals(new SSFalse(), "1.0 isGreaterThan: 1.0;");
      assertResultEquals(new SSTrue(), "0.0 isLessThan: 1.0;");
      assertResultEquals(new SSFalse(), "1.0 isLessThan: 1.0;");

      assertResultEquals(new SSString("1.0"), "1.0 asString;");

   }
   /****************************************************************************
   * 
   ****************************************************************************/
   @Test
   public void charSSLong_forProperExpression() throws Exception {

      assertResultEquals(new SSChar('a'), "'a';");
      assertResultEquals(new SSLong(2), "1 plus: 1;");

      assertResultEquals(new SSTrue(), "'a' equals: 'a';");
      assertResultEquals(new SSFalse(), "'a' equals: 'b';");
      assertResultEquals(new SSFalse(), "'a' equals: null;");
      assertResultEquals(new SSFalse(), "'a' isNotEqualTo: 'a';");
      assertResultEquals(new SSTrue(), "'a' isNotEqualTo: 'b';");
      assertResultEquals(new SSTrue(), "'a' isLessOrEqualTo: 'a';");
      assertResultEquals(new SSTrue(), "'a' isLessOrEqualTo: 'b';");
      assertResultEquals(new SSFalse(), "'b' isLessOrEqualTo: 'a';");
      assertResultEquals(new SSTrue(), "'b' isGreaterOrEqualTo: 'a';");
      assertResultEquals(new SSTrue(), "'b' isGreaterOrEqualTo: 'b';");
      assertResultEquals(new SSFalse(), "'a' isGreaterOrEqualTo: 'b';");
      assertResultEquals(new SSTrue(), "'b' isGreaterThan: 'a';");
      assertResultEquals(new SSFalse(), "'a' isGreaterThan: 'a';");
      assertResultEquals(new SSTrue(), "'a' isLessThan: 'b';");
      assertResultEquals(new SSFalse(), "'a' isLessThan: 'a';");

      assertResultEquals(new SSString("a"), "'a' asString;");

      assertResultEquals(new SSLong(97), "'a' hash;");
   }
   /****************************************************************************
   * 
   ****************************************************************************/
   @Test
   public void test_block() throws Exception {

      assertResultEquals(new SSLong(2), "!y = 1; {!x | x plus: 1;} executeWith: y;");
      assertResultEquals(new SSLong(3), """
            !a = 1;
            !b = 2;
            {!x1 !x2 | x1 plus: x2;} executeWith: a :and: b;
            """);
      assertResultEquals(new SSLong(6), """
            !a = 1;
            !b = 2;
            !c = 3;
            {!x1 !x2 !x3 | (x1 plus: x2) plus: x3;} executeWith: a :and: b :and: c;
            """);
      assertResultEquals(new SSLong(10), """
            !a = 1;
            !b = 2;
            !c = 3;
            !d = 4;
            {!x1 !x2 !x3 !x4| (x1 plus: x2) plus: (x3 plus: x4);}
                executeWith: a :and: b :and: c :and: d;
            """);
      assertResultEquals(new SSLong(15), """
            !a = 1;
            !b = 2;
            !c = 3;
            !d = 4;
            !e = 5;
            {!x1 !x2 !x3 !x4 !x5 | ((x1 plus: x2) plus: (x3 plus: x4)) plus: x5;}
                 executeWith: a :and: b :and: c :and: d :and: e;
            """);
      assertResultEquals(new SSLong(21),
            """
                  !a = 1;
                  !b = 2;
                  !c = 3;
                  !d = 4;
                  !e = 5;
                  !f = 6;
                  {!x1 !x2 !x3 !x4 !x5 !x6| ((x1 plus: x2) plus: (x3 plus: x4)) plus: (x5 plus: x6);}
                     executeWith: a :and: b :and: c :and: d :and: e :and: f;
                  """);

   }
   /****************************************************************************
   * 
   ****************************************************************************/
   @Test
   public void test_object_addMethod() throws Exception {

      assertResultEquals(new SSLong(1), """
            !object = Object new;
            object addMethod: 'a' :using: { 1;};
            object a;
            """);
   }
   /****************************************************************************
   * 
   ****************************************************************************/
   @Test
   public void test_object_addField() throws Exception {

      assertResultEquals(new SSLong(10), """
            !object = Object new;
            object addField: 'a';
            object a: 10;
            object a;
            """);
   }
   /****************************************************************************
   * 
   ****************************************************************************/
   @Test
   public void test_object_cloning() throws Exception {

      assertResultEquals(new SSFalse(), """
            !o = Object new;
            !new = (o clone);
            new equals: o;
            """);
   }
   /****************************************************************************
   * 
   ****************************************************************************/
   @Test
   public void test_iteration() throws Exception {

      assertResultEquals(new SSLong(10), """
            !counter = 0;
            {counter isLessThan: 10;} whileTrue: {
                counter = (counter plus: 1);
            };
            """);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   private void assertResultEquals(final SSObject o, final String program)
         throws IOException {

      assertEquals(o, new Interpreter().exacute(program, this.stack));
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   private Stack stack = Stack.create();
}
