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
import java.util.function.DoubleBinaryOperator;
import java.util.function.LongBinaryOperator;
import java.util.stream.LongStream;
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
      this.methods.put("asDouble", asDouble);
      this.methods.put("asLong", asLong);
      addBinaryMethod("clone", SSLong::clone);
      this.methods.put("incremented", incremented);
      this.methods.put("isGreaterThan:", isGreaterThan);
      this.methods.put("isGreaterOrEqualTo:", isGreaterOrEqualTo);
      this.methods.put("isLessThan:", isLessThan);
      this.methods.put("isLessOrEqualTo:", isLessOrEqualTo);
      this.methods.put("dividedBy:", dividedBy);
      this.methods.put("minus:", minus);
      this.methods.put("multipliedBy:", multipliedBy);
      this.methods.put("plus:", plus);
      this.methods.put("times:", times);
      this.methods.put("to:", to);
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
   private SSLong(final SSLong other) {

      super(other);
      this.value = other.value;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private SSObject calc(final Stack stack, final List<SSObject> args,
         final LongBinaryOperator opLong, final DoubleBinaryOperator opDouble) {

      final var arg = args.get(1).evaluate(stack);

      if (arg instanceof SSLong l) {
         return new SSLong(opLong.applyAsLong(this.value, l.value));
      } else if (arg instanceof SSDouble d) {
         return new SSDouble(opDouble.applyAsDouble(this.value, d.value));
      } else {
         final var n = arg.invoke(stack, "nature").toString();
         throwException(stack, arg, "Cannot cast " + n + " to number.");
         return stack.getNull(); // never happens
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static long evaluateSecond(final List<SSObject> args, final Stack stack) {

      return ((SSLong) args.get(1).evaluate(stack)).value;
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

      return new SSLong((SSLong) args.get(0));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject incremented(final Stack stack,
         final List<SSObject> args) {

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
   private static SSObject times(final Stack stack, final List<SSObject> args) {

      var value = ((SSLong) args.get(0)).value;
      final var block = args.get(1);
      SSObject result = stack.getNull();
      
      while(value-- > 0) {
         result = block.execute(stack);
      }
      return result;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject to(final Stack stack, final List<SSObject> args) {

      final var startInclusive = ((SSLong) args.get(0)).value;
      final var endExclusive = ((SSLong) args.get(1)).value;
      return new SSStream(
            LongStream.range(startInclusive, endExclusive).mapToObj(SSLong::new));
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
   
   private final static SSBinaryBlock asDouble = new SSBinaryBlock(
         SSLong::asDouble);
   private final static SSBinaryBlock asLong = new SSBinaryBlock(
         SSLong::asLong);
   private final static SSBinaryBlock incremented = new SSBinaryBlock(
         SSLong::incremented);
   private final static SSBinaryBlock isGreaterThan = new SSBinaryBlock(
         SSLong::isGreaterThan);
   private final static SSBinaryBlock isGreaterOrEqualTo = new SSBinaryBlock(
         SSLong::isGreaterOrEqualTo);
   private final static SSBinaryBlock isLessThan = new SSBinaryBlock(
         SSLong::isLessThan);
   private final static SSBinaryBlock isLessOrEqualTo = new SSBinaryBlock(
         SSLong::isLessOrEqualTo);
   private final static SSBinaryBlock dividedBy = new SSBinaryBlock(
         SSLong::dividedBy);
   private final static SSBinaryBlock minus = new SSBinaryBlock(
         SSLong::minus);
   private final static SSBinaryBlock multipliedBy = new SSBinaryBlock(
         SSLong::multipliedBy);
   private final static SSBinaryBlock plus = new SSBinaryBlock(
         SSLong::plus);
   private final static SSBinaryBlock times = new SSBinaryBlock(
         SSLong::times);
   private final static SSBinaryBlock to = new SSBinaryBlock(
         SSLong::to);
}
