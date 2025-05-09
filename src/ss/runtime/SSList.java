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

import static ss.runtime.SSBinaryBlock.bb;

import java.util.ArrayList;
import java.util.List;
/*******************************************************************************
 * @author lukasz.bownik@gmail.com {
 ******************************************************************************/
public final class SSList extends SSDynamicObject {
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSList() {

      this(new ArrayList<>());
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSList(final List<? extends SSObject> elements) {

      super(sharedMethods);
      this.elements = new ArrayList<>(elements);
   }

   /****************************************************************************
    * 
   ****************************************************************************/
   static MethodMap putMethods(final MethodMap methods) {

      final var listOfItem = List.of("item");
      final var listOfIndex = List.of("index");
      final var listOfBlock = List.of("block");
      final var listOfIndexItem = List.of("index", "item");
      
      methods.add("add:", bb(SSList::append, listOfItem));
      methods.add("append:", bb(SSList::append, listOfItem));
      methods.add("at:", bb(SSList::at, listOfIndex));
      methods.add("at::put:", bb(SSList::atPut, listOfIndexItem));
      methods.add("at::put::andReturnPreviousItem",
            bb(SSList::atPutAndReturnPreviousItem, listOfIndexItem));
      methods.add("forEach:", bb(SSList::forEach, listOfBlock));
      methods.add("nature", bb((s, a) -> nature));
      methods.add("removeAt:", bb(SSList::removeAt, listOfIndex));
      methods.add("removeAt::andReturnRemovedItem",
            bb(SSList::removeAtAndReturnRemovedItem, listOfIndex));
      methods.add("size", bb(SSList::size));
      methods.add("selectIf:", bb(SSList::selectIf, listOfBlock));
      methods.add("transformUsing:", bb(SSList::transformUsing, listOfBlock));

      return methods;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject append(final Stack stack, final SSObject[] args) {

      final var subject = (SSList) args[0];
      subject.elements.add(args[1].evaluate(stack));
      return subject;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static SSObject at(final Stack stack, final SSObject[] args) {

      final var subject = (SSList) args[0];
      final var index = ((SSLong) args[1].evaluate(stack)).intValue();

      try {
         return subject.elements.get(index);
      } catch (final IndexOutOfBoundsException e) {
         return throwException(stack, args[1],
               "Index " + index + " out of bounds.");
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject atPut(final Stack stack, final SSObject[] args) {

      atPutAndReturnPreviousItem(stack, args);
      return args[0];
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject atPutAndReturnPreviousItem(final Stack stack,
         final SSObject[] args) {

      final var subject = (SSList) args[0];
      final var index = ((SSLong) args[1].evaluate(stack)).intValue();
      final var value = args[2].evaluate(stack);

      try {
         return subject.elements.set(index, value);
      } catch (final IndexOutOfBoundsException e) {
         return throwException(stack, args[1],
               "Index " + index + " out of bounds.");
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject removeAt(final Stack stack, final SSObject[] args) {

      removeAtAndReturnRemovedItem(stack, args);
      return args[0];
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject removeAtAndReturnRemovedItem(final Stack stack,
         final SSObject[] args) {

      final var subject = (SSList) args[0];
      final var index = ((SSLong) args[1].evaluate(stack)).intValue();

      try {
         return subject.elements.remove(index);
      } catch (final IndexOutOfBoundsException e) {
         return throwException(stack, args[1],
               "Index " + index + " out of bounds.");
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static SSObject forEach(final Stack stack, final SSObject[] args) {

      final var subject = (SSList) args[0];
      for (var item : subject.elements) {
         args[1].invoke(stack, "executeWith:", new SSObject[] {item.evaluate(stack)});
      }
      return subject;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject size(final Stack stack, final SSObject[] args) {

      final var subject = (SSList) args[0];
      return new SSLong(subject.elements.size());
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static SSObject selectIf(final Stack stack, final SSObject[] args) {

      final var subject = (SSList) args[0];
      final var newStream = subject.elements.stream().filter(item -> {
         var result = args[1].invoke(stack, "executeWith:",
               new SSObject[] {item.evaluate(stack)});
         return result == stack.getTrue();
      });
      return new SSStream(newStream);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static SSObject transformUsing(final Stack stack,
         final SSObject[] args) {

      final var subject = (SSList) args[0];
      final var newStream = subject.elements.stream().map(item -> args[1]
            .invoke(stack, "executeWith:", new SSObject[] {item.evaluate(stack)}));
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

      return o instanceof SSList s && this.elements.equals(s.elements);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   final ArrayList<SSObject> elements;
   
   final static MethodMap sharedMethods = putMethods(
         new MethodMap(SSDynamicObject.sharedMethods, true));
   private final static SSString nature = new SSString("list");
   /****************************************************************************
    * 
    ***************************************************************************/
   public final static class Factory extends SSDynamicObject {
      /*************************************************************************
       * 
      *************************************************************************/
      public Factory() {

         addMethod("new", bb((stack, args) -> new SSList()));
         addMethod("append:", bb(SSList.Factory::append, List.of("item")));
      }
      /*************************************************************************
       * 
      *************************************************************************/
      private static SSObject append(final Stack stack, final SSObject[] args) {

         final var result = new SSList();
         result.elements.add(args[1].evaluate(stack));
         return result;
      }
   }
}
