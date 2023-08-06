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
import static org.junit.Assert.assertTrue;

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
   public void complexExpressios_evaluateProperly() throws Exception {

      assertResultEquals(new SSTrue(), "true;");
      assertResultEquals(new SSTrue(), "(true);");
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
      
      assertResultEquals(new SSDouble(3), "6 dividedBy: 2.0;");
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
      
      assertResultEquals(new SSLong(10),"!c = 1; {c isLessThan: 10} whileTrue: {c = c plus: 1}; c;");
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
