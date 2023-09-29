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
//-----------------------------------------------------------------------------
package ss.parser;

import ss.runtime.SSLong;
import ss.runtime.SSObject;
/*******************************************************************************
 * @author lukasz.bownik@gmail.com
 ******************************************************************************/
public final class LongConstant implements Expression {
   /****************************************************************************
    * 
   ****************************************************************************/
   public LongConstant(final Long value) {

      this.value = value;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public String toString() {

      return this.value.toString();
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public int hashCode() {

      return this.value.hashCode();
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public boolean equals(final Object o) {

      return o instanceof LongConstant l && this.value.equals(l.value);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSObject toSSObject() {

      return new SSLong(this.value);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private final Long value;
}
