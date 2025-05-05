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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
/*******************************************************************************
 * @author lukasz.bownik@gmail.com {
 ******************************************************************************/
public final class SSSet extends SSDynamicObject {
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSSet() {

      this(new HashSet<>());
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSSet(final Set<SSObject> elements) {

      super(sharedMethods);
      this.elements = new HashSet<>(elements);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static MethodMap putMethods(final MethodMap methods) {

      final var listOfItem = List.of("item");
      final var listOfBlock = List.of("block");

      methods.add("add:", bb(SSSet::append, listOfItem));
      methods.add("append:", bb(SSSet::append, listOfItem));
      methods.add("forEach:", bb(SSSet::forEach, listOfBlock));
      methods.add("nature", bb((s, a) -> nature));
      methods.add("remove:", bb(SSSet::remove, listOfItem));
      methods.add("size", bb(SSSet::size));
      methods.add("selectIf:", bb(SSSet::selectIf, listOfBlock));
      methods.add("transformUsing:", bb(SSSet::transformUsing, listOfBlock));

      return methods;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject append(final Stack stack, final SSObject[] args) {

      final var subject = (SSSet) args[0];
      subject.elements.add(args[1].evaluate(stack));
      return subject;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject remove(final Stack stack, final SSObject[] args) {

      final var subject = (SSSet) args[0];

      subject.elements.remove(args[1].evaluate(stack));
      return subject;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static SSObject forEach(final Stack stack, final SSObject[] args) {

      final var subject = (SSSet) args[0];
      for (var item : subject.elements) {
         args[1].invoke(stack, "executeWith:",
               new SSObject[] { item.evaluate(stack) });
      }
      return subject;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject size(final Stack stack, final SSObject[] args) {

      final var subject = (SSSet) args[0];
      return new SSLong(subject.elements.size());
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static SSObject selectIf(final Stack stack, final SSObject[] args) {

      final var subject = (SSSet) args[0];
      final var newStream = subject.elements.stream().filter(item -> {
         var result = args[1].invoke(stack, "executeWith:",
               new SSObject[] { item.evaluate(stack) });
         return result == stack.getTrue();
      });
      return new SSStream(newStream);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static SSObject transformUsing(final Stack stack, final SSObject[] args) {

      final var subject = (SSSet) args[0];
      final var newStream = subject.elements.stream().map(item -> args[1]
            .invoke(stack, "executeWith:", new SSObject[] { item.evaluate(stack) }));
      return new SSStream(newStream);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public String toString() {

      return this.elements.toString();
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public int hashCode() {

      return this.elements.hashCode();
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   public boolean equals(final Object o) {

      return o instanceof SSSet s && this.elements.equals(s.elements);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private final HashSet<SSObject> elements;

   final static MethodMap sharedMethods = putMethods(
         new MethodMap(SSDynamicObject.sharedMethods, true));
   private final static SSString nature = new SSString("set");
   /****************************************************************************
    * 
    ***************************************************************************/
   public final static class Factory extends SSDynamicObject {
      /*************************************************************************
       * 
      *************************************************************************/
      public Factory() {

         addMethod("new", bb((s, a) -> new SSSet()));
         addMethod("append:", bb(SSSet.Factory::append, List.of("item")));
      }
      /*************************************************************************
       * 
      *************************************************************************/
      private static SSObject append(final Stack stack, final SSObject[] args) {

         final var result = new SSSet();
         result.elements.add(args[1].evaluate(stack));
         return result;
      }
   }
}
