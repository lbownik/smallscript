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

public class BinaryBlockUseCases extends UseCaseBase {

   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void blockHasSetBasicProperties() throws Exception {

      assertSSTrue("Object method: \"asString\" size equals: 1;");
      assertSSTrue("Object method: \"asString\" nature equals: \"binaryBlock\";");
      assertSSTrue(
            "Object method: \"asString\" asString startsWith: \"binaryBlock#\";");
      assertSSTrue("Object method: \"asString\" hash isGreaterThan: 0;");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void blockIsEqualOnlyToSelf() throws Exception {

      assertSSTrue(
            "Object method: \"asString\" equals: (Object method: \"asString\");");
      assertSSFalse(
            "Object method: \"asString\" equals: (Object method: \"hash\");");

      assertSSFalse(
            "Object method: \"asString\" isNotEqualTo: (Object method: \"asString\");");
      assertSSTrue(
            "Object method: \"asString\" isNotEqualTo: (Object method: \"hash\");");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void blockCanBeExecuted() throws Exception {

      assertSSTrue("""
            !block = Object method: "asString";
            block executeWith: true equals: "true";
            """);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Test
   public void callingInexistentMethodThrowsException() throws Exception {

      assertSSTrue("""
               !block = Object method: "asString";
               true try: {
                  block test;
               } :catch: {!e |
                  (e nature equals: "exception") and:
                  (e message equals: "Method 'test' is not defined.");
               };
            """);
   }
   /****************************************************************************
   * 
   ***************************************************************************/
   @Test
   public void blockCanThrowItself_andExceuteTryCach() throws Exception {

      assertSSTrue("""
            !block = Object method: "asString";
            block try: {
              block throw;
            } :catch: {!e |
              e equals: block;
            };
            """);
   }
   /****************************************************************************
   * 
   ***************************************************************************/
   @Test
   public void cloaning_returnNewInstanceOfBlock() throws Exception {

      assertSSTrue("""
            !block = Object method: "asString";
            (block clone isNotEqualTo: block) and:
            (block clone isNotEqualTo: null);
            """);
   }
}
