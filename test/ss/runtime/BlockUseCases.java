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
      assertSSTrue("{} execute isEqualTo: null;");
      assertSSTrue("""
            {
               #comment
            } execute isEqualTo: null;
            """);
   }
   /****************************************************************************
    * 
    ****************************************************************************/
   @Test
   public void blockEqualsOnlyToItself() throws Exception {
   
      assertSSTrue("""
            !block = {};
            block isEqualTo: block;
            """);
      assertSSFalse("{} isEqualTo: null;");
      assertSSFalse("{} isEqualTo: {};");
      
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

      assertSSTrue("{true } execute isEqualTo: true;");
   }
   /****************************************************************************
   * 
   ****************************************************************************/
   @Test
   public void execute_exacutesStatementsInBlock_forProperInvocation()
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
   public void whileTrue_iteratesProperly_forProperInvocation()
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

            closure execute isEqualTo: "abc";
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

            closure execute execute isEqualTo: "abc";
            """);
   }
   /****************************************************************************
    * 
    ****************************************************************************/
   @Test 
   public void blockCloning_works() throws Exception {
      
      
      assertSSTrue("""
            !block1 = {};
            !block2 = block1 clone;
            (block1 isNotEqualTo: block2) and: (block1 arguments isEqualTo: (block2 arguments));
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
   public void blockArguments_canBeListed() throws Exception {
      
      assertSSTrue("""
            !args = { } arguments;
            args size isEqualTo: 0;
            """);
      
      assertSSTrue("""
            !args = { !a !b | null; } arguments;
            (args size isEqualTo: 2) and: (args at: 0 isEqualTo: "a") and: (args at: 1 isEqualTo: "b");
            """);
   }
   /****************************************************************************
    * 
    ****************************************************************************/
   @Test
   public void blockCanGetAssignedNewFields() throws Exception {
      
      assertSSTrue("""
            !block = {};
            block addField: "field" :withValue: 2;
            block field isEqualTo: 2;
            """);
      
      assertSSTrue("""
            !block = {};
            block addField: "field" :withValue: 2;
            block field: 3;
             block field isEqualTo: 3;
            """);
      
      assertResultEquals(new SSLong(2), """
            !block = {};
            block addMethod: "return:" :using: { !this !value | value };
            block return: 2;
            """);
   }
   /****************************************************************************
    * 
    ****************************************************************************/
   @Test
   public void blockCanGetAssignedNewMethods() throws Exception {
      
      assertResultEquals(new SSLong(2), """
            !block = {};
            block addMethod: "return:" :using: { !this !value | value };
            block return: 2;
            """);
   }
   /****************************************************************************
    * 
    ****************************************************************************/
   @Test
   public void methodsAssignedToClonedBlock_areNotAssignedToOriginaBlock() 
         throws Exception {
      
      assertSSTrue("""
            !block = {};
            !clonedBlock = block clone;
            clonedBlock addMethod: "return:" :using: { !this !value | value };
            
            block try: {
               block return: 2;
               false;
            } :catch: { !e |
               clonedBlock return: 2 isEqualTo: 2;
            };
            """);
   }
   /****************************************************************************
    * 
    ****************************************************************************/
   @Test
   public void fieldsAssignedToClonedBlock_areNotAssignedToOriginaBlock() 
         throws Exception {
      
      assertSSTrue("""
            !block = {};
            !clonedBlock = block clone;
            clonedBlock addField: "field" :withValue: 2;
            
            block try: {
               block field;
               false;
            } :catch: { !e |
               clonedBlock field isEqualTo: 2;
            };
            """);
   }
}
