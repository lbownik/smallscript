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
import static ss.runtime.SSBinaryBlock.bb;
/*******************************************************************************
 * @author lukasz.bownik@gmail.com {
 ******************************************************************************/
public final class SSLong extends SSDynamicObject {
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSLong(final long value) {

      super(new MethodMap(sharedMethods));
      this.value = value;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static Methods putMethods(final Methods methods) {

      final List<String> listOfNumber = List.of("number");
      
      methods.add("asDouble", bb(SSLong::asDouble));
      methods.add("asLong", bb((SSLong::asLong)));
      methods.add("clone", bb(SSLong::clone));
      methods.add("incremented", bb(SSLong::incremented));
      methods.add("isGreaterThan:", bb(SSLong::isGreaterThan, listOfNumber));
      methods.add("isGreaterOrEqualTo:", bb(SSLong::isGreaterOrEqualTo, listOfNumber));
      methods.add("isLessThan:", bb(SSLong::isLessThan, listOfNumber));
      methods.add("isLessOrEqualTo:", bb(SSLong::isLessOrEqualTo, listOfNumber));
      methods.add("dividedBy:", bb(SSLong::dividedBy, listOfNumber));
      methods.add("minus:", bb(SSLong::minus, listOfNumber));
      methods.add("multipliedBy:", bb(SSLong::multipliedBy, listOfNumber));
      methods.add("nature", bb((s, a) -> nature));
      methods.add("plus:", bb(SSLong::plus, listOfNumber));
      methods.add("times:", bb(SSLong::times, List.of("block")));
      methods.add("to:", bb(SSLong::to, listOfNumber));

      return methods;
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
   @Override
   protected void addMethod(final String name, final SSObject block) {

      if(this.methods == SSLong.sharedMethods) {
         this.methods = new MethodMap(SSLong.sharedMethods);
      }
      this.methods.add(name, block);
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

      while (value-- > 0) {
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
   final static Methods sharedMethods = putMethods(
         new MethodMap(SSDynamicObject.sharedMethods));
}
