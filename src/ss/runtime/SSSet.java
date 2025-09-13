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

import java.util.HashSet;
import java.util.List;
/*******************************************************************************
 * @author lukasz.bownik@gmail.com {
 ******************************************************************************/
public final class SSSet extends SSDynamicObject {
   /****************************************************************************
    * 
   ****************************************************************************/
   SSSet(final Heap heap, final MethodMap methods) {

      super(heap, methods);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static MethodMap putMethods(final Heap heap, final MethodMap methods) {

      final var listOfItem = List.of("item");
      final var listOfBlock = List.of("block");

      methods.add("add:", heap.newBinaryBlock(SSSet::append, listOfItem));
      methods.add("append:", heap.newBinaryBlock(SSSet::append, listOfItem));
      methods.add("forEach:", heap.newBinaryBlock(SSSet::forEach, listOfBlock));
      methods.add("nature", heap.newBinaryBlock((s, h, a) -> h.newString("set")));
      methods.add("remove:", heap.newBinaryBlock(SSSet::remove, listOfItem));
      methods.add("size", heap.newBinaryBlock(SSSet::size));
      methods.add("selectIf:", heap.newBinaryBlock(SSSet::selectIf, listOfBlock));
      methods.add("transformUsing:", heap.newBinaryBlock(SSSet::transformUsing, listOfBlock));

      return methods;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static SSObject newFactory(final Heap heap) {

      final var result = heap.newObject();
      
      result.addMethod("new", heap.newBinaryBlock((s, h, a) -> h.newSet()));
      result.addMethod("append:", heap.newBinaryBlock(SSSet::appendToNew, List.of("item")));
      result.addMethod("addMethod::using:", heap
            .newBinaryBlock(SSSet::addMethodToFactory, List.of("name", "block")));

      return result;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static SSObject addMethodToFactory(final Stack stack, final Heap heap,
         final SSObject[] args) {

      final var subject = (SSNativeObject) args[0];
      final var name = args[1].evaluate(stack).toString();

      if (name.equals("new")) {
         return SSNativeObject.throwException(stack, heap, subject,
               "Method 'Set new' cannot be overriden.");
      } else if (name.equals("append:")) {
         return SSNativeObject.throwException(stack, heap, subject,
               "Method 'Set append:' cannot be overriden.");
      } else {
         return SSNativeObject.addMethod(stack, heap, args);
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject append(final Stack stack, final Heap heap, final SSObject[] args) {

      final var subject = (SSSet) args[0];
      subject.elements.add(args[1].evaluate(stack));
      return subject;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject appendToNew(final Stack stack, final Heap heap, final SSObject[] args) {

      final var result = heap.newSet();
      result.elements.add(args[1].evaluate(stack));
      return result;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject remove(final Stack stack, final Heap heap, final SSObject[] args) {

      final var subject = (SSSet) args[0];

      subject.elements.remove(args[1].evaluate(stack));
      return subject;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static SSObject forEach(final Stack stack, final Heap heap, final SSObject[] args) {

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
   private static SSObject size(final Stack stack, final Heap heap, final SSObject[] args) {

      final var subject = (SSSet) args[0];
      return heap.newLong(subject.elements.size());
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static SSObject selectIf(final Stack stack, final Heap heap, final SSObject[] args) {

      final var subject = (SSSet) args[0];
      final var newStream = subject.elements.stream().filter(item -> {
         var result = args[1].invoke(stack, "executeWith:",
               new SSObject[] { item.evaluate(stack) });
         return result == stack.getTrue();
      });
      return heap.newStream(newStream);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static SSObject transformUsing(final Stack stack, final Heap heap, final SSObject[] args) {

      final var subject = (SSSet) args[0];
      final var newStream = subject.elements.stream().map(item -> args[1]
            .invoke(stack, "executeWith:", new SSObject[] { item.evaluate(stack) }));
      return heap.newStream(newStream);
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
   void add(final SSObject o) {
      
      this.elements.add(o);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private final HashSet<SSObject> elements = new HashSet<>();
}
