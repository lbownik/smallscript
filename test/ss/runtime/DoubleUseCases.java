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
public class DoubleUseCases extends UseCaseBase {
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void doubleHasSetBasicProperties() throws Exception {

      assertSSTrue("1.0 asString isEqualTo: \"1.0\";");
      assertSSTrue("1.0 hash isEqualTo: 1072693248;");

      assertSSTrue("1.0 asDouble isEqualTo: 1.0;");
      assertSSTrue("1.0 asLong isEqualTo: 1;");
      assertSSTrue("1.0 size isEqualTo: 1;");
      assertSSTrue("1.0 nature isEqualTo: \"number\";");
      assertSSTrue("""
            !o = 1.0;
            o orDefault: "a" isEqualTo: o;
            """);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void doubleEualsOnlyToItself() throws Exception {

      assertSSTrue("1.0 isEqualTo: 1.0;");
      assertSSFalse("1.0 isEqualTo: 1;");
      assertSSFalse("1.0 isEqualTo: 2.0;");
      assertSSFalse("1.0 isEqualTo: null;");
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
   }
   /****************************************************************************
    * 
    ****************************************************************************/
   @Test
   public void doubleCanPerformAritmetics() throws Exception {

      assertResultEquals(new SSDouble(2.1), "1.0 plus: 1.1;");
      assertResultEquals(new SSDouble(2.0), "1.0 plus: 1;");
      assertResultEquals(new SSDouble(0), "1.0 minus: 1.0;");
      assertResultEquals(new SSDouble(0), "1.0 minus: 1;");
      assertResultEquals(new SSDouble(4.0), "2.0 multipliedBy: 2.0;");
      assertResultEquals(new SSDouble(4.0), "2.0 multipliedBy: 2;");
      assertResultEquals(new SSDouble(3.0), "6.0 dividedBy: 2;");
      assertResultEquals(new SSDouble(3.0), "6 dividedBy: 2.0;");
   }
   /****************************************************************************
    * 
    ****************************************************************************/
   @Test
   public void arithmeticMethodsThrowsException_whenGivenNonNumericArgument()
         throws Exception {

      assertSSTrue("""
            true try: {
               1.0 plus: true;
               false;
            } :catch: {!e|
               (e nature isEqualTo: "exception") and:
               (e message isEqualTo: "Cannot cast object to number.");
            };
            """);
      assertSSTrue("""
            true try: {
               1.0 minus: true;
               false;
            } :catch: {!e|
               (e nature isEqualTo: "exception") and:
               (e message isEqualTo: "Cannot cast object to number.");
            };
            """);
      assertSSTrue("""
            true try: {
               1.0 multipliedBy: true;
               false;
            } :catch: {!e|
               (e nature isEqualTo: "exception") and:
               (e message isEqualTo: "Cannot cast object to number.");
            };
            """);
      assertSSTrue("""
            true try: {
               1.0 dividedBy: true;
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
   public void doubleCanBeCloned() throws Exception {

      assertSSTrue("1.0 clone isEqualTo: 1.0;");
      assertSSTrue("""
            !old = 1.0;
            old addField: "test" :withValue: 1;
            !new = old clone;
            new test: 2;
            old test isEqualTo: 1;
            """);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Test
   public void builtInMethods_returnArgumentLists() throws Exception {

      assertSSTrue("""
            1.0 method: "asDouble" arguments isEqualTo: (List new);
            """);
      assertSSTrue("""
            1.0 method: "asLong" arguments isEqualTo: (List new);
            """);
      assertSSTrue("""
            1.0 method: "clone" arguments isEqualTo: (List new);
            """);
      assertSSTrue("""
            1.0 method: "dividedBy:" arguments isEqualTo: (List append: "number");
            """);
      assertSSTrue("""
            1.0 method: "isGreaterThan:" arguments isEqualTo: (List append: "number");
            """);
      assertSSTrue("""
            1.0 method: "isGreaterOrEqualTo:" arguments isEqualTo: (List append: "number");
            """);
      assertSSTrue("""
            1.0 method: "isLessThan:" arguments isEqualTo: (List append: "number");
            """);
      assertSSTrue("""
            1.0 method: "isLessOrEqualTo:" arguments isEqualTo: (List append: "number");
            """);
      assertSSTrue("""
            1.0 method: "minus:" arguments isEqualTo: (List append: "number");
            """);
      assertSSTrue("""
            1.0 method: "multipliedBy:" arguments isEqualTo: (List append: "number");
            """);
      assertSSTrue("""
            1.0 method: "plus:" arguments isEqualTo: (List append: "number");
            """);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
}
