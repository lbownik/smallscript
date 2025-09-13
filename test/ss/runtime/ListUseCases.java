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

      assertSSTrue("List new size isEqualTo: 0;");
      assertSSTrue("List new nature isEqualTo: \"list\";");
      assertSSTrue("List new asString isEqualTo: \"[]\";");
      assertSSTrue("List hash isGreaterThan: 0;");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void listEualsOnlyToIdenticalList() throws Exception {

      assertSSTrue("List new isEqualTo: (List new);");
      assertSSFalse("List new isNotEqualTo: (List new);");

      assertSSFalse("List new isEqualTo: null;");
      assertSSTrue("List new isNotEqualTo: null;");
      
      assertSSTrue("List append: 1 isEqualTo: (List append: 1);");
      assertSSFalse("List append: 1 isEqualTo: (List append: 1 append: 2);");
      assertSSTrue("List append: 1 isNotEqualTo: (List append: 1 append: 2);");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void executingListDoesNothingAndReturnNull() throws Exception {

      assertSSTrue("List execute isEqualTo: List;");
      assertSSTrue("List new execute isEqualTo: (List new);");
      assertSSTrue("List append: 1 execute isEqualTo: (List append: 1);");
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
              (e nature isEqualTo: "exception") and:
              (e message isEqualTo: "Index 0 out of bounds.");
            };
            """);
      assertSSTrue("""
            !counter = 0;
            !innerItem = null;
            !result = List new forEach: {!item |
               counter = counter plus: 1;
               innerItem = item;
            };
            counter isEqualTo: 0;
            """);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void nonEmptyListReturnsElementsAtIndex()
         throws Exception {

      assertSSTrue("List append: 10 at: 0 isEqualTo: 10;");
      assertSSTrue("List append: 10 add: 11 at: 1 isEqualTo: 11;");
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
            (items isEqualTo: list) and: (result isEqualTo: list);
            """);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void elementsCanBeRemovedFromNonEptyList()
         throws Exception {

      assertSSTrue("List append: 10 append: 11 removeAt: 1 isEqualTo: (List append: 10);");
      assertSSTrue("List append: 10 append: 11 removeAt: 0 isEqualTo: (List append: 11);");
      assertSSTrue("List append: 10 removeAt: 0 isEqualTo: (List new);");
      assertSSTrue("List append: 10 removeAt: 0 :andReturnRemovedItem isEqualTo: 10;");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void elementsCanBePutAtSepcifiedIndex()
         throws Exception {

      assertSSTrue("List append: 10 at: 0 :put: 11 isEqualTo: (List append: 11);");
      assertSSTrue("List append: 10 at: 0 :put: 11 :andReturnPreviousItem isEqualTo: 10;");
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
                   selectIf: { !item | item isEqualTo: "abc" } 
                   transformUsing: { !item | item size }
                   forEach: { !item | result append: item };
            (result size isEqualTo: 1) and: (result at: 0 isEqualTo: 3);
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
                             selectIf: { !item | item isEqualTo: "abc" } 
                             collectTo: List;
            (result size isEqualTo: 1) and: (result at: 0 isEqualTo: "abc");
            """);
   }
   /****************************************************************************
   * 
   ****************************************************************************/
   @Test
   public void method_List_new_cannotBeOverriden() throws Exception {

      assertSSTrue("""
            Object try: {
               List addMethod: "new" :using: {};
               false;
            } :catch: {!e | 
               e message isEqualTo: "Method 'List new' cannot be overriden.";
            };
            """);
   }
   
   /****************************************************************************
   * 
   ****************************************************************************/
   @Test
   public void method_List_append_cannotBeOverriden() throws Exception {

      assertSSTrue("""
            Object try: {
               List addMethod: "append:" :using: {};
               false;
            } :catch: {!e | 
               e message isEqualTo: "Method 'List append:' cannot be overriden.";
            };
            """);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Test
   public void builtInMethods_returnArgumentLists() throws Exception {

      assertSSTrue("""
            List new method: "invoke::with:" arguments isEqualTo: (List append: "method" append: "argList");
            """);
      assertSSTrue("""
            List new method: "addField:" arguments isEqualTo: (List append: "name");
            """);
      assertSSTrue("""
            List new method: "addField::withValue:" arguments isEqualTo: (List append: "name" append: "value");
            """);
      assertSSTrue("""
            List new method: "addMethod::using:" arguments isEqualTo: (List append: "name" append: "block");
            """);
      assertSSTrue("""
            List new method: "asString" arguments isEqualTo: (List new);
            """);
      assertSSTrue("""
            List new method: "at:" arguments isEqualTo: (List append: "index");
            """);
      assertSSTrue("""
            List new method: "clone" arguments isEqualTo: (List new);
            """);
      assertSSTrue("""
            List new method: "collectTo:" arguments isEqualTo: (List append: "collector");
            """);
      assertSSTrue("""
            List new method: "isEqualTo:" arguments isEqualTo: (List append: "other");
            """);
      assertSSTrue("""
            List new method: "execute" arguments isEqualTo: (List new);
            """);
      assertSSTrue("""
            List new method: "fields" arguments isEqualTo: (List new);
            """);
      assertSSTrue("""
            List new method: "forEach:" arguments isEqualTo: (List append: "block");
            """);
      assertSSTrue("""
            List new method: "method:" arguments isEqualTo: (List append: "name");
            """);
      assertSSTrue("""
            List new method: "methods" arguments isEqualTo: (List new);
            """);
      assertSSTrue("""
            List new method: "nature" arguments isEqualTo: (List new);
            """);
      assertSSTrue("""
            List new method: "hash" arguments isEqualTo: (List new);
            """);
      assertSSTrue("""
            List new method: "isNotEqualTo:" arguments isEqualTo: (List append: "other");
            """);
      assertSSTrue("""
            List new method: "orDefault:" arguments isEqualTo: (List append: "default");
            """);
      assertSSTrue("""
            List new method: "size" arguments isEqualTo: (List new);
            """);
      assertSSTrue("""
            List new method: "selectIf:" arguments isEqualTo: (List append: "block");
            """);
      assertSSTrue("""
            List new method: "throw" arguments isEqualTo: (List new);
            """);
      assertSSTrue("""
            List new method: "transformUsing:" arguments isEqualTo: (List append: "block");
            """);
      assertSSTrue("""
            List new method: "try::catch:" arguments isEqualTo: (List append: "tryBlock" append: "catchBlock");
            """);
      
      assertSSTrue("""
            List new method: "add:" arguments isEqualTo: (List append: "item");
            """);
      assertSSTrue("""
            List new method: "append:" arguments isEqualTo: (List append: "item");
            """);
      assertSSTrue("""
            List new method: "at::put:" arguments isEqualTo: (List append: "index" append: "item");
            """);
      assertSSTrue("""
            List new method: "at::put::andReturnPreviousItem" arguments isEqualTo: (List append: "index" append: "item");
            """);
      assertSSTrue("""
            List new method: "removeAt:" arguments isEqualTo: (List append: "index");
            """);
      assertSSTrue("""
            List new method: "removeAt::andReturnRemovedItem" arguments isEqualTo: (List append: "index");
            """);
   }
   /****************************************************************************
    * 
    ****************************************************************************/
}
