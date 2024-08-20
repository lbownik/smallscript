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

import java.util.List;

import org.junit.Test;

public class BlockUseCases extends UseCaseBase {

   /****************************************************************************
    * 
    ****************************************************************************/
   @Test
   public void emptyBlock_evaluatesToNull() throws Exception {

      assertSSTrue("{} isNotEqualTo: null;");
      assertSSTrue("{} execute equals: null;");
      assertSSTrue("""
            {
               #comment
            } execute equals: null;
            """);
   }
   /****************************************************************************
    * 
    ****************************************************************************/
   @Test
   public void block_equalsOnlyToItself() throws Exception {
   
      assertSSTrue("""
            !block = {};
            block equals: block;
            """);
      assertSSFalse("{} equals: null;");
      assertSSFalse("{} equals: {};");
      
      assertSSFalse("""
            !block = {};
            block isNotEqualTo: block;
            """);
      assertSSTrue("{} isNotEqualTo: {};");
      
   }
   /****************************************************************************
    * 
    ****************************************************************************/
   @Test
   public void blockWithAVariableName_evaluatesToTheVariableValue() throws Exception {

      assertSSTrue("{true } execute equals: true;");
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
   @Test
   public void arguments_returnsArgumentNames_forProperInvocation()
         throws Exception {

      assertResultEquals(new SSString("arg"), """
            {!arg | null;} arguments at: 0;
            """);
      assertResultEquals(new SSLong(0), """
            {} arguments size;
            """);
   }
   /****************************************************************************
    * 
    ****************************************************************************/
   @Test
   public void blockClosures_work() throws Exception {

      assertSSTrue("""
            !closure = {
               !val = "abc";
               {
                  val;
               };
            } execute;

            closure execute equals: "abc";
            """);
      
      assertSSTrue("""
            !closure = {
               !val = "abc";
               {
                  {
                     val;
                  };
               };
            } execute;

            closure execute execute equals: "abc";
            """);
   }
   /****************************************************************************
    * 
    ****************************************************************************/
   @Test 
   public void blocksDontClone() throws Exception {
      
      
      assertSSTrue("""
            !block1 = {};
            !block2 = block1 clone;
            block1 equals: block2;
            """);
   }
   /****************************************************************************
    * 
    ****************************************************************************/
   @Test 
   public void parsingBlock_works() throws Exception {
      
      SSBlock block = new Parser().parse("!arg | arg;");
      SSString arg = new SSString("abc");
      Stack stack = Stack.create();
      
      assertEquals(arg, block.execute(stack, List.of(arg)));
   }
   /****************************************************************************
    * 
    ****************************************************************************/
   @Test 
   public void test() throws Exception {
      
//      System.out.println(new Parser().parse("""
//            { !a !b !c | 
//               !d = a asString;
//               b;
//            };
//            """));
      System.out.println(new Parser().parse("""
            true ifTrue: "a" :ifFalse: "b";
            """));
   }
   /****************************************************************************
    * 
    ****************************************************************************/
}
