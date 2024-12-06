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
public class NullUseCases extends UseCaseBase {
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void nullHasSetBasicProperties() throws Exception {

      assertSSTrue("null size isEqualTo: 0;");
      assertSSTrue("null nature isEqualTo: \"null\";");
      assertSSTrue("null asString isEqualTo: \"null\";");
      assertSSTrue("null hash isEqualTo: 0;");
      assertSSTrue("null orDefault: 10 isEqualTo: 10;");
      assertSSTrue("null orDefault: {5 plus: 5} isEqualTo: 10;");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void nullEualsOnlyToItself() throws Exception {

      assertSSTrue("null isEqualTo: null;");
      assertSSFalse("null isNotEqualTo: null;");

      assertSSFalse("null isEqualTo: true;");
      assertSSTrue("null isNotEqualTo: true;");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void executingNullDoesNothingAndReturnNull() throws Exception {

      assertSSNull("null execute;");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void negationOfNullIsStillNull() throws Exception {

      assertSSNull("null not;");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void invokingUndefinedMethodsOnNull_doesNothing_andReturnsNull()
         throws Exception {

      assertSSNull("null ifTrue: false;");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void nullIsEquivalentToEmptyList()
         throws Exception {

      assertSSTrue("null size isEqualTo: 0;");
      assertSSTrue("null at: 0 isEqualTo: null;");
      assertSSTrue("""
            !counter = 0;
            !innerItem = null;
            !result = null forEach: {!item |
               counter = counter plus: 1;
               innerItem = item;
            };
            (counter isEqualTo: 0) and: (result isEqualTo: null) and: (innerItem isEqualTo: null);
            """);
      assertSSTrue("""
            !result = null selectIf: {!item | item startsWith: "x"} 
                           transformUsing: {!item | item size }
                           collectTo: List;
                           
            result isEqualTo: null;
            """);
      
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Test
   public void builtInMethods_returnNullAsArgumentLists() throws Exception {
      
      assertSSTrue("""
            null method: "asString" arguments isEqualTo: null;
            """);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void null_canBeThrown() throws Exception {

      assertSSTrue("""
            Object try: {
               null throw;
            } :catch: { !e |
               e isEqualTo: null;
            };
            """);
   }
   /****************************************************************************
    * 
    ****************************************************************************/
}
