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
import java.util.Set;
/*******************************************************************************
 * @author lukasz.bownik@gmail.com {
 ******************************************************************************/
public final class SSSet extends SSDynamicObject {
   /****************************************************************************
    * 
   ****************************************************************************/
   private SSSet() {

      this(new HashSet<>());
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSSet(final Set<SSObject> elements) {

      this.elements = new HashSet<>(elements);
      addBinaryMethod("add:", SSSet::append);
      addBinaryMethod("append:", SSSet::append);
      addBinaryMethod("forEach:", SSSet::forEach);
      addBinaryMethod("remove:", SSSet::remove);
      addBinaryMethod("size", SSSet::size);
      addBinaryMethod("selectIf:", SSSet::selectIf);
      addBinaryMethod("transformUsing:", SSSet::transformUsing);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject append(final Stack stack, final List<SSObject> args) {

      final var subject = (SSSet) args.get(0);
      subject.elements.add(args.get(1).evaluate(stack.pushNewFrame()));
      return subject;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject remove(final Stack stack, final List<SSObject> args) {

      final var subject = (SSSet) args.get(0);

      subject.elements.remove(args.get(1).evaluate(stack.pushNewFrame()));
      return subject;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject forEach(final Stack stack, final List<SSObject> args) {

      final var subject = (SSSet) args.get(0);
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

      final var subject = (SSSet) args.get(0);
      return new SSLong(subject.elements.size());
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject selectIf(final Stack stack, final List<SSObject> args) {

      final var subject = (SSSet) args.get(0);
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
   private static SSObject transformUsing(final Stack stack,
         final List<SSObject> args) {

      final var subject = (SSSet) args.get(0);
      final var newStream = subject.elements.stream()
            .map(item -> args.get(1).invoke(stack.pushNewFrame(), "executeWith:",
                  List.of(item.evaluate(stack.pushNewFrame()))));
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
   /****************************************************************************
    * 
    ***************************************************************************/
   public final static class Factory extends SSDynamicObject {
      /*************************************************************************
       * 
      *************************************************************************/
      public Factory() {

         addBinaryMethod("new", SSSet.Factory::createNew);
         addBinaryMethod("append:", SSSet.Factory::append);
      }
      /*************************************************************************
       * 
      *************************************************************************/
      private static SSSet createNew(final Stack stack, final List<SSObject> args) {

         final var result = new SSSet();
         result.addField(stack, "nature", nature);
         return result;
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

         return "Set";
      }
      /*************************************************************************
       * 
      *************************************************************************/
      private final static SSString nature = new SSString("set");
   }
}
