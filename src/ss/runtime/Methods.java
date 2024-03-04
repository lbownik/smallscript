//------------------------------------------------------------------------------
//Copyright 2023 Lukasz Bownik
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

import static java.util.Collections.emptySet;
import java.util.Set;

/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
interface Methods {

   /****************************************************************************
    * 
   ****************************************************************************/
   default SSObject get(final String key) {

      return null;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   default SSObject getOrDefault(final String key, final SSObject defaultValue) {

      return defaultValue;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   default void add(final String key, final SSObject value) {

      throw new UnsupportedOperationException();
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   default Set<String> keySet() {

      return emptySet();
   }

   /****************************************************************************
    * 
   ****************************************************************************/
   public final static Methods empty = new Methods() {
   };
}
