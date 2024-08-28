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
public class ListUseCases extends UseCaseBase {
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void listHasSetBasicProperties() throws Exception {

      assertSSTrue("List new size equals: 0;");
      assertSSTrue("List new nature equals: \"list\";");
      assertSSTrue("List new asString equals: \"[]\";");
      assertSSTrue("List hash isGreaterThan: 0;");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void listEualsOnlyToIdenticalList() throws Exception {

      assertSSTrue("List new equals: (List new);");
      assertSSFalse("List new isNotEqualTo: (List new);");

      assertSSFalse("List new equals: null;");
      assertSSTrue("List new isNotEqualTo: null;");
      
      assertSSTrue("List append: 1 equals: (List append: 1);");
      assertSSFalse("List append: 1 equals: (List append: 1 append: 2);");
      assertSSTrue("List append: 1 isNotEqualTo: (List append: 1 append: 2);");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void executingListDoesNothingAndReturnNull() throws Exception {

      assertSSTrue("List execute equals: List;");
      assertSSTrue("List new execute equals: (List new);");
      assertSSTrue("List append: 1 execute equals: (List append: 1);");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void emptyListHasNoElements()
         throws Exception {

      assertSSTrue("""
            !l = List new;
            l try: {
              l at: 0;
            } :catch: {!e |
              (e nature equals: "exception") and:
              (e message equals: "Index 0 out of bounds.");
            };
            """);
      assertSSTrue("""
            !counter = 0;
            !innerItem = null;
            !result = List new forEach: {!item |
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
   public void nonEmptyListReturnsElementsAtIndex()
         throws Exception {

      assertSSTrue("List append: 10 at: 0 equals: 10;");
      assertSSTrue("List append: 10 add: 11 at: 1 equals: 11;");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void forEachPerformsActionForEachElement()
         throws Exception {

      assertSSTrue("""
            !items = List new;
            !list = List append: 10 append: 11;
            !result = list forEach: {!item |
               items add: item;
            };
            (items equals: list) and: (result equals: list);
            """);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void elementsCanBeRemovedFromNonEptyList()
         throws Exception {

      assertSSTrue("List append: 10 append: 11 removeAt: 1 equals: (List append: 10);");
      assertSSTrue("List append: 10 append: 11 removeAt: 0 equals: (List append: 11);");
      assertSSTrue("List append: 10 removeAt: 0 equals: (List new);");
      assertSSTrue("List append: 10 removeAt: 0 :andReturnRemovedItem equals: 10;");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void elementsCanBePutAtSepcifiedIndex()
         throws Exception {

      assertSSTrue("List append: 10 at: 0 :put: 11 equals: (List append: 11);");
      assertSSTrue("List append: 10 at: 0 :put: 11 :andReturnPreviousItem equals: 10;");
   }
   /****************************************************************************
    * 
    ****************************************************************************/
   @Test
   public void elementsCanBeFiltered_andTransformed()
         throws Exception {

      assertSSTrue("""
            !source = List append: "abc" append: "abcd" append: "xy";
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
            !source = List append: "abc" append: "abcd" append: "xy";
            !result = source selectIf: { !item | item startsWith: "ab" }
                             selectIf: { !item | item equals: "abc" } 
                             collectTo: List;
            (result size equals: 1) and: (result at: 0 equals: "abc");
            """);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Test
   public void builtInMethods_returnArgumentLists() throws Exception {

      assertSSTrue("""
            List new method: "invoke::with:" arguments equals: (List append: "method" append: "argList");
            """);
      assertSSTrue("""
            List new method: "addField:" arguments equals: (List append: "name");
            """);
      assertSSTrue("""
            List new method: "addField::withValue:" arguments equals: (List append: "name" append: "value");
            """);
      assertSSTrue("""
            List new method: "addImmutableField::withValue:" arguments equals: (List append: "name" append: "value");
            """);
      assertSSTrue("""
            List new method: "addMethod::using:" arguments equals: (List append: "name" append: "block");
            """);
      assertSSTrue("""
            List new method: "asString" arguments equals: (List new);
            """);
      assertSSTrue("""
            List new method: "at:" arguments equals: (List append: "index");
            """);
      assertSSTrue("""
            List new method: "clone" arguments equals: (List new);
            """);
      assertSSTrue("""
            List new method: "collectTo:" arguments equals: (List append: "collector");
            """);
      assertSSTrue("""
            List new method: "equals:" arguments equals: (List append: "other");
            """);
      assertSSTrue("""
            List new method: "execute" arguments equals: (List new);
            """);
      assertSSTrue("""
            List new method: "fields" arguments equals: (List new);
            """);
      assertSSTrue("""
            List new method: "forEach:" arguments equals: (List append: "block");
            """);
      assertSSTrue("""
            List new method: "method:" arguments equals: (List append: "name");
            """);
      assertSSTrue("""
            List new method: "methods" arguments equals: (List new);
            """);
      assertSSTrue("""
            List new method: "nature" arguments equals: (List new);
            """);
      assertSSTrue("""
            List new method: "nature:" arguments equals: (List append: "value");
            """);
      assertSSTrue("""
            List new method: "hash" arguments equals: (List new);
            """);
      assertSSTrue("""
            List new method: "isNotEqualTo:" arguments equals: (List append: "other");
            """);
      assertSSTrue("""
            List new method: "orDefault:" arguments equals: (List append: "default");
            """);
      assertSSTrue("""
            List new method: "size" arguments equals: (List new);
            """);
      assertSSTrue("""
            List new method: "selectIf:" arguments equals: (List append: "block");
            """);
      assertSSTrue("""
            List new method: "throw" arguments equals: (List new);
            """);
      assertSSTrue("""
            List new method: "transformUsing:" arguments equals: (List append: "block");
            """);
      assertSSTrue("""
            List new method: "try::catch:" arguments equals: (List append: "tryBlock" append: "catchBlock");
            """);
      
      assertSSTrue("""
            List new method: "add:" arguments equals: (List append: "item");
            """);
      assertSSTrue("""
            List new method: "append:" arguments equals: (List append: "item");
            """);
      assertSSTrue("""
            List new method: "at::put:" arguments equals: (List append: "index" append: "item");
            """);
      assertSSTrue("""
            List new method: "at::put::andReturnPreviousItem" arguments equals: (List append: "index" append: "item");
            """);
      assertSSTrue("""
            List new method: "removeAt:" arguments equals: (List append: "index");
            """);
      assertSSTrue("""
            List new method: "removeAt::andReturnRemovedItem" arguments equals: (List append: "index");
            """);
   }
   /****************************************************************************
    * 
    ****************************************************************************/
}
