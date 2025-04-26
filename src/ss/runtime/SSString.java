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
public final class SSString extends SSDynamicObject {

   /****************************************************************************
    * 
   ****************************************************************************/
   public SSString(final String value) {

      super(sharedMethods);
      this.value = value;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static MethodMap putMethods(final MethodMap methods) {

      methods.add("append:", bb(SSString::concatenate, List.of("text")));
      methods.add("at:", bb(SSString::at, List.of("index")));
      methods.add("clone", bb(SSString::clone));
      methods.add("concatenate:", bb(SSString::concatenate, List.of("text")));
      methods.add("nature", bb((s, a) -> nature));
      methods.add("size", bb(SSString::size));
      methods.add("startsWith:", bb(SSString::startsWith, List.of("text")));

      return methods;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private SSString(final SSString other) {

      super(other);
      this.value = other.value;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject at(final Stack stack, final SSObject[] args) {

      final var subject = (SSString) args[0];
      final var index = ((SSLong) args[1]).intValue();
      if (index > -1 & index < subject.value.length()) {
         return new SSString(subject.value.substring(index, index + 1));
      } else {
         return throwException(stack, args[1],
               "Index " + index + " out of bounds.");
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject clone(final Stack stack, final SSObject[] args) {

      return new SSString((SSString) args[0]);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject concatenate(final Stack stack,
         final SSObject[] args) {

      final var subject = (SSString) args[0];
      return new SSString(
            subject.value.concat(args[1].evaluate(stack).toString()));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject size(final Stack stack, final SSObject[] args) {

      final var subject = (SSString) args[0];
      return new SSLong(subject.value.length());
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject startsWith(final Stack stack, final SSObject[] args) {

      final var subject = (SSString) args[0];
      return stack.get(subject.value.startsWith(args[1].toString()));
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

   final static MethodMap sharedMethods = putMethods(
         new MethodMap(SSDynamicObject.sharedMethods, true));
   private final static SSString nature = new SSString("string");
}
