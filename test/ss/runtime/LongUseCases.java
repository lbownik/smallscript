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

      assertSSTrue("1 asString equals: \"1\";");
      assertResultEquals(new SSLong(1), "1 hash;");

      assertSSTrue("1 asDouble equals: 1.0;");
      assertSSTrue("1 asLong equals: 1;");
      assertSSTrue("1 size equals: 1;");
      assertSSTrue("1 nature equals: \"number\";");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void longEualsOnlyToItself() throws Exception {

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
               (e nature equals: "exception") and:
               (e message equals: "Cannot cast object to number.");
            };
            """);
      assertSSTrue("""
            true try: {
               1 minus: true;
               false;
            } :catch: {!e|
               (e nature equals: "exception") and:
               (e message equals: "Cannot cast object to number.");
            };
            """);
      assertSSTrue("""
            true try: {
               1 multipliedBy: true;
               false;
            } :catch: {!e|
               (e nature equals: "exception") and:
               (e message equals: "Cannot cast object to number.");
            };
            """);
      assertSSTrue("""
            true try: {
               1 dividedBy: true;
               false;
            } :catch: {!e|
               (e nature equals: "exception") and:
               (e message equals: "Cannot cast object to number.");
            };
            """);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void longCanBeCloned() throws Exception {

      assertSSTrue("1 clone equals: 1;");
      assertSSTrue("""
            !old = 1;
            old addField: "test" :withValue: 1;
            !new = old clone;
            new test: 2;
            old test equals: 1;
            """);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
}
