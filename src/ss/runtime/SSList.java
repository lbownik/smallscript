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
   public SSList(final List<SSObject> elements) {

      this.elements = new ArrayList<>(elements);
      addBinaryMethod("add:", SSList::append);
      addBinaryMethod("append:", SSList::append);
      addBinaryMethod("at:", SSList::at);
      addBinaryMethod("forEach:", SSList::forEach);
      addBinaryMethod("size", SSList::size);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject append(final Stack stack, final List<SSObject> args) {

      final var subject = (SSList) args.get(0);
      subject.elements.add(args.get(1));
      return subject;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject at(final Stack stack, final List<SSObject> args) {

      final var subject = (SSList) args.get(0);
      final var index = ((SSLong) args.get(1)).intValue();

      if (index > -1 & index < subject.elements.size()) {
         return subject.elements.get(index);
      } else {
         return throwException(args.get(1), "Index " + index + " out of bounds.");
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject forEach(final Stack stack, final List<SSObject> args) {

      final var subject = (SSList) args.get(0);
      for (var item : subject.elements) {
         args.get(1).invoke(stack.pushNewFrame(), "executeWith:", List.of(item));
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
   private final ArrayList<SSObject> elements;
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
      private static SSList createNew(final Stack stack,
            final List<SSObject> args) {

         final var result = new SSList();
         result.addField("nature", nature);
         return result;
      }
      /*************************************************************************
       * 
      *************************************************************************/
      private static SSObject append(final Stack stack, final List<SSObject> args) {

         final var result = createNew(stack, args);
         result.elements.add(args.get(1));
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
