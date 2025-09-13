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
   SSLong(final Heap heap, final MethodMap methods, final long value) {

      super(heap, methods);
      this.value = value;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static MethodMap putMethods(final Heap heap, final MethodMap methods) {

      final List<String> listOfNumber = List.of("number");

      methods.add("asDouble", heap.newBinaryBlock(SSLong::asDouble));
      methods.add("asLong", heap.newBinaryBlock((SSLong::asLong)));
      methods.add("clone", heap.newBinaryBlock(SSLong::clone));
      methods.add("incremented", heap.newBinaryBlock(SSLong::incremented));
      methods.add("isGreaterThan:",
            heap.newBinaryBlock(SSLong::isGreaterThan, listOfNumber));
      methods.add("isGreaterOrEqualTo:",
            heap.newBinaryBlock(SSLong::isGreaterOrEqualTo, listOfNumber));
      methods.add("isLessThan:",
            heap.newBinaryBlock(SSLong::isLessThan, listOfNumber));
      methods.add("isLessOrEqualTo:",
            heap.newBinaryBlock(SSLong::isLessOrEqualTo, listOfNumber));
      methods.add("dividedBy:",
            heap.newBinaryBlock(SSLong::dividedBy, listOfNumber));
      methods.add("minus:", heap.newBinaryBlock(SSLong::minus, listOfNumber));
      methods.add("multipliedBy:",
            heap.newBinaryBlock(SSLong::multipliedBy, listOfNumber));
      methods.add("nature", heap.newBinaryBlock((s, h, a) -> h.newString("number")));
      methods.add("plus:", heap.newBinaryBlock(SSLong::plus, listOfNumber));
      methods.add("times:", heap.newBinaryBlock(SSLong::times, List.of("block")));
      methods.add("to:", heap.newBinaryBlock(SSLong::to, listOfNumber));

      return methods;
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
   private SSObject calc(final Stack stack, final Heap heap, final SSObject[] args,
         final LongBinaryOperator opLong, final DoubleBinaryOperator opDouble) {

      final var arg = args[1].evaluate(stack);

      if (arg instanceof SSLong l) {
         return heap.newLong(opLong.applyAsLong(this.value, l.value));
      } else if (arg instanceof SSDouble d) {
         return heap.newDouble(opDouble.applyAsDouble(this.value, d.value));
      } else {
         final var n = arg.invoke(stack, "nature").toString();
         throwException(stack, heap, arg, "Cannot cast " + n + " to number.");
         return stack.getNull(); // never happens
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static long evaluateSecond(final SSObject[] args, final Heap heap,
         final Stack stack) {

      return ((SSLong) args[1].evaluate(stack)).value;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject asDouble(final Stack stack, final Heap heap,
         final SSObject[] args) {

      final var subject = (SSLong) args[0];
      return heap.newDouble((double) subject.value);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject clone(final Stack stack, final Heap heap,
         final SSObject[] args) {

      return new SSLong((SSLong) args[0]);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject incremented(final Stack stack, final Heap heap,
         final SSObject[] args) {

      final var subject = (SSLong) args[0];
      return heap.newLong(subject.value + 1);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject asLong(final Stack stack, final Heap heap,
         final SSObject[] args) {

      return args[0];
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject dividedBy(final Stack stack, final Heap heap,
         final SSObject[] args) {

      final var subject = (SSLong) args[0];
      return subject.calc(stack, heap, args, (x, y) -> x / y, (x, y) -> x / y);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject isGreaterThan(final Stack stack, final Heap heap,
         final SSObject[] args) {

      final var subject = (SSLong) args[0];
      return stack.get(subject.value > evaluateSecond(args, heap, stack));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject isGreaterOrEqualTo(final Stack stack, final Heap heap,
         final SSObject[] args) {

      final var subject = (SSLong) args[0];
      return stack.get(subject.value >= evaluateSecond(args, heap, stack));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject isLessThan(final Stack stack, final Heap heap,
         final SSObject[] args) {

      final var subject = (SSLong) args[0];
      return stack.get(subject.value < evaluateSecond(args, heap, stack));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject isLessOrEqualTo(final Stack stack, final Heap heap,
         final SSObject[] args) {

      final var subject = (SSLong) args[0];
      return stack.get(subject.value <= evaluateSecond(args, heap, stack));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject minus(final Stack stack, final Heap heap,
         final SSObject[] args) {

      final var subject = (SSLong) args[0];
      return subject.calc(stack, heap, args, (x, y) -> x - y, (x, y) -> x - y);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject multipliedBy(final Stack stack, final Heap heap,
         final SSObject[] args) {

      final var subject = (SSLong) args[0];
      return subject.calc(stack, heap, args, (x, y) -> x * y, (x, y) -> x * y);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject plus(final Stack stack, final Heap heap,
         final SSObject[] args) {

      final var subject = (SSLong) args[0];
      return subject.calc(stack, heap, args, (x, y) -> x + y, (x, y) -> x + y);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject times(final Stack stack, final Heap heap,
         final SSObject[] args) {

      var value = ((SSLong) args[0]).value;
      final var block = args[1];
      SSObject result = stack.getNull();

      while (value-- > 0) {
         result = block.execute(stack);
      }
      return result;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject to(final Stack stack, final Heap heap,
         final SSObject[] args) {

      final var startInclusive = ((SSLong) args[0]).value;
      final var endExclusive = ((SSLong) args[1]).value;
      return heap.newStream(
            LongStream.range(startInclusive, endExclusive).mapToObj(heap::newLong));
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
