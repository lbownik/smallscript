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

      assertSSTrue("Set new size isEqualTo: 0;");
      assertSSTrue("Set new nature isEqualTo: \"set\";");
      assertSSTrue("Set new asString isEqualTo: \"[]\";");
      assertSSTrue("Set hash isGreaterThan: 0;");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void setEualsOnlyToIdenticalSet() throws Exception {

      assertSSTrue("Set new isEqualTo: (Set new);");
      assertSSFalse("Set new isNotEqualTo: (Set new);");

      assertSSFalse("Set new isEqualTo: null;");
      assertSSTrue("Set new isNotEqualTo: null;");

      assertSSTrue("Set append: 1 isEqualTo: (Set append: 1);");
      assertSSFalse("Set append: 1 isEqualTo: (Set append: 1 append: 2);");
      assertSSTrue("Set append: 1 isNotEqualTo: (Set append: 1 append: 2);");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void executingSetDoesNothingAndReturnNull() throws Exception {

      assertSSTrue("Set execute isEqualTo: Set;");
      assertSSTrue("Set new execute isEqualTo: (Set new);");
      assertSSTrue("Set append: 1 execute isEqualTo: (Set append: 1);");
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
            counter isEqualTo: 0;
            """);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void nonEmptySetGrows() throws Exception {

      assertSSTrue("Set append: 10 size isEqualTo: 1;");
      assertSSTrue("Set append: 10 size isEqualTo: 1;");
      assertSSTrue("Set append: 10 add: 11 size isEqualTo: 2;");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void addingElementsIsIdempotent() throws Exception {

      assertSSTrue("Set append: 10 append: 10 isEqualTo: (Set append: 10);");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void removingElementsWorks() throws Exception {

      assertSSTrue("Set append: 10 append: 11 remove: 10 size isEqualTo: 1;");
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
            } isEqualTo: items;
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
            !source = Set append: "abc" append: "abcd" append: "xy";
            !result = source selectIf: { !item | item startsWith: "ab" }
                             selectIf: { !item | item isEqualTo: "abc" } 
                             collectTo: Set;
            (result size isEqualTo: 1);
            """);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Test
   public void builtInMethods_returnArgumentLists() throws Exception {

      assertSSTrue("""
            Set new method: "invoke::with:" arguments isEqualTo: (List append: "method" append: "argList");
            """);
      assertSSTrue("""
            Set new method: "addField:" arguments isEqualTo: (List append: "name");
            """);
      assertSSTrue("""
            Set new method: "addField::withValue:" arguments isEqualTo: (List append: "name" append: "value");
            """);
      assertSSTrue("""
            Set new method: "addMethod::using:" arguments isEqualTo: (List append: "name" append: "block");
            """);
      assertSSTrue("""
            Set new method: "asString" arguments isEqualTo: (List new);
            """);
      assertSSTrue("""
            Set new method: "at:" arguments isEqualTo: (List append: "index");
            """);
      assertSSTrue("""
            Set new method: "clone" arguments isEqualTo: (List new);
            """);
      assertSSTrue("""
            Set new method: "collectTo:" arguments isEqualTo: (List append: "collector");
            """);
      assertSSTrue("""
            Set new method: "isEqualTo:" arguments isEqualTo: (List append: "other");
            """);
      assertSSTrue("""
            Set new method: "execute" arguments isEqualTo: (List new);
            """);
      assertSSTrue("""
            Set new method: "fields" arguments isEqualTo: (List new);
            """);
      assertSSTrue("""
            Set new method: "forEach:" arguments isEqualTo: (List append: "block");
            """);
      assertSSTrue("""
            Set new method: "method:" arguments isEqualTo: (List append: "name");
            """);
      assertSSTrue("""
            Set new method: "methods" arguments isEqualTo: (List new);
            """);
      assertSSTrue("""
            Set new method: "nature" arguments isEqualTo: (List new);
            """);
      assertSSTrue("""
            Set new method: "hash" arguments isEqualTo: (List new);
            """);
      assertSSTrue("""
            Set new method: "isNotEqualTo:" arguments isEqualTo: (List append: "other");
            """);
      assertSSTrue("""
            Set new method: "orDefault:" arguments isEqualTo: (List append: "default");
            """);
      assertSSTrue("""
            Set new method: "size" arguments isEqualTo: (List new);
            """);
      assertSSTrue("""
            Set new method: "selectIf:" arguments isEqualTo: (List append: "block");
            """);
      assertSSTrue("""
            Set new method: "throw" arguments isEqualTo: (List new);
            """);
      assertSSTrue("""
            Set new method: "transformUsing:" arguments isEqualTo: (List append: "block");
            """);
      assertSSTrue("""
            Set new method: "try::catch:" arguments isEqualTo: (List append: "tryBlock" append: "catchBlock");
            """);
      
      assertSSTrue("""
            Set new method: "add:" arguments isEqualTo: (List append: "item");
            """);
      assertSSTrue("""
            Set new method: "append:" arguments isEqualTo: (List append: "item");
            """);
      assertSSTrue("""
            Set new method: "remove:" arguments isEqualTo: (List append: "item");
            """);
   }
   /****************************************************************************
    * 
    ****************************************************************************/
}
