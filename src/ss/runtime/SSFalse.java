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
public final class SSFalse extends SSDynamicObject {
   /****************************************************************************
    * 
   ****************************************************************************/
   protected SSObject doClone() {

      return this;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public SSObject invoke(final Stack stack, final String method,
         final List<SSObject> args) {

      return switch (method) {
         case "not" -> stack.getTrue();
         case "and:" -> this;
         case "or:", "xor:" -> args.get(0).evaluate(stack.pushNewFrame());
         case "size" -> new SSLong(1);
         case "at:" -> this;
         case "ifTrue:" -> stack.getNull();
         case "ifFalse:" -> args.get(0).evaluate(stack.pushNewFrame());
         case "ifTrue::ifFalse:" -> args.get(1).evaluate(stack.pushNewFrame());
         default -> super.invoke(stack, method, args);
      };
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public String toString() {

      return name;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public int hashCode() {

      return 1;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public boolean equals(final Object obj) {

      return getClass().equals(obj.getClass());
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public final static String name = "false";
}
