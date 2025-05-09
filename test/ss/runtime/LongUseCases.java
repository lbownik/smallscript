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
public class LongUseCases extends UseCaseBase {
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void longHasSetBasicProperties() throws Exception {

      assertSSTrue("1 asString isEqualTo: \"1\";");
      assertResultEquals(new SSLong(1), "1 hash;");

      assertSSTrue("1 asDouble isEqualTo: 1.0;");
      assertSSTrue("1 asLong isEqualTo: 1;");
      assertSSTrue("1 size isEqualTo: 1;");
      assertSSTrue("1 nature isEqualTo: \"number\";");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void longEualsOnlyToItself() throws Exception {

      assertSSTrue("1 isEqualTo: 1;");
      assertSSFalse("1 isEqualTo: 1.0;");
      assertSSFalse("1 isEqualTo: 2;");
      assertSSFalse("1 isEqualTo: null;");
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
   }
   /****************************************************************************
    * 
    ****************************************************************************/
   @Test
   public void longCanPerformAritmetics() throws Exception {

      assertResultEquals(new SSLong(1), "1;");
      assertResultEquals(new SSLong(2), "1 plus: 1;");
      assertResultEquals(new SSDouble(2), "1 plus: 1.0;");
      assertResultEquals(new SSLong(0), "1 minus: 1;");
      assertResultEquals(new SSDouble(0), "1 minus: 1.0;");
      assertResultEquals(new SSLong(4), "2 multipliedBy: 2;");
      assertResultEquals(new SSDouble(4), "2 multipliedBy: 2.0;");
      assertResultEquals(new SSLong(3), "6 dividedBy: 2;");
      assertResultEquals(new SSDouble(3), "6 dividedBy: 2.0;");
   }
   /****************************************************************************
    * 
    ****************************************************************************/
   @Test
   public void arithmeticMethodsThrowsException_whenGivenNonNumericArgument()
         throws Exception {

      assertSSTrue("""
            true try: {
               1 plus: true;
               false;
            } :catch: {!e|
               (e nature isEqualTo: "exception") and:
               (e message isEqualTo: "Cannot cast object to number.");
            };
            """);
      assertSSTrue("""
            true try: {
               1 minus: true;
               false;
            } :catch: {!e|
               (e nature isEqualTo: "exception") and:
               (e message isEqualTo: "Cannot cast object to number.");
            };
            """);
      assertSSTrue("""
            true try: {
               1 multipliedBy: true;
               false;
            } :catch: {!e|
               (e nature isEqualTo: "exception") and:
               (e message isEqualTo: "Cannot cast object to number.");
            };
            """);
      assertSSTrue("""
            true try: {
               1 dividedBy: true;
               false;
            } :catch: {!e|
               (e nature isEqualTo: "exception") and:
               (e message isEqualTo: "Cannot cast object to number.");
            };
            """);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void longCanBeCloned() throws Exception {

      assertSSTrue("1 clone isEqualTo: 1;");
      assertSSTrue("""
            !old = 1;
            old addField: "test" :withValue: 1;
            !new = old clone;
            new test: 2;
            old test isEqualTo: 1;
            """);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void longCanProduceStreams() throws Exception {

      assertSSTrue("""
            !list = List new;
            
            1 to: 3 forEach: {!item | list append: item};
            
            (list size isEqualTo: 2) and: (list at: 0 isEqualTo: 1) and:
            (list at: 1 isEqualTo: 2);
            """);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void longCanRepeatActionNTimes() throws Exception {

      assertSSTrue("""
            !list = List new;
            
            !result = 2 times: {list append: 0};
            
            (list size isEqualTo: 2) and: (list at: 0 isEqualTo: 0) and:
            (list at: 1 isEqualTo: 0) and: (result nature isEqualTo: "list");
            """);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Test
   public void builtInMethods_returnArgumentLists() throws Exception {

      assertSSTrue("""
            1 method: "asDouble" arguments isEqualTo: (List new);
            """);
      assertSSTrue("""
            1 method: "asLong" arguments isEqualTo: (List new);
            """);
      assertSSTrue("""
            1 method: "clone" arguments isEqualTo: (List new);
            """);
      assertSSTrue("""
            1 method: "incremented" arguments isEqualTo: (List new);
            """);
      assertSSTrue("""
            1 method: "dividedBy:" arguments isEqualTo: (List append: "number");
            """);
      assertSSTrue("""
            1 method: "isGreaterThan:" arguments isEqualTo: (List append: "number");
            """);
      assertSSTrue("""
            1 method: "isGreaterOrEqualTo:" arguments isEqualTo: (List append: "number");
            """);
      assertSSTrue("""
            1 method: "isLessThan:" arguments isEqualTo: (List append: "number");
            """);
      assertSSTrue("""
            1 method: "isLessOrEqualTo:" arguments isEqualTo: (List append: "number");
            """);
      assertSSTrue("""
            1 method: "minus:" arguments isEqualTo: (List append: "number");
            """);
      assertSSTrue("""
            1 method: "multipliedBy:" arguments isEqualTo: (List append: "number");
            """);
      assertSSTrue("""
            1 method: "plus:" arguments isEqualTo: (List append: "number");
            """);
      assertSSTrue("""
            1 method: "times:" arguments isEqualTo: (List append: "block");
            """);
      assertSSTrue("""
            1 method: "to:" arguments isEqualTo: (List append: "number");
            """);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
}
