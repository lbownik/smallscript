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
public final class SSString extends SSDynamicObject {
   /****************************************************************************
    * 
   ****************************************************************************/
   static {

      var n = new SSString("string", true);
      n.addField("nature", n);
      nature = n;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSString(final String value) {

      this(value, true);
      addField("nature", nature);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private SSString(final String value, boolean noNature) {

      this.value = value;
      addBinaryMethod("append:", SSString::concatenate);
      addBinaryMethod("at:", SSString::at);
      addBinaryMethod("clone", SSString::clone);
      addBinaryMethod("concatenate:", SSString::concatenate);
      addBinaryMethod("size", SSString::size);
      addBinaryMethod("startsWith:", SSString::startsWith);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private SSString(final Map<String, SSObject> methods,
         final Map<String, SSObject> fields, final String value) {

      super(methods, fields);
      this.value = value;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject at(final Stack stack, final List<SSObject> args) {

      final var subject = (SSString) args.get(0);
      final var index = ((SSLong) args.get(1)).intValue();
      if (index > -1 & index < subject.value.length()) {
         return new SSChar(subject.value.charAt(index));
      } else {
         return throwException(args.get(1), "Index " + index + " out of bounds.");
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject clone(final Stack stack, final List<SSObject> args) {

      final var subject = (SSString) args.get(0);
      return new SSString(subject.methods, subject.fields, subject.value);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject concatenate(final Stack stack,
         final List<SSObject> args) {

      final var subject = (SSString) args.get(0);
      return new SSString(subject.value
            .concat(args.get(1).evaluate(stack.pushNewFrame()).toString()));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject size(final Stack stack, final List<SSObject> args) {

      final var subject = (SSString) args.get(0);
      return new SSLong(subject.value.length());
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject startsWith(final Stack stack, final List<SSObject> args) {

      final var subject = (SSString) args.get(0);
      return stack.get(subject.value.startsWith(args.get(1).toString()));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public String toString() {

      return this.value;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public int hashCode() {

      return this.value.hashCode();
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public boolean equals(final Object o) {

      return o instanceof SSString s && this.value.equals(s.value);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private final String value;

   private final static SSString nature;
}
