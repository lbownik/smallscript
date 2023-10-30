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
      addField(null, "nature", nature);
      addBinaryMethod("asDouble", SSLong::asDouble);
      addBinaryMethod("asLong", SSLong::asLong);
      addBinaryMethod("clone", SSLong::clone);
      addBinaryMethod("incremented", SSLong::incremented);
      addBinaryMethod("isGreaterThan:", SSLong::isGreaterThan);
      addBinaryMethod("isGreaterOrEqualTo:", SSLong::isGreaterOrEqualTo);
      addBinaryMethod("isLessThan:", SSLong::isLessThan);
      addBinaryMethod("isLessOrEqualTo:", SSLong::isLessOrEqualTo);
      addBinaryMethod("dividedBy:", SSLong::dividedBy);
      addBinaryMethod("minus:", SSLong::minus);
      addBinaryMethod("multipliedBy:", SSLong::multipliedBy);
      addBinaryMethod("plus:", SSLong::plus);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSLong(final int value) {

      this((long) value);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private SSLong(final Map<String, SSObject> methods,
         final Map<String, SSObject> fields, final long value) {

      super(methods, fields);
      this.value = value;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private SSObject calc(final Stack stack, final List<SSObject> args,
         final LongBinaryOperator opLong, final DoubleBinaryOperator opDouble) {

      final var arg = args.get(1).evaluate(stack.pushNewFrame());

      if (arg instanceof SSLong l) {
         return new SSLong(opLong.applyAsLong(this.value, l.value));
      } else if (arg instanceof SSDouble d) {
         return new SSDouble(opDouble.applyAsDouble(this.value, d.value));
      } else {
         final var n = arg.invoke(stack.pushNewFrame(), "nature").toString();
         throwException(stack, arg, "Cannot cast " + n + " to number.");
         return stack.getNull(); // never happens
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static long evaluateSecond(final List<SSObject> args, final Stack stack) {

      return ((SSLong) args.get(1).evaluate(stack.pushNewFrame())).value;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject asDouble(final Stack stack, final List<SSObject> args) {

      final var subject = (SSLong) args.get(0);
      return new SSDouble((double) subject.value);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject clone(final Stack stack, final List<SSObject> args) {

      final var subject = (SSLong) args.get(0);
      return new SSLong(subject.methods, subject.fields, subject.value);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject incremented(final Stack stack, final List<SSObject> args) {

      final var subject = (SSLong) args.get(0);
      return new SSLong(subject.value + 1);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject asLong(final Stack stack, final List<SSObject> args) {

      return args.get(0);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject dividedBy(final Stack stack, final List<SSObject> args) {

      final var subject = (SSLong) args.get(0);
      return subject.calc(stack, args, (x, y) -> x / y, (x, y) -> x / y);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject isGreaterThan(final Stack stack,
         final List<SSObject> args) {

      final var subject = (SSLong) args.get(0);
      return stack.get(subject.value > evaluateSecond(args, stack));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject isGreaterOrEqualTo(final Stack stack,
         final List<SSObject> args) {

      final var subject = (SSLong) args.get(0);
      return stack.get(subject.value >= evaluateSecond(args, stack));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject isLessThan(final Stack stack, final List<SSObject> args) {

      final var subject = (SSLong) args.get(0);
      return stack.get(subject.value < evaluateSecond(args, stack));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject isLessOrEqualTo(final Stack stack,
         final List<SSObject> args) {

      final var subject = (SSLong) args.get(0);
      return stack.get(subject.value <= evaluateSecond(args, stack));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject minus(final Stack stack, final List<SSObject> args) {

      final var subject = (SSLong) args.get(0);
      return subject.calc(stack, args, (x, y) -> x - y, (x, y) -> x - y);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject multipliedBy(final Stack stack,
         final List<SSObject> args) {

      final var subject = (SSLong) args.get(0);
      return subject.calc(stack, args, (x, y) -> x * y, (x, y) -> x * y);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject plus(final Stack stack, final List<SSObject> args) {

      final var subject = (SSLong) args.get(0);
      return subject.calc(stack, args, (x, y) -> x + y, (x, y) -> x + y);
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

   private final static SSString nature = new SSString("number");
}
