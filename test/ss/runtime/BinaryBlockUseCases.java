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

      assertSSTrue("Object method: \"asString\" size isEqualTo: 1;");
      assertSSTrue("Object method: \"asString\" nature isEqualTo: \"binaryBlock\";");
      assertSSTrue(
            "Object method: \"asString\" asString startsWith: \"binaryBlock#\";");
      assertSSTrue("Object method: \"asString\" hash isGreaterThan: 0;");
      assertSSTrue("Object method: \"asString\" fields size isEqualTo: 0;");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void at0_returnsTheBlockItself() throws Exception {
      assertSSTrue("""
            !block = Object method: "asString";
            block isEqualTo: block at: 0;
            """);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void forEach_executesOnlyForItself() throws Exception {
      assertSSTrue("""
            !outerBlock = null;
            !block = Object method: "asString";
            block forEach: { !item | outerBlock = item;};
            block isEqualTo: outerBlock;
            """);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void selectIf_transformUsing_collectTo_operateonThisBlock()
         throws Exception {
      assertSSTrue(
            """
                  !block = Object method: "asString";
                  !list = block selectIf: {!item | true} transformUsing: {!item | item} collectTo: List;
                  (list size isEqualTo: 1) and: (list at: 0 isEqualTo: block);
                  """);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void blockIsEqualOnlyToSelf() throws Exception {

      assertSSTrue(
            "Object method: \"asString\" isEqualTo: (Object method: \"asString\");");
      assertSSFalse(
            "Object method: \"asString\" isEqualTo: (Object method: \"hash\");");

      assertSSFalse(
            "Object method: \"asString\" isNotEqualTo: (Object method: \"asString\");");
      assertSSTrue(
            "Object method: \"asString\" isNotEqualTo: (Object method: \"hash\");");
      assertSSTrue(
            "Object method: \"asString\" isNotEqualTo: (Object method: null);");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void blockCanBeExecuted() throws Exception {

      assertSSTrue("""
            !block = Object method: "asString";
            block executeWith: true isEqualTo: "true";
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
                  (e nature isEqualTo: "exception") and:
                  (e message isEqualTo: "Method 'test' is not defined.");
               };
            """);
   }
   /****************************************************************************
   * 
   ****************************************************************************/
   @Test
   public void blockCanThrowItself_andExceuteTryCach() throws Exception {

      assertSSTrue("""
            !block = Object method: "asString";
            !outerBlock = null;
            block try: { !this |
              outerBlock = this;
              block throw;
            } :catch: {!e |
              (e isEqualTo: block) and: (outerBlock isEqualTo: block);
            };
            """);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void blockArguments_canBeListed() throws Exception {

      assertSSTrue("""
            !args = Object method: "hash" arguments;
            args size isEqualTo: 0;
            """);

      assertSSTrue("""
            !args = Object method: "addField::withValue:" arguments;
            (args size isEqualTo: 2) and: (args at: 0 isEqualTo: "name")
               and: (args at: 1 isEqualTo: "value");
            """);
   }
   /****************************************************************************
   * 
   ****************************************************************************/
   @Test
   public void cloning_returnNewInstanceOfBlock() throws Exception {

      assertSSTrue("""
            !block = Object method: "asString";
            (block clone isNotEqualTo: block) and:
            (block clone isNotEqualTo: null);
            """);
   }
   /****************************************************************************
   * 
   ****************************************************************************/
   @Test
   public void clonedBinaryBlock_presentsTheSameArgumentListAsOriginalBlock()
         throws Exception {

      assertSSTrue("""
            !args = Object method: "addField::withValue:" clone arguments;
            (args size isEqualTo: 2) and: (args at: 0 isEqualTo: "name")
               and: (args at: 1 isEqualTo: "value");
            """);
   }
   /****************************************************************************
    * 
    ****************************************************************************/
   @Test
   public void blockCanGetAssignedNewFields() throws Exception {
      
      assertSSTrue("""
            !block = Object method: "asString";
            block addField: "field" :withValue: 2;
            block field isEqualTo: 2;
            """);
      
      assertSSTrue("""
            !block = Object method: "asString";
            block addField: "field" :withValue: 2;
            block field: 3;
             block field isEqualTo: 3;
            """);
      
      assertResultEquals(createLong(2), """
            !block = Object method: "asString";
            block addMethod: "return:" :using: { !this !value | value };
            block return: 2;
            """);
   }
   /****************************************************************************
    * 
    ****************************************************************************/
   @Test
   public void blockCanGetAssignedNewMethods() throws Exception {
      
      assertResultEquals(createLong(2), """
            !block = Object method: "asString";
            block addMethod: "return:" :using: { !this !value | value };
            block return: 2;
            """);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void methodsAssignedToOriginalBlock_areCopiedToClonedBlock() 
         throws Exception {
      
      assertSSTrue("""
            !block = Object method: "asString";
            block addMethod: "return:" :using: { !this !value | value };
            !clonedBlock = block clone;
            
            (block return: 2 isEqualTo: 2) 
               and: (clonedBlock return: 2 isEqualTo: 2);
            """);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void methodsAssignedToClonedBlock_areNotAssignedToOriginaBlock() 
         throws Exception {
      
      assertSSTrue("""
            !block = Object method: "asString";
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
    ***************************************************************************/
   @Test
   public void fieldsAssignedToOriginalBlock_areCopiedToClonedBlock() 
         throws Exception {
      
      assertSSTrue("""
            !block = Object method: "asString";
            block addField: "field" :withValue: 2;
            !clonedBlock = block clone;
            
            (block field isEqualTo: 2) 
               and: (clonedBlock field isEqualTo: 2);
            """);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void fieldsAssignedToClonedBlock_areNotAssignedToOriginaBlock() 
         throws Exception {
      
      assertSSTrue("""
            !block = Object method: "asString";
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
