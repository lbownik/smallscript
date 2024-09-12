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

      assertSSTrue("Map new size equals: 0;");
      assertSSTrue("Map new nature equals: \"map\";");
      assertSSTrue("Map new asString equals: \"{}\";");
      assertSSTrue("Map hash isGreaterThan: 0;");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void mapEualsOnlyToIdenticalMap() throws Exception {

      assertSSTrue("Map new equals: (Map new);");
      assertSSFalse("Map new isNotEqualTo: (Map new);");

      assertSSFalse("Map new equals: null;");
      assertSSTrue("Map new isNotEqualTo: null;");

      assertSSTrue("Map at: \"A\" :put: \"B\" equals: (Map at: \"A\" :put: \"B\");");
      assertSSFalse(
            "Map at: \"A\" :put: \"B\" equals: (Map at: \"A\" :put: \"B\" at: \"C\" :put: \"D\");");
      assertSSTrue("Map at: \"A\" :put: \"B\" isNotEqualTo: (Map at: \"A\" :put: \"B\" at: \"C\" :put: \"D\");");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void executingMapDoesNothingAndReturnNull() throws Exception {

      assertSSTrue("Map execute equals: Map;");
      assertSSTrue("Map new execute equals: (Map new);");
      assertSSTrue("Map at: \"A\" :put: \"B\" execute equals: (Map at: \"A\" :put: \"B\");");
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
            counter equals: 0;
            """);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void nonEmptyMapGrows() throws Exception {

      assertSSTrue("Map at: \"A\" :put: \"B\" size equals: 1;");
      assertSSTrue("Map at: \"A\" :put: \"B\" at: \"C\" :put: \"D\" size equals: 2;");
      
      assertSSTrue("Map at: \"A\" :put: \"B\" at: \"A\" equals: \"B\";");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void valuesAddedToMapCanBeRetireved() throws Exception {
      
      assertSSTrue("Map at: \"A\" :put: \"B\" at: \"A\" equals: \"B\";");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void retrievingNonExistentValueReturnNull() throws Exception {
      
      assertSSTrue("Map new at: \"A\" equals: null;");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void addingElementsIsIdempotent() throws Exception {

      assertSSTrue("Map at: \"A\" :put: \"B\" at: \"A\" :put: \"D\" equals: (Map at: \"A\" :put: \"D\");");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void addingMappingToMap_returnsNullOrPreviousValue() throws Exception {

      assertSSTrue("""
            !map = Map new;
            map at: \"A\" :put: \"B\" :andGetPreviousValue equals: null;
            """);
      assertSSTrue("""
            !map = Map  at: \"A\" :put: \"X\";
            map at: \"A\" :put: \"B\" :andGetPreviousValue equals: \"X\";
            """);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void removingMapping_returnsNullOrRemovedValue() throws Exception {

      assertSSTrue("""
            !map = Map new;
            map removeAt: \"A\" :andGetRemovedValue equals: null;
            """);
      assertSSTrue("""
            !map = Map  at: \"A\" :put: \"X\";
            map removeAt: \"A\" :andGetRemovedValue equals: \"X\";
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
            } equals: items;
            """);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void elementsCanBeRemovedFromNonEptyMap() throws Exception {

      assertSSTrue("Map at: \"A\" :put: \"B\" at: \"C\" :put: \"D\" removeAt: \"C\" equals: (Map at: \"A\" :put: \"B\");");
      assertSSTrue("Map at: \"A\" :put: \"B\" removeAt: \"A\" equals: (Map new);");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void mepReturnsListOfKeayAsSet() throws Exception {

      assertSSTrue("""
            !keys = Map new keys;
            (keys nature equals: "set") and: (keys size equals: 0);
            """);
      assertSSTrue("""
            !keys = Map at: "a" :put: "b" keys;
            !list = List new;
            keys forEach: {!key | list append: key;};
            (keys nature equals: "set") and: (keys size equals: 1)
            and: (list at: 0 equals: "a");
            """);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Test
   public void builtInMethods_returnArgumentLists() throws Exception {

      assertSSTrue("""
            Map new method: "invoke::with:" arguments equals: (List append: "method" append: "argList");
            """);
      assertSSTrue("""
            Map new method: "addField:" arguments equals: (List append: "name");
            """);
      assertSSTrue("""
            Map new method: "addField::withValue:" arguments equals: (List append: "name" append: "value");
            """);
      assertSSTrue("""
            Map new method: "addMethod::using:" arguments equals: (List append: "name" append: "block");
            """);
      assertSSTrue("""
            Map new method: "asString" arguments equals: (List new);
            """);
      assertSSTrue("""
            Map new method: "at:" arguments equals: (List append: "key");
            """);
      assertSSTrue("""
            Map new method: "clone" arguments equals: (List new);
            """);
      assertSSTrue("""
            Map new method: "collectTo:" arguments equals: (List append: "collector");
            """);
      assertSSTrue("""
            Map new method: "equals:" arguments equals: (List append: "other");
            """);
      assertSSTrue("""
            Map new method: "execute" arguments equals: (List new);
            """);
      assertSSTrue("""
            Map new method: "fields" arguments equals: (List new);
            """);
      assertSSTrue("""
            Map new method: "forEach:" arguments equals: (List append: "block");
            """);
      assertSSTrue("""
            Map new method: "method:" arguments equals: (List append: "name");
            """);
      assertSSTrue("""
            Map new method: "methods" arguments equals: (List new);
            """);
      assertSSTrue("""
            Map new method: "nature" arguments equals: (List new);
            """);
      assertSSTrue("""
            Map new method: "hash" arguments equals: (List new);
            """);
      assertSSTrue("""
            Map new method: "isNotEqualTo:" arguments equals: (List append: "other");
            """);
      assertSSTrue("""
            Map new method: "orDefault:" arguments equals: (List append: "default");
            """);
      assertSSTrue("""
            Map new method: "size" arguments equals: (List new);
            """);
      assertSSTrue("""
            Map new method: "selectIf:" arguments equals: (List append: "block");
            """);
      assertSSTrue("""
            Map new method: "throw" arguments equals: (List new);
            """);
      assertSSTrue("""
            Map new method: "transformUsing:" arguments equals: (List append: "block");
            """);
      assertSSTrue("""
            Map new method: "try::catch:" arguments equals: (List append: "tryBlock" append: "catchBlock");
            """);
      
      assertSSTrue("""
            Map new method: "at::put:" arguments equals: (List append: "key" append: "value");
            """);
      assertSSTrue("""
            Map new method: "at::put::andGetPreviousValue" arguments equals: (List append: "key" append: "value");
            """);
      assertSSTrue("""
            Map new method: "removeAt:" arguments equals: (List append: "key");
            """);
      assertSSTrue("""
            Map new method: "removeAt::andGetRemovedValue" arguments equals: (List append: "key");
            """);
   }
   /****************************************************************************
    * 
    ****************************************************************************/
}
