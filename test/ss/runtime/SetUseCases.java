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
public class SetUseCases extends UseCaseBase {
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void setHasSetBasicProperties() throws Exception {

      assertSSTrue("Set new size equals: 0;");
      assertSSTrue("Set new nature equals: \"set\";");
      assertSSTrue("Set new asString equals: \"[]\";");
      assertSSTrue("Set hash isGreaterThan: 0;");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void setEualsOnlyToIdenticalSet() throws Exception {

      assertSSTrue("Set new equals: (Set new);");
      assertSSFalse("Set new isNotEqualTo: (Set new);");

      assertSSFalse("Set new equals: null;");
      assertSSTrue("Set new isNotEqualTo: null;");

      assertSSTrue("Set append: 1 equals: (Set append: 1);");
      assertSSFalse("Set append: 1 equals: (Set append: 1 append: 2);");
      assertSSTrue("Set append: 1 isNotEqualTo: (Set append: 1 append: 2);");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void executingSetDoesNothingAndReturnNull() throws Exception {

      assertSSTrue("Set execute equals: Set;");
      assertSSTrue("Set new execute equals: (Set new);");
      assertSSTrue("Set append: 1 execute equals: (Set append: 1);");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void emptySetHasNoElements() throws Exception {

      assertSSTrue("""
            !counter = 0;
            !innerItem = null;
            !result = Set new forEach: {!item |
               counter = counter plus: 1;
               innerItem = item;
            };
            counter equals: 0;
            """);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void nonEmptySetGrows() throws Exception {

      assertSSTrue("Set append: 10 size equals: 1;");
      assertSSTrue("Set append: 10 append: 11 size equals: 2;");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void addingElementsIsIdempotent() throws Exception {

      assertSSTrue("Set append: 10 append: 10 equals: (Set append: 10);");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void forEachPerformsActionForEachElement() throws Exception {

      assertSSTrue("""
            !items = Set new;
            Set append: 10 append: 11 forEach: {!item |
               items append: item;
            } equals: items;
            """);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void elementsCanBeRemovedFromNonEptySet() throws Exception {

      assertSSTrue("Set append: 10 append: 11 remove: 11 equals: (Set append: 10);");
      assertSSTrue(
            "Set append: 10 append: 11 remove: 10 equals: (Set append: 11);");
      assertSSTrue("Set append: 10 remove: 10 equals: (Set new);");
   }
   /****************************************************************************
    * 
    ****************************************************************************/
}
