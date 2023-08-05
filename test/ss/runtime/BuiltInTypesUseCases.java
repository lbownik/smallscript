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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;
/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class BuiltInTypesUseCases {

   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void null_WorksProperly_forAllOperations() throws Exception {

      assertResultEquals(SSNull.instance(), "null;");
      assertResultEquals(SSNull.instance(), "null evaluate;");
      assertResultEquals(new SSLong(0), "null hash;");
      assertResultEquals(new SSTrue(), "null equals: null;");
      assertResultEquals(new SSFalse(), "null isNotEqualTo: null;");
      assertResultEquals(new SSFalse(), "null equals: true;");
      assertResultEquals(new SSTrue(), "null isNotEqualTo: true;");
      assertResultEquals(new SSString("null"), "null asString;");

      assertResultEquals(SSNull.instance(), "null not;");
      assertResultEquals(SSNull.instance(), "null ifTrue: false;");
      assertResultEquals(SSNull.instance(), "null execute;");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void true_WorksProperly_forAllOperations() throws Exception {

      assertResultEquals(new SSTrue(), "true;");
      assertResultEquals(new SSTrue(), "true execute;");
      assertResultEquals(new SSFalse(), "true not;");
      assertResultEquals(new SSTrue(), "true equals: true;");
      assertResultEquals(new SSFalse(), "true equals: false;");
      assertResultEquals(new SSFalse(), "true isNotEqualTo: true;");
      assertResultEquals(new SSTrue(), "true isNotEqualTo: false;");
      assertResultEquals(new SSString("true"), "true asString;");
      assertResultEquals(new SSTrue(), "true and: true;");
      assertResultEquals(new SSFalse(), "true and: false;");
      assertResultEquals(SSNull.instance(), "true and: null;");
      assertResultEquals(new SSLong(1), "true and: 1;");
      assertResultEquals(new SSTrue(), "true or: true;");
      assertResultEquals(new SSTrue(), "true or: false;");
      assertResultEquals(new SSTrue(), "true or: null;");
      assertResultEquals(new SSTrue(), "true or: 1;");
      assertResultEquals(new SSFalse(), "true xor: true;");
      assertResultEquals(new SSTrue(), "true xor: false;");
      assertResultEquals(SSNull.instance(), "true xor: null;");
      try {
         assertResultEquals(SSNull.instance(), "true xor: 1;");
         fail("\"true xor: 1;\" should have failed.");
      } catch (RuntimeException e) {
         assertEquals("Method 'not' is not defined.", e.getMessage());
      }
      assertResultEquals(new SSLong(0), "true ifTrue: 0;");
      assertResultEquals(SSNull.instance(), "true ifFalse: 0;");
      assertResultEquals(new SSLong(0), "true ifTrue: 0 :ifFalse: 1;");
      assertResultEquals(new SSLong(2), "true hash;");
      assertResultEquals(new SSTrue(), "true clone;");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void false_WorksProperly_forAllOperations() throws Exception {

      assertResultEquals(new SSFalse(), "false;");
      assertResultEquals(new SSFalse(), "false execute;");
      assertResultEquals(new SSTrue(), "false not;");
      assertResultEquals(new SSTrue(), "false equals: false;");
      assertResultEquals(new SSFalse(), "false equals: true;");
      assertResultEquals(new SSFalse(), "false isNotEqualTo: false;");
      assertResultEquals(new SSTrue(), "false isNotEqualTo: true;");
      assertResultEquals(new SSString("false"), "false asString;");
      assertResultEquals(new SSFalse(), "false and: false;");
      assertResultEquals(new SSFalse(), "false and: true;");
      assertResultEquals(new SSFalse(), "false and: null;");
      assertResultEquals(new SSFalse(), "false and: 1;");
      assertResultEquals(new SSTrue(), "false or: true;");
      assertResultEquals(new SSFalse(), "false or: false;");
      assertResultEquals(SSNull.instance(), "false or: null;");
      assertResultEquals(new SSLong(1), "false or: 1;");
      assertResultEquals(new SSTrue(), "false xor: true;");
      assertResultEquals(new SSFalse(), "false xor: false;");
      assertResultEquals(SSNull.instance(), "false xor: null;");
      assertResultEquals(new SSLong(1), "false or: 1;");
      assertResultEquals(SSNull.instance(), "false ifTrue: 0;");
      assertResultEquals(new SSLong(0), "false ifFalse: 0;");
      assertResultEquals(new SSLong(1), "false ifTrue: 0 :ifFalse: 1;");
      assertResultEquals(new SSLong(1), "false hash;");
      assertResultEquals(new SSFalse(), "false clone;");
   }

   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void string_WorksProperly_forAllOperations() throws Exception {

      assertResultEquals(new SSString("abc"), "\"abc\";");
      assertResultEquals(new SSLong(96354), "\"abc\" hash;");
      assertResultEquals(new SSTrue(), "\"abc\" equals: \"abc\";");
      assertResultEquals(new SSFalse(), "\"abc\" equals: \"a\";");
      assertResultEquals(new SSFalse(), "\"abc\" equals: null;");
      assertResultEquals(new SSFalse(), "\"abc\" isNotEqualTo: \"abc\";");
      assertResultEquals(new SSTrue(), "\"abc\" isNotEqualTo: \"a\";");
      assertResultEquals(new SSTrue(), "\"abc\" isNotEqualTo: null;");
      assertResultEquals(new SSLong(3), "\"abc\" size;");
      assertResultEquals(new SSChar('a'), "\"abc\" at: 0;");
      assertResultEquals(new SSString("abcd"), "\"abc\" concatenate: \"d\";");
      assertResultEquals(new SSString("abc"), "\"abc\" clone;");
   }
   /****************************************************************************
   * 
   ****************************************************************************/
   @Test
   public void char_WorksProperly_forAllOperations() throws Exception {

      assertResultEquals(new SSChar('a'), "'a';");
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
      assertResultEquals(new SSChar('a'), "'a' clone;");
   }

   /****************************************************************************
   * 
   ****************************************************************************/
   @Test
   public void long_WorksProperly_forAllOperations() throws Exception {

      assertResultEquals(new SSLong(1), "1;");
      assertResultEquals(new SSLong(2), "1 plus: 1;");
      assertResultEquals(new SSDouble(2), "1 plus: 1.0;");
      assertResultEquals(new SSLong(0), "1 minus: 1;");
      assertResultEquals(new SSDouble(0), "1 minus: 1.0;");
      assertResultEquals(new SSLong(4), "2 multipliedBy: 2;");
      assertResultEquals(new SSDouble(4), "2 multipliedBy: 2.0;");
      assertResultEquals(new SSLong(3), "6 dividedBy: 2;");
      assertResultEquals(new SSDouble(3), "6 dividedBy: 2.0;");
      try {
         assertResultEquals(SSNull.instance(), "1 plus: true;");
         fail("\"1 plus: true;\" should have failed.");
      } catch (RuntimeException e) {
         // good
      }
      assertResultEquals(new SSTrue(), "1 equals: 1;");
      assertResultEquals(new SSFalse(), "1 equals: 1.0;");
      assertResultEquals(new SSFalse(), "1 equals: 2;");
      assertResultEquals(new SSFalse(), "1 equals: null;");
      assertResultEquals(new SSFalse(), "1 isNotEqualTo: 1;");
      assertResultEquals(new SSTrue(), "1 isNotEqualTo: 1.0;");
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
      assertResultEquals(new SSLong(1), "1 clone;");
      assertResultEquals(new SSLong(1), "1 asLong;");
      assertResultEquals(new SSDouble(1), "1 asDouble;");
   }
   /****************************************************************************
   * 
   ****************************************************************************/
   @Test
   public void double_WorksProperly_forAllOperations() throws Exception {

      assertResultEquals(new SSDouble(2.1), "1.0 plus: 1.1;");
      assertResultEquals(new SSDouble(2.0), "1.0 plus: 1;");
      assertResultEquals(new SSDouble(0), "1.0 minus: 1.0;");
      assertResultEquals(new SSDouble(0), "1.0 minus: 1;");
      assertResultEquals(new SSDouble(4.0), "2.0 multipliedBy: 2.0;");
      assertResultEquals(new SSDouble(4.0), "2.0 multipliedBy: 2;");
      assertResultEquals(new SSDouble(3.0), "6.0 dividedBy: 2;");
      assertResultEquals(new SSDouble(3.0), "6 dividedBy: 2.0;");
      try {
         assertResultEquals(SSNull.instance(), "1.0 plus: true;");
         fail("\"1.0 plus: true;\" should have failed.");
      } catch (RuntimeException e) {
         // good
      }
      assertResultEquals(new SSTrue(), "1.0 equals: 1.0;");
      assertResultEquals(new SSFalse(), "1.0 equals: 1;");
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
      assertResultEquals(new SSLong(1072693248), "1.0 hash;");
      assertResultEquals(new SSDouble(1), "1.0 clone;");
      assertResultEquals(new SSDouble(1), "1.0 asDouble;");
      assertResultEquals(new SSLong(1), "1.0 asLong;");
   }
   /****************************************************************************
   * 
   ****************************************************************************/
   @Test
   public void objectFactory_WorksProperly_forAllOperations() throws Exception {

      assertResultEquals(new SSString("Object"), "Object asString;");
      assertResultEquals(new SSString("Object"), "Object clone asString;");
      assertNotNull(new Interpreter().exacute("Object new;", this.stack));
   }
   /****************************************************************************
   * 
   ****************************************************************************/
   @Test
   public void object_addMethod_createsNewMethod_forProperInvocation()
         throws Exception {

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
   public void object_addVield_createsNewFieldAccessorMethods_forProperInvocation()
         throws Exception {

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
   public void object_clone_createsNewObject_forProperInvocation() throws Exception {

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
   public void block_execute_exacutesStatementsInBlock_forProperInvocation()
         throws Exception {

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
   public void block_whileTrue_iteratesProperly_forProperInvocation()
         throws Exception {

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
