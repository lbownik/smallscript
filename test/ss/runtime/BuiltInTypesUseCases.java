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

import org.junit.Test;
/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class BuiltInTypesUseCases extends UseCaseBase {
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void string_WorksProperly_forAllOperations() throws Exception {

      assertResultEquals(new SSString("abc"), "\"abc\";");
      assertResultEquals(new SSLong(96354), "\"abc\" hash;");
      assertSSTrue("\"abc\" equals: \"abc\";");
      assertSSFalse("\"abc\" equals: \"a\";");
      assertSSFalse("\"abc\" equals: null;");
      assertSSFalse("\"abc\" isNotEqualTo: \"abc\";");
      assertSSTrue("\"abc\" isNotEqualTo: \"a\";");
      assertSSTrue("\"abc\" isNotEqualTo: null;");
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
      assertSSTrue("'a' equals: 'a';");
      assertSSFalse("'a' equals: 'b';");
      assertSSFalse("'a' equals: null;");
      assertSSFalse("'a' isNotEqualTo: 'a';");
      assertSSTrue("'a' isNotEqualTo: 'b';");
      assertSSTrue("'a' isLessOrEqualTo: 'a';");
      assertSSTrue("'a' isLessOrEqualTo: 'b';");
      assertSSFalse("'b' isLessOrEqualTo: 'a';");
      assertSSTrue("'b' isGreaterOrEqualTo: 'a';");
      assertSSTrue("'b' isGreaterOrEqualTo: 'b';");
      assertSSFalse("'a' isGreaterOrEqualTo: 'b';");
      assertSSTrue("'b' isGreaterThan: 'a';");
      assertSSFalse("'a' isGreaterThan: 'a';");
      assertSSTrue("'a' isLessThan: 'b';");
      assertSSFalse("'a' isLessThan: 'a';");
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
         assertSSNull("1 plus: true;");
         fail("\"1 plus: true;\" should have failed.");
      } catch (RuntimeException e) {
         // good
      }
      assertSSTrue("1 equals: 1;");
      assertSSFalse("1 equals: 1.0;");
      assertSSFalse("1 equals: 2;");
      assertSSFalse("1 equals: null;");
      assertSSFalse("1 isNotEqualTo: 1;");
      assertSSTrue("1 isNotEqualTo: 1.0;");
      assertSSTrue("1 isNotEqualTo: 2;");
      assertSSTrue("1 isNotEqualTo: null;");
      assertSSTrue("1 isLessOrEqualTo: 1;");
      assertSSTrue("1 isLessOrEqualTo: 2;");
      assertSSFalse("1 isLessOrEqualTo: 0;");
      assertSSTrue("2 isGreaterOrEqualTo: 1;");
      assertSSTrue("2 isGreaterOrEqualTo: 2;");
      assertSSFalse("0 isGreaterOrEqualTo: 2;");
      assertSSTrue("1 isGreaterThan: 0;");
      assertSSFalse("1 isGreaterThan: 1;");
      assertSSTrue("0 isLessThan: 1;");
      assertSSFalse("1 isLessThan: 1;");
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
         assertSSNull("1.0 plus: true;");
         fail("\"1.0 plus: true;\" should have failed.");
      } catch (RuntimeException e) {
         // good
      }
      assertSSTrue("1.0 equals: 1.0;");
      assertSSFalse("1.0 equals: 1;");
      assertSSFalse("1.0 equals: 2.0;");
      assertSSFalse("1.0 equals: null;");
      assertSSFalse("1.0 isNotEqualTo: 1.0;");
      assertSSTrue("1.0 isNotEqualTo: 2.0;");
      assertSSTrue("1.0 isLessOrEqualTo: 1.0;");
      assertSSTrue("1.0 isLessOrEqualTo: 2.0;");
      assertSSFalse("1.0 isLessOrEqualTo: 0.0;");
      assertSSTrue("2.0 isGreaterOrEqualTo: 1.0;");
      assertSSTrue("2.0 isGreaterOrEqualTo: 2.0;");
      assertSSFalse("0.0 isGreaterOrEqualTo: 2.0;");
      assertSSTrue("1.0 isGreaterThan: 0.0;");
      assertSSFalse("1.0 isGreaterThan: 1.0;");
      assertSSTrue("0.0 isLessThan: 1.0;");
      assertSSFalse("1.0 isLessThan: 1.0;");
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
   public void objectFactory_worksProperly_forAllOperations() throws Exception {

      assertResultEquals(new SSString("Object"), "Object asString;");
      assertResultEquals(new SSString("Object"), "Object clone asString;");
      assertNotNull(new Interpreter().exacute("Object new;", this.stack));
   }

   /****************************************************************************
    * 
    ****************************************************************************/
   @Test
   public void emptyBlock_evaluatesToNull() throws Exception {

      assertEquals(SSNull.instance(),
            new Interpreter().exacute("{};", this.stack).invoke(stack, "execute"));
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
    ****************************************************************************/
}
