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
//------------------------------------------------------------------------------
package ss.runtime;

import java.util.ArrayList;
import java.util.List;
/*******************************************************************************
 * @author lukasz.bownik@gmail.com {
 ******************************************************************************/
public final class SSList extends SSDynamicObject {
   /****************************************************************************
    * 
   ****************************************************************************/
   SSList(final Heap heap, final MethodMap methods) {

      super(heap, methods);
   }

   /****************************************************************************
    * 
   ****************************************************************************/
   static MethodMap putMethods(final Heap heap, final MethodMap methods) {

      final var listOfItem = List.of("item");
      final var listOfIndex = List.of("index");
      final var listOfBlock = List.of("block");
      final var listOfIndexItem = List.of("index", "item");

      methods.add("add:", heap.newBinaryBlock(SSList::append, listOfItem));
      methods.add("append:", heap.newBinaryBlock(SSList::append, listOfItem));
      methods.add("at:", heap.newBinaryBlock(SSList::at, listOfIndex));
      methods.add("at::put:", heap.newBinaryBlock(SSList::atPut, listOfIndexItem));
      methods.add("at::put::andReturnPreviousItem", heap
            .newBinaryBlock(SSList::atPutAndReturnPreviousItem, listOfIndexItem));
      methods.add("forEach:", heap.newBinaryBlock(SSList::forEach, listOfBlock));
      methods.add("nature", heap.newBinaryBlock((s, h, a) -> h.newString("list")));
      methods.add("removeAt:", heap.newBinaryBlock(SSList::removeAt, listOfIndex));
      methods.add("removeAt::andReturnRemovedItem",
            heap.newBinaryBlock(SSList::removeAtAndReturnRemovedItem, listOfIndex));
      methods.add("size", heap.newBinaryBlock(SSList::size));
      methods.add("selectIf:", heap.newBinaryBlock(SSList::selectIf, listOfBlock));
      methods.add("transformUsing:",
            heap.newBinaryBlock(SSList::transformUsing, listOfBlock));

      return methods;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static SSObject newFactory(final Heap heap) {

      final var result = heap.newObject();

      result.addMethod("new",
            heap.newBinaryBlock((s, h, a) -> h.newList()));
      result.addMethod("append:",
            heap.newBinaryBlock(SSList::appendToNew, List.of("item")));
      result.addMethod("addMethod::using:", heap
            .newBinaryBlock(SSList::addMethodToFactory, List.of("name", "block")));

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
               "Method 'List new' cannot be overriden.");
      } else if (name.equals("append:")) {
         return SSNativeObject.throwException(stack, heap, subject,
               "Method 'List append:' cannot be overriden.");
      } else {
         return SSNativeObject.addMethod(stack, heap, args);
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject append(final Stack stack, final Heap heap,
         final SSObject[] args) {

      final var subject = (SSList) args[0];
      subject.elements.add(args[1].evaluate(stack));
      return subject;
   }
   /*************************************************************************
    * 
   *************************************************************************/
   private static SSObject appendToNew(final Stack stack, final Heap heap,
         final SSObject[] args) {

      final var result = heap.newList();
      result.elements.add(args[1].evaluate(stack));
      return result;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static SSObject at(final Stack stack, final Heap heap, final SSObject[] args) {

      final var subject = (SSList) args[0];
      final var index = ((SSLong) args[1].evaluate(stack)).intValue();

      try {
         return subject.elements.get(index);
      } catch (final IndexOutOfBoundsException e) {
         return throwException(stack, heap, args[1],
               "Index " + index + " out of bounds.");
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject atPut(final Stack stack, final Heap heap,
         final SSObject[] args) {

      atPutAndReturnPreviousItem(stack, heap, args);
      return args[0];
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject atPutAndReturnPreviousItem(final Stack stack,
         final Heap heap, final SSObject[] args) {

      final var subject = (SSList) args[0];
      final var index = ((SSLong) args[1].evaluate(stack)).intValue();
      final var value = args[2].evaluate(stack);

      try {
         return subject.elements.set(index, value);
      } catch (final IndexOutOfBoundsException e) {
         return throwException(stack, heap, args[1],
               "Index " + index + " out of bounds.");
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject removeAt(final Stack stack, final Heap heap,
         final SSObject[] args) {

      removeAtAndReturnRemovedItem(stack, heap, args);
      return args[0];
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject removeAtAndReturnRemovedItem(final Stack stack,
         final Heap heap, final SSObject[] args) {

      final var subject = (SSList) args[0];
      final var index = ((SSLong) args[1].evaluate(stack)).intValue();

      try {
         return subject.elements.remove(index);
      } catch (final IndexOutOfBoundsException e) {
         return throwException(stack, heap, args[1],
               "Index " + index + " out of bounds.");
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static SSObject forEach(final Stack stack, final Heap heap,
         final SSObject[] args) {

      final var subject = (SSList) args[0];
      for (var item : subject.elements) {
         args[1].invoke(stack, "executeWith:",
               new SSObject[] { item.evaluate(stack) });
      }
      return subject;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject size(final Stack stack, final Heap heap,
         final SSObject[] args) {

      final var subject = (SSList) args[0];
      return heap.newLong(subject.elements.size());
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static SSObject selectIf(final Stack stack, final Heap heap,
         final SSObject[] args) {

      final var subject = (SSList) args[0];
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
   static SSObject transformUsing(final Stack stack, final Heap heap,
         final SSObject[] args) {

      final var subject = (SSList) args[0];
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

      return o instanceof SSList s && this.elements.equals(s.elements);
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
   final ArrayList<SSObject> elements = new ArrayList<>();
}
