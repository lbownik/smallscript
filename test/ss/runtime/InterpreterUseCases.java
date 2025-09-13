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

import org.junit.Test;
/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public class InterpreterUseCases extends UseCaseBase {
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void complexExpressios_evaluateProperly() throws Exception {

      assertSSTrue("true;");
      assertSSTrue("(true);");
      assertResultEquals(createString("true"), "true asString;");
      assertResultEquals(createLong(3569038), "true asString hash;");
      assertResultEquals(createString("3569038"), "true asString hash asString;");
      assertResultEquals(createString("3569038"),
            "1 isEqualTo: 1 asString hash asString;");
      assertResultEquals(createString("3569038"),
            "1 asString isEqualTo: \"1\" asString hash asString;");
      assertResultEquals(createString("true"),
            "1 asString isEqualTo: \"1\" isEqualTo: true asString;");
      assertResultEquals(createString("true"),
            "1 asString isEqualTo: \"1\" ifTrue: true :ifFalse: false asString;");
      assertResultEquals(createString("true"),
            "!x = 1 asString isEqualTo: \"1\" ifTrue: (1 isEqualTo: 1) :ifFalse: {!x = 1; false;} asString; x;");
      assertResultEquals(createLong(1),
            "true addMethod: \"a::b\" :using: {!this !param | param;}; true a: 1 :b;");
      assertResultEquals(createString("1"),
            "true addMethod: \"a::b\" :using: {!this !param | param;}; true a: 1 :b asString;");
      
      assertResultEquals(createDouble(3), "6 dividedBy: 2.0;");
      assertResultEquals(createLong(4), "(2 multipliedBy: 1) plus: 2;");
      assertResultEquals(createLong(6), "2 multipliedBy: (1 plus: 2);");
      assertResultEquals(createLong(1), "1 hash;");
      assertResultEquals(createLong(4), "({2 multipliedBy: 1;} execute) plus: 2;");
      assertResultEquals(createLong(6), "2 multipliedBy: ({1 plus: 2;} execute);");
      assertResultEquals(createLong(2), "(2 isGreaterThan: 1) ifTrue: 2;");
      assertSSTrue("(2 isGreaterThan: 1) ifFalse: 2 isEqualTo: null;");
      assertResultEquals(createLong(2),
            "(2 isGreaterThan: 1) ifTrue: 2 :ifFalse: 3;");
      assertResultEquals(createLong(7),
            "((2 isGreaterThan: 1) ifTrue: 2 :ifFalse: 3) plus: 5;");
      assertResultEquals(createLong(8), 
            "(2 isLessThan: 1) ifTrue: 2 :ifFalse: (3 plus: 5);");
      assertResultEquals(createLong(3),
            "(2 isLessThan: 1) ifTrue: 2 :ifFalse: 3 execute;");
      assertResultEquals(createLong(3),
            "((2 isLessThan: 1) ifTrue: {2;} :ifFalse: {3;}) execute;");
      assertResultEquals(createLong(18),
            "((2 multipliedBy: 2) plus: 2) multipliedBy: 3;");
      assertResultEquals(createLong(2), "!var = 2;");
      assertResultEquals(createLong(2), "true ifTrue: 2;");
      assertResultEquals(createLong(3), "!var = 2; var = 3; var;");
      
      assertResultEquals(createLong(10),"!c = 1; {c isLessThan: 10} whileTrue: {c = c plus: 1}; c;");
   }
   
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void exceptions_workProperly() throws Exception {
      
      assertSSTrue("""
            null try: { true} :catch: {!e | false} isEqualTo: null;
            """);
      
      assertSSTrue("""
            1 try: { 
              true;
            } :catch: {!e | 
              e;
            };
            """);
      assertResultEquals(createString("Exception"),"""
            1 try: {
              "Exception" throw;
            } :catch: {!e |
               e;
            };
            """);
      
      assertResultEquals(createString("Exception"),"""
            1 try: {
              {
                "Exception" throw;
              } execute;
            } :catch: {!e |
              e;
            };
            """);
      
      assertResultEquals(createString("Exception2"),"""
            1 try: {
              1 try: {
                {
                  "Exception" throw;
                 } execute;
               } :catch: {!e |
                 "Exception2" throw;
               };
            } :catch: {!e |
              e;
            };
            """);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
}
