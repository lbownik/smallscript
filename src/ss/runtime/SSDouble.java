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
import java.util.Map;
/*******************************************************************************
 * @author lukasz.bownik@gmail.com {
 ******************************************************************************/
public final class SSDouble extends SSDynamicObject {
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSDouble(final double value) {

      this.value = value;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private SSDouble(final Map<String, SSBlock> methods,
         final Map<String, SSObject> fields, final double value) {

      super(methods, fields);
      this.value = value;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   protected SSObject doClone() {

      return new SSDouble(this.methods, this.fields, this.value);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public SSObject invoke(final Stack stack, final String method,
         final List<SSObject> args) {

      return switch (method) {
         case "plus:" -> new SSDouble(this.value + evaluateFirst(args, stack));
         case "minus:" -> new SSDouble(this.value - evaluateFirst(args, stack));
         case "multipliedBy:" ->
            new SSDouble(this.value * evaluateFirst(args, stack));
         case "dividedBy:" -> new SSDouble(this.value / evaluateFirst(args, stack));
         case "asLong" -> new SSLong((long)this.value);
         case "asDouble" -> this;
         case "isGreaterThan:" -> stack.get(this.value > evaluateFirst(args, stack));
         case "isLessThan:" -> stack.get(this.value < evaluateFirst(args, stack));
         case "isGreaterOrEqualTo:" ->
            stack.get(this.value >= evaluateFirst(args, stack));
         case "isLessOrEqualTo:" ->
            stack.get(this.value <= evaluateFirst(args, stack));
         default -> super.invoke(stack, method, args);
      };
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static double evaluateFirst(final List<SSObject> args,
         final Stack stack) {

      var first = args.get(0).evaluate(stack.pushNewFrame());

      if (first instanceof SSDouble d) {
         return d.value;
      } else if (first instanceof SSLong l) {
         return l.value;
      } else {
         throw new RuntimeException(
               "Cannot cast " + first.getClass() + " to SSDouble.");
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public String toString() {

      return Double.toString(this.value);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public int hashCode() {

      return Double.hashCode(this.value);
   }

   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public boolean equals(final Object o) {

      return o instanceof SSDouble d && Double.compare(this.value, d.value) == 0;

   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public final double value;
}
