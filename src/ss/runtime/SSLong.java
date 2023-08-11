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
import java.util.function.DoubleBinaryOperator;
import java.util.function.LongBinaryOperator;
/*******************************************************************************
 * @author lukasz.bownik@gmail.com {
 ******************************************************************************/
public final class SSLong extends SSDynamicObject {
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSLong(final long value) {

      this.value = value;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSLong(final int value) {

      this.value = value;
   }
//   /****************************************************************************
//    * 
//   ****************************************************************************/
//   private SSLong(final Map<String, SSObject> methods,
//         final Map<String, SSObject> fields, final long value) {
//
//      super(methods, fields);
//      this.value = value;
//   }
//   /****************************************************************************
//    * 
//   ****************************************************************************/
//   protected SSObject doClone() {
//
//      return new SSLong(this.methods, this.fields, this.value);
//   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public SSObject invoke(final Stack stack, final String method,
         final List<SSObject> args) {

      return switch (method) {
         case "plus:" -> calc(stack, args, (x, y) -> x + y, (x, y) -> x + y);
         case "minus:" -> calc(stack, args, (x, y) -> x - y, (x, y) -> x - y);
         case "multipliedBy:" -> calc(stack, args, (x, y) -> x * y, (x, y) -> x * y);
         case "dividedBy:" -> calc(stack, args, (x, y) -> x / y, (x, y) -> x / y);
         case "asDouble" -> new SSDouble(this.value);
         case "asLong" -> this;
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
   private SSObject calc(final Stack stack, final List<SSObject> args,
          final LongBinaryOperator opLong, final DoubleBinaryOperator opDouble) {

      final var arg = args.get(0).evaluate(stack.pushNewFrame());

      if (arg instanceof SSLong l) {
         return new SSLong(opLong.applyAsLong(this.value, l.value));
      } else if (arg instanceof SSDouble d) {
         return new SSDouble(opDouble.applyAsDouble(this.value, d.value));
      } else {
         throw new RuntimeException(
               "Cannot cast " + arg.getClass() + " to SSLong or SSDouble");
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static long evaluateFirst(final List<SSObject> args, final Stack stack) {

      return ((SSLong) args.get(0).evaluate(stack.pushNewFrame())).value;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public String toString() {

      return Long.toString(this.value);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public int hashCode() {

      return Long.hashCode(this.value);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public int intValue() {

      return (int) this.value;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public boolean equals(final Object o) {

      return o instanceof SSLong l && this.value == l.value;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public final long value;
}
