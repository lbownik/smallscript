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
      assertSSTrue("Set append: 10 size equals: 1;");
      assertSSTrue("Set append: 10 add: 11 size equals: 2;");
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
   public void removingElementsWorks() throws Exception {

      assertSSTrue("Set append: 10 append: 11 remove: 10 size equals: 1;");
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
    ****************************************************************************/
   @Test
   public void elementsCanBeFiltered_andTransformed()
         throws Exception {

      assertSSTrue("""
            !source = Set append: "abc" append: "abcd" append: "xy";
            !result = List new;
            source selectIf: { !item | item startsWith: "ab" }
                   selectIf: { !item | item equals: "abc" } 
                   transformUsing: { !item | item size }
                   forEach: { !item | result append: item };
            (result size equals: 1) and: (result at: 0 equals: 3);
            """);
   }
   /****************************************************************************
    * 
    ****************************************************************************/
   @Test
   public void elementsCanBeCollectedToAnotherList()
         throws Exception {

      assertSSTrue("""
            !source = Set append: "abc" append: "abcd" append: "xy";
            !result = source selectIf: { !item | item startsWith: "ab" }
                             selectIf: { !item | item equals: "abc" } 
                             collectTo: Set;
            (result size equals: 1);
            """);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Test
   public void builtInMethods_returnArgumentLists() throws Exception {

      assertSSTrue("""
            Set new method: "invoke::with:" arguments equals: (List append: "method" append: "argList");
            """);
      assertSSTrue("""
            Set new method: "addField:" arguments equals: (List append: "name");
            """);
      assertSSTrue("""
            Set new method: "addField::withValue:" arguments equals: (List append: "name" append: "value");
            """);
      assertSSTrue("""
            Set new method: "addImmutableField::withValue:" arguments equals: (List append: "name" append: "value");
            """);
      assertSSTrue("""
            Set new method: "addMethod::using:" arguments equals: (List append: "name" append: "block");
            """);
      assertSSTrue("""
            Set new method: "asString" arguments equals: (List new);
            """);
      assertSSTrue("""
            Set new method: "at:" arguments equals: (List append: "index");
            """);
      assertSSTrue("""
            Set new method: "clone" arguments equals: (List new);
            """);
      assertSSTrue("""
            Set new method: "collectTo:" arguments equals: (List append: "collector");
            """);
      assertSSTrue("""
            Set new method: "equals:" arguments equals: (List append: "other");
            """);
      assertSSTrue("""
            Set new method: "execute" arguments equals: (List new);
            """);
      assertSSTrue("""
            Set new method: "fields" arguments equals: (List new);
            """);
      assertSSTrue("""
            Set new method: "forEach:" arguments equals: (List append: "block");
            """);
      assertSSTrue("""
            Set new method: "method:" arguments equals: (List append: "name");
            """);
      assertSSTrue("""
            Set new method: "methods" arguments equals: (List new);
            """);
      assertSSTrue("""
            Set new method: "nature" arguments equals: (List new);
            """);
      assertSSTrue("""
            Set new method: "nature:" arguments equals: (List append: "value");
            """);
      assertSSTrue("""
            Set new method: "hash" arguments equals: (List new);
            """);
      assertSSTrue("""
            Set new method: "isNotEqualTo:" arguments equals: (List append: "other");
            """);
      assertSSTrue("""
            Set new method: "orDefault:" arguments equals: (List append: "default");
            """);
      assertSSTrue("""
            Set new method: "size" arguments equals: (List new);
            """);
      assertSSTrue("""
            Set new method: "selectIf:" arguments equals: (List append: "block");
            """);
      assertSSTrue("""
            Set new method: "throw" arguments equals: (List new);
            """);
      assertSSTrue("""
            Set new method: "transformUsing:" arguments equals: (List append: "block");
            """);
      assertSSTrue("""
            Set new method: "try::catch:" arguments equals: (List append: "tryBlock" append: "catchBlock");
            """);
      
      assertSSTrue("""
            Set new method: "add:" arguments equals: (List append: "item");
            """);
      assertSSTrue("""
            Set new method: "append:" arguments equals: (List append: "item");
            """);
      assertSSTrue("""
            Set new method: "remove:" arguments equals: (List append: "item");
            """);
   }
   /****************************************************************************
    * 
    ****************************************************************************/
}
