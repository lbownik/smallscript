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
      addField(null, "nature", nature);
      addBinaryMethod("asDouble", SSDouble::asDouble);
      addBinaryMethod("asLong", SSDouble::asLong);
      addBinaryMethod("clone", SSDouble::clone);
      addBinaryMethod("dividedBy:", SSDouble::dividedBy);
      addBinaryMethod("isGreaterThan:", SSDouble::isGreaterThan);
      addBinaryMethod("isGreaterOrEqualTo:", SSDouble::isGreaterOrEqualTo);
      addBinaryMethod("isLessThan:", SSDouble::isLessThan);
      addBinaryMethod("isLessOrEqualTo:", SSDouble::isLessOrEqualTo);
      addBinaryMethod("minus:", SSDouble::minus);
      addBinaryMethod("multipliedBy:", SSDouble::multipliedBy);
      addBinaryMethod("plus:", SSDouble::plus);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private SSDouble(final Map<String, SSObject> methods,
         final Map<String, SSObject> fields, final double value) {

      super(methods, fields);
      this.value = value;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static double evaluateSecond(final List<SSObject> args,
         final Stack stack) {

      var first = args.get(1).evaluate(stack.pushNewFrame());

      if (first instanceof SSDouble d) {
         return d.value;
      } else if (first instanceof SSLong l) {
         return l.value;
      } else {
         final var n = first.invoke(stack.pushNewFrame(), "nature").toString();
         throwException(stack, first, "Cannot cast " + n + " to number.");
         return 0.0; //never happens
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject asDouble(final Stack stack, final List<SSObject> args) {

      return args.get(0);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject asLong(final Stack stack, final List<SSObject> args) {

      final var subject = (SSDouble) args.get(0);
      return new SSLong((long) subject.value);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject clone(final Stack stack, final List<SSObject> args) {

      final var subject = (SSDouble) args.get(0);
      return new SSDouble(subject.methods, subject.fields, subject.value);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject dividedBy(final Stack stack, final List<SSObject> args) {

      final var subject = (SSDouble) args.get(0);
      return new SSDouble(subject.value / evaluateSecond(args, stack));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject isGreaterThan(final Stack stack, final List<SSObject> args) {

      final var subject = (SSDouble) args.get(0);
      return stack.get(subject.value > evaluateSecond(args, stack));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject isGreaterOrEqualTo(final Stack stack, final List<SSObject> args) {

      final var subject = (SSDouble) args.get(0);
      return stack.get(subject.value >= evaluateSecond(args, stack));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject isLessThan(final Stack stack, final List<SSObject> args) {

      final var subject = (SSDouble) args.get(0);
      return stack.get(subject.value < evaluateSecond(args, stack));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject isLessOrEqualTo(final Stack stack, final List<SSObject> args) {

      final var subject = (SSDouble) args.get(0);
      return stack.get(subject.value <= evaluateSecond(args, stack));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject minus(final Stack stack, final List<SSObject> args) {

      final var subject = (SSDouble) args.get(0);
      return new SSDouble(subject.value - evaluateSecond(args, stack));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject multipliedBy(final Stack stack, final List<SSObject> args) {

      final var subject = (SSDouble) args.get(0);
      return new SSDouble(subject.value * evaluateSecond(args, stack));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject plus(final Stack stack, final List<SSObject> args) {

      final var subject = (SSDouble) args.get(0);
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
   
   private final static SSString nature = new SSString("number");
}
