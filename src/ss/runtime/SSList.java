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

import java.util.ArrayList;
import java.util.List;
/*******************************************************************************
 * @author lukasz.bownik@gmail.com {
 ******************************************************************************/
public final class SSList extends SSDynamicObject {
   /****************************************************************************
    * 
   ****************************************************************************/
   private SSList() {

      this(new ArrayList<>());
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSList(final List<? extends SSObject> elements) {

      this.elements = new ArrayList<>(elements);
      addBinaryMethod("add:", SSList::append);
      addBinaryMethod("append:", SSList::append);
      addBinaryMethod("at:", SSList::at);
      addBinaryMethod("at::put:", SSList::atPut);
      addBinaryMethod("at::put::andReturnPreviousItem",
            SSList::atPutAndReturnPreviousItem);
      addBinaryMethod("forEach:", SSList::forEach);
      addBinaryMethod("removeAt:", SSList::removeAt);
      addBinaryMethod("removeAt::andReturnRemovedItem",
            SSList::removeAtAndReturnRemovedItem);
      addBinaryMethod("size", SSList::size);
      addBinaryMethod("selectIf:", SSList::selectIf);
      addBinaryMethod("transformUsing:", SSList::transformUsing);
      addField(null, "nature", SSList.Factory.nature);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject append(final Stack stack, final List<SSObject> args) {

      final var subject = (SSList) args.get(0);
      subject.elements.add(args.get(1).evaluate(stack.pushNewFrame()));
      return subject;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject at(final Stack stack, final List<SSObject> args) {

      final var subject = (SSList) args.get(0);
      final var index = ((SSLong) args.get(1).evaluate(stack.pushNewFrame()))
            .intValue();

      try {
         return subject.elements.get(index);
      } catch (final IndexOutOfBoundsException e) {
         return throwException(stack, args.get(1), "Index " + index + " out of bounds.");
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject atPut(final Stack stack, final List<SSObject> args) {

      atPutAndReturnPreviousItem(stack, args);
      return args.get(0);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject atPutAndReturnPreviousItem(final Stack stack,
         final List<SSObject> args) {

      final var subject = (SSList) args.get(0);
      final var index = ((SSLong) args.get(1).evaluate(stack.pushNewFrame()))
            .intValue();

      try {
         return subject.elements.set(index,
               args.get(2).evaluate(stack.pushNewFrame()));
      } catch (final IndexOutOfBoundsException e) {
         return throwException(stack, args.get(1), "Index " + index + " out of bounds.");
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject removeAt(final Stack stack, final List<SSObject> args) {

      removeAtAndReturnRemovedItem(stack, args);
      return args.get(0);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject removeAtAndReturnRemovedItem(final Stack stack,
         final List<SSObject> args) {

      final var subject = (SSList) args.get(0);
      final var index = ((SSLong) args.get(1).evaluate(stack.pushNewFrame()))
            .intValue();

      try {
         return subject.elements.remove(index);
      } catch(final IndexOutOfBoundsException e) {
         return throwException(stack, args.get(1), "Index " + index + " out of bounds.");
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject forEach(final Stack stack, final List<SSObject> args) {

      final var subject = (SSList) args.get(0);
      for (var item : subject.elements) {
         args.get(1).invoke(stack.pushNewFrame(), "executeWith:",
               List.of(item.evaluate(stack.pushNewFrame())));
      }
      return subject;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject size(final Stack stack, final List<SSObject> args) {

      final var subject = (SSList) args.get(0);
      return new SSLong(subject.elements.size());
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject selectIf(final Stack stack, final List<SSObject> args) {

      final var subject = (SSList) args.get(0);
      final var newStream = subject.elements.stream().filter(item -> {
         var result = args.get(1).invoke(stack.pushNewFrame(), "executeWith:",
               List.of(item.evaluate(stack.pushNewFrame())));
         return result == stack.getTrue();
      });
      return new SSStream(newStream);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject transformUsing(final Stack stack, final List<SSObject> args) {

      final var subject = (SSList) args.get(0);
      final var newStream = subject.elements.stream().map(item -> 
         args.get(1).invoke(stack.pushNewFrame(), "executeWith:",
               List.of(item.evaluate(stack.pushNewFrame())))
      );
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
   /****************************************************************************
    * 
    ***************************************************************************/
   public final static class Factory extends SSDynamicObject {
      /*************************************************************************
       * 
      *************************************************************************/
      public Factory() {

         addBinaryMethod("new", SSList.Factory::createNew);
         addBinaryMethod("append:", SSList.Factory::append);
      }
      /*************************************************************************
       * 
      *************************************************************************/
      private static SSList createNew(final Stack stack, final List<SSObject> args) {

         return new SSList();
      }
      /*************************************************************************
       * 
      *************************************************************************/
      private static SSObject append(final Stack stack, final List<SSObject> args) {

         final var result = createNew(stack, args);
         result.elements.add(args.get(1).evaluate(stack.pushNewFrame()));
         return result;
      }
      /*************************************************************************
       * 
      *************************************************************************/
      @Override
      public String toString() {

         return "List";
      }
      /*************************************************************************
       * 
      *************************************************************************/
      private final static SSString nature = new SSString("list");
   }
}
