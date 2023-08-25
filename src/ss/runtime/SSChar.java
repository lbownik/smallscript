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
public final class SSChar extends SSDynamicObject {
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSChar(final char value) {

      this.value = value;
      addBinaryMethod("clone", SSChar::clone);
      addBinaryMethod("isGreaterOrEqualTo:", SSChar::isGreaterThanOrEqualTo);
      addBinaryMethod("isGreaterThan:", SSChar::isGreaterThan);
      addBinaryMethod("isLessOrEqualTo:", SSChar::isLessOrEqualTo);
      addBinaryMethod("isLessThan:", SSChar::isLessThan);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private SSChar(final Map<String, SSObject> methods,
         final Map<String, SSObject> fields, final char value) {

      super(methods, fields);
      this.value = value;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject clone(final Stack stack, final List<SSObject> args) {

      final var subject = (SSChar) args.get(0);
      return new SSChar(subject.methods, subject.fields, subject.value);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static char evaluateSecond(final Stack stack, final List<SSObject> args) {

      return ((SSChar) args.get(1).evaluate(stack.pushNewFrame())).value;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public String toString() {

      return Character.toString(this.value);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public int hashCode() {

      return Character.hashCode(this.value);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public boolean equals(final Object o) {

      return getClass() == o.getClass() && this.value == ((SSChar) o).value;

   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject isGreaterThan(final Stack stack,
         final List<SSObject> args) {

      final var subject = (SSChar) args.get(0);
      return stack.get(subject.value > evaluateSecond(stack, args));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject isGreaterThanOrEqualTo(final Stack stack,
         final List<SSObject> args) {

      final var subject = (SSChar) args.get(0);
      return stack.get(subject.value >= evaluateSecond(stack, args));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject isLessThan(final Stack stack, final List<SSObject> args) {

      final var subject = (SSChar) args.get(0);
      return stack.get(subject.value < evaluateSecond(stack, args));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject isLessOrEqualTo(final Stack stack,
         final List<SSObject> args) {

      final var subject = (SSChar) args.get(0);
      return stack.get(subject.value <= evaluateSecond(stack, args));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private final char value;
}
