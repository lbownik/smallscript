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

import static ss.runtime.SSBinaryBlock.bb;

import java.util.List;
/*******************************************************************************
 * @author lukasz.bownik@gmail.com {
 ******************************************************************************/
public final class SSDouble extends SSDynamicObject {
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSDouble(final double value) {

      super(sharedMethods);
      this.value = value;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private SSDouble(final SSDouble other) {

      super(other);
      this.value = other.value;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static MethodMap putMethods(final MethodMap methods) {

      final List<String> listOfNumber = List.of("number");
      
      methods.add("asDouble", bb(SSDouble::asDouble));
      methods.add("asLong", bb(SSDouble::asLong));
      methods.add("clone", bb(SSDouble::clone));
      methods.add("dividedBy:", bb(SSDouble::dividedBy, listOfNumber));
      methods.add("isGreaterThan:", bb(SSDouble::isGreaterThan, listOfNumber));
      methods.add("isGreaterOrEqualTo:", bb(SSDouble::isGreaterOrEqualTo, listOfNumber));
      methods.add("isLessThan:", bb(SSDouble::isLessThan, listOfNumber));
      methods.add("isLessOrEqualTo:", bb(SSDouble::isLessOrEqualTo, listOfNumber));
      methods.add("minus:", bb(SSDouble::minus, listOfNumber));
      methods.add("multipliedBy:", bb(SSDouble::multipliedBy, listOfNumber));
      methods.add("nature", bb((s, a) -> nature));
      methods.add("plus:", bb(SSDouble::plus, listOfNumber));
      
      return methods;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static double evaluateSecond(final SSObject[] args,
         final Stack stack) {

      var first = args[1].evaluate(stack);

      if (first instanceof SSDouble d) {
         return d.value;
      } else if (first instanceof SSLong l) {
         return l.value;
      } else {
         final var n = first.invoke(stack, "nature").toString();
         throwException(stack, first, "Cannot cast " + n + " to number.");
         return 0.0; // never happens
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject asDouble(final Stack stack, final SSObject[] args) {

      return args[0];
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject asLong(final Stack stack, final SSObject[] args) {

      final var subject = (SSDouble) args[0];
      return new SSLong((long) subject.value);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject clone(final Stack stack, final SSObject[] args) {

      return new SSDouble((SSDouble) args[0]);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject dividedBy(final Stack stack, final SSObject[] args) {

      final var subject = (SSDouble) args[0];
      return new SSDouble(subject.value / evaluateSecond(args, stack));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject isGreaterThan(final Stack stack,
         final SSObject[] args) {

      final var subject = (SSDouble) args[0];
      return stack.get(subject.value > evaluateSecond(args, stack));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject isGreaterOrEqualTo(final Stack stack,
         final SSObject[] args) {

      final var subject = (SSDouble) args[0];
      return stack.get(subject.value >= evaluateSecond(args, stack));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject isLessThan(final Stack stack, final SSObject[] args) {

      final var subject = (SSDouble) args[0];
      return stack.get(subject.value < evaluateSecond(args, stack));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject isLessOrEqualTo(final Stack stack,
         final SSObject[] args) {

      final var subject = (SSDouble) args[0];
      return stack.get(subject.value <= evaluateSecond(args, stack));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject minus(final Stack stack, final SSObject[] args) {

      final var subject = (SSDouble) args[0];
      return new SSDouble(subject.value - evaluateSecond(args, stack));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject multipliedBy(final Stack stack,
         final SSObject[] args) {

      final var subject = (SSDouble) args[0];
      return new SSDouble(subject.value * evaluateSecond(args, stack));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject plus(final Stack stack, final SSObject[] args) {

      final var subject = (SSDouble) args[0];
      return new SSDouble(subject.value + evaluateSecond(args, stack));
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

   final static MethodMap sharedMethods = putMethods(
         new MethodMap(SSDynamicObject.sharedMethods, true));
   private final static SSString nature = new SSString("number");
}
