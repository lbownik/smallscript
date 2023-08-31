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

      assertSSTrue("Map at: 'A' :put: 'B' equals: (Map at: 'A' :put: 'B');");
      assertSSFalse(
            "Map at: 'A' :put: 'B' equals: (Map at: 'A' :put: 'B' at: 'C' :put: 'D');");
      assertSSTrue("Map at: 'A' :put: 'B' isNotEqualTo: (Map at: 'A' :put: 'B' at: 'C' :put: 'D');");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void executingMapDoesNothingAndReturnNull() throws Exception {

      assertSSTrue("Map execute equals: Map;");
      assertSSTrue("Map new execute equals: (Map new);");
      assertSSTrue("Map at: 'A' :put: 'B' execute equals: (Map at: 'A' :put: 'B');");
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

      assertSSTrue("Map at: 'A' :put: 'B' size equals: 1;");
      assertSSTrue("Map at: 'A' :put: 'B' at: 'C' :put: 'D' size equals: 2;");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void addingElementsIsIdempotent() throws Exception {

      assertSSTrue("Map at: 'A' :put: 'B' at: 'A' :put: 'D' equals: (Map at: 'A' :put: 'D');");
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void forEachPerformsActionForEachElement() throws Exception {

      assertSSTrue("""
            !items = Map new;
            Map at: 'A' :put: 'B' at: 'A' :put: 'D' forEach: {!key !value |
               items at: key :put: value;
            } equals: items;
            """);
   }
   /****************************************************************************
    * 
    ***************************************************************************/
   @Test
   public void elementsCanBeRemovedFromNonEptyMap() throws Exception {

      assertSSTrue("Map at: 'A' :put: 'B' at: 'C' :put: 'D' removeAt: 'C' equals: (Map at: 'A' :put: 'B');");
      assertSSTrue("Map at: 'A' :put: 'B' removeAt: 'A' equals: (Map new);");
   }
   /****************************************************************************
    * 
    ****************************************************************************/
}
