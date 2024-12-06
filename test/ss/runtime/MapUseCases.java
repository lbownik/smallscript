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
public class MapUseCases extends UseCaseBase {
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void mapHasMapBasicProperties() throws Exception {

      assertSSTrue("Map new size isEqualTo: 0;");
      assertSSTrue("Map new nature isEqualTo: \"map\";");
      assertSSTrue("Map new asString isEqualTo: \"{}\";");
      assertSSTrue("Map hash isGreaterThan: 0;");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void mapEualsOnlyToIdenticalMap() throws Exception {

      assertSSTrue("Map new isEqualTo: (Map new);");
      assertSSFalse("Map new isNotEqualTo: (Map new);");

      assertSSFalse("Map new isEqualTo: null;");
      assertSSTrue("Map new isNotEqualTo: null;");

      assertSSTrue("Map at: \"A\" :put: \"B\" isEqualTo: (Map at: \"A\" :put: \"B\");");
      assertSSFalse(
            "Map at: \"A\" :put: \"B\" isEqualTo: (Map at: \"A\" :put: \"B\" at: \"C\" :put: \"D\");");
      assertSSTrue("Map at: \"A\" :put: \"B\" isNotEqualTo: (Map at: \"A\" :put: \"B\" at: \"C\" :put: \"D\");");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void executingMapDoesNothingAndReturnNull() throws Exception {

      assertSSTrue("Map execute isEqualTo: Map;");
      assertSSTrue("Map new execute isEqualTo: (Map new);");
      assertSSTrue("Map at: \"A\" :put: \"B\" execute isEqualTo: (Map at: \"A\" :put: \"B\");");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void emptyMapHasNoElements() throws Exception {

      assertSSTrue("""
            !counter = 0;
            !innerItem = null;
            !result = Map new forEach: {!item |
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
   public void nonEmptyMapGrows() throws Exception {

      assertSSTrue("Map at: \"A\" :put: \"B\" size isEqualTo: 1;");
      assertSSTrue("Map at: \"A\" :put: \"B\" at: \"C\" :put: \"D\" size isEqualTo: 2;");
      
      assertSSTrue("Map at: \"A\" :put: \"B\" at: \"A\" isEqualTo: \"B\";");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void valuesAddedToMapCanBeRetireved() throws Exception {
      
      assertSSTrue("Map at: \"A\" :put: \"B\" at: \"A\" isEqualTo: \"B\";");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void retrievingNonExistentValueReturnNull() throws Exception {
      
      assertSSTrue("Map new at: \"A\" isEqualTo: null;");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void addingElementsIsIdempotent() throws Exception {

      assertSSTrue("Map at: \"A\" :put: \"B\" at: \"A\" :put: \"D\" isEqualTo: (Map at: \"A\" :put: \"D\");");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void addingMappingToMap_returnsNullOrPreviousValue() throws Exception {

      assertSSTrue("""
            !map = Map new;
            map at: \"A\" :put: \"B\" :andGetPreviousValue isEqualTo: null;
            """);
      assertSSTrue("""
            !map = Map  at: \"A\" :put: \"X\";
            map at: \"A\" :put: \"B\" :andGetPreviousValue isEqualTo: \"X\";
            """);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void removingMapping_returnsNullOrRemovedValue() throws Exception {

      assertSSTrue("""
            !map = Map new;
            map removeAt: \"A\" :andGetRemovedValue isEqualTo: null;
            """);
      assertSSTrue("""
            !map = Map  at: \"A\" :put: \"X\";
            map removeAt: \"A\" :andGetRemovedValue isEqualTo: \"X\";
            """);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void forEachPerformsActionForEachElement() throws Exception {

      assertSSTrue("""
            !items = Map new;
            Map at: \"A\" :put: \"B\" at: \"A\" :put: \"D\" forEach: {!key !value |
               items at: key :put: value;
            } isEqualTo: items;
            """);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void elementsCanBeRemovedFromNonEptyMap() throws Exception {

      assertSSTrue("Map at: \"A\" :put: \"B\" at: \"C\" :put: \"D\" removeAt: \"C\" isEqualTo: (Map at: \"A\" :put: \"B\");");
      assertSSTrue("Map at: \"A\" :put: \"B\" removeAt: \"A\" isEqualTo: (Map new);");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void mepReturnsListOfKeayAsSet() throws Exception {

      assertSSTrue("""
            !keys = Map new keys;
            (keys nature isEqualTo: "set") and: (keys size isEqualTo: 0);
            """);
      assertSSTrue("""
            !keys = Map at: "a" :put: "b" keys;
            !list = List new;
            keys forEach: {!key | list append: key;};
            (keys nature isEqualTo: "set") and: (keys size isEqualTo: 1)
            and: (list at: 0 isEqualTo: "a");
            """);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Test
   public void builtInMethods_returnArgumentLists() throws Exception {

      assertSSTrue("""
            Map new method: "invoke::with:" arguments isEqualTo: (List append: "method" append: "argList");
            """);
      assertSSTrue("""
            Map new method: "addField:" arguments isEqualTo: (List append: "name");
            """);
      assertSSTrue("""
            Map new method: "addField::withValue:" arguments isEqualTo: (List append: "name" append: "value");
            """);
      assertSSTrue("""
            Map new method: "addMethod::using:" arguments isEqualTo: (List append: "name" append: "block");
            """);
      assertSSTrue("""
            Map new method: "asString" arguments isEqualTo: (List new);
            """);
      assertSSTrue("""
            Map new method: "at:" arguments isEqualTo: (List append: "key");
            """);
      assertSSTrue("""
            Map new method: "clone" arguments isEqualTo: (List new);
            """);
      assertSSTrue("""
            Map new method: "collectTo:" arguments isEqualTo: (List append: "collector");
            """);
      assertSSTrue("""
            Map new method: "isEqualTo:" arguments isEqualTo: (List append: "other");
            """);
      assertSSTrue("""
            Map new method: "execute" arguments isEqualTo: (List new);
            """);
      assertSSTrue("""
            Map new method: "fields" arguments isEqualTo: (List new);
            """);
      assertSSTrue("""
            Map new method: "forEach:" arguments isEqualTo: (List append: "block");
            """);
      assertSSTrue("""
            Map new method: "method:" arguments isEqualTo: (List append: "name");
            """);
      assertSSTrue("""
            Map new method: "methods" arguments isEqualTo: (List new);
            """);
      assertSSTrue("""
            Map new method: "nature" arguments isEqualTo: (List new);
            """);
      assertSSTrue("""
            Map new method: "hash" arguments isEqualTo: (List new);
            """);
      assertSSTrue("""
            Map new method: "isNotEqualTo:" arguments isEqualTo: (List append: "other");
            """);
      assertSSTrue("""
            Map new method: "orDefault:" arguments isEqualTo: (List append: "default");
            """);
      assertSSTrue("""
            Map new method: "size" arguments isEqualTo: (List new);
            """);
      assertSSTrue("""
            Map new method: "selectIf:" arguments isEqualTo: (List append: "block");
            """);
      assertSSTrue("""
            Map new method: "throw" arguments isEqualTo: (List new);
            """);
      assertSSTrue("""
            Map new method: "transformUsing:" arguments isEqualTo: (List append: "block");
            """);
      assertSSTrue("""
            Map new method: "try::catch:" arguments isEqualTo: (List append: "tryBlock" append: "catchBlock");
            """);
      
      assertSSTrue("""
            Map new method: "at::put:" arguments isEqualTo: (List append: "key" append: "value");
            """);
      assertSSTrue("""
            Map new method: "at::put::andGetPreviousValue" arguments isEqualTo: (List append: "key" append: "value");
            """);
      assertSSTrue("""
            Map new method: "removeAt:" arguments isEqualTo: (List append: "key");
            """);
      assertSSTrue("""
            Map new method: "removeAt::andGetRemovedValue" arguments isEqualTo: (List append: "key");
            """);
   }
   /****************************************************************************
    * 
    ****************************************************************************/
}
