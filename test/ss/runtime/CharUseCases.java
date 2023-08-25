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
public class CharUseCases extends UseCaseBase {
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void charHasSetBasicProperties() throws Exception {

      assertSSTrue("'a' size equals: 1;");
      assertSSTrue("'a' nature equals: \"character\";");
      assertSSTrue("'a' asString equals: \"a\";");
      assertSSTrue("'a' hash isGreaterThan: 0;");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void charEualsOnlyToItself() throws Exception {

      assertSSTrue("'a' equals: 'a';");
      assertSSFalse("'a' equals: 'b';");
      assertSSFalse("'a' equals: null;");
      assertSSFalse("'a' isNotEqualTo: 'a';");
      assertSSTrue("'a' isNotEqualTo: 'b';");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void charsAreLessOrGreaterThanOneAnother() throws Exception {

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
   }
   /****************************************************************************
   * 
   ****************************************************************************/
   @Test
   public void charCanBeCloned() throws Exception {

      assertSSTrue("""
            !old = 'a';
            old addField: "test" :withValue: 1;
            !new = old clone;
            new test: 2;
            old test equals: 1;
            """);
   }
   /****************************************************************************
   * 
   ****************************************************************************/
}
