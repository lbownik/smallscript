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
package ss.runtime;

import java.util.List;
/*******************************************************************************
 * @author lukasz.bownik@gmail.com {
 ******************************************************************************/
public final class SSString extends SSDynamicObject {
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSString(final String value) {

      this.value = value;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public SSObject invoke(final Stack stack, final String method,
         final List<SSObject> args) {

      return switch (method) {
         case "clone" -> new SSString(this.value);
         case "concatenate:" -> new SSString(this.value.concat(
               ((SSString) args.get(0).evaluate(stack.pushNewFrame())).value));
         case "size" -> new SSLong(this.value.length());
         case "at:" ->
            new SSChar(this.value.charAt(((SSLong) args.get(0)).intValue()));
         case "startsWith:" ->
            stack.get(this.value.startsWith(args.get(0).toString()));
         default -> super.invoke(stack, method, args);
      };
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public String toString() {

      return this.value;
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

      return o instanceof SSString s && this.value.equals(s.value);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private final String value;
}
