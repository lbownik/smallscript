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

      super(new MethodMap(sharedMethods));
      this.value = value;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static Methods putMethods(final Methods methods) {

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
   @Override
   protected void addMethod(final String name, final SSObject block) {

      if(this.methods == SSString.sharedMethods) {
         this.methods = new MethodMap(SSString.sharedMethods);
      }
      this.methods.add(name, block);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject at(final Stack stack, final List<SSObject> args) {

      final var subject = (SSString) args.get(0);
      final var index = ((SSLong) args.get(1)).intValue();
      if (index > -1 & index < subject.value.length()) {
         return new SSString(subject.value.substring(index, index + 1));
      } else {
         return throwException(stack, args.get(1), "Index " + index + " out of bounds.");
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject clone(final Stack stack, final List<SSObject> args) {

      return new SSString((SSString) args.get(0));
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject concatenate(final Stack stack,
         final List<SSObject> args) {

      final var subject = (SSString) args.get(0);
      return new SSString(subject.value
            .concat(args.get(1).evaluate(stack).toString()));
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

   final static Methods sharedMethods = putMethods(
         new MethodMap(SSDynamicObject.sharedMethods));
   private final static SSString nature = new SSString("string");
}
