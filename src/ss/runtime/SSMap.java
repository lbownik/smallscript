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

import java.util.HashMap;
import java.util.List;
/*******************************************************************************
 * @author lukasz.bownik@gmail.com {
 ******************************************************************************/
public final class SSMap extends SSDynamicObject {
   /****************************************************************************
    * 
   ****************************************************************************/
   private SSMap() {

      this(new HashMap<>());
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   public SSMap(final HashMap<SSObject, SSObject> elements) {

      super(new MethodMap(sharedMethods));
      this.elements = new HashMap<>(elements);
   }

   /****************************************************************************
    * 
   ****************************************************************************/
   static Methods putMethods(final Methods methods) {

      methods.add("at:", bb(SSMap::at, List.of("key")));
      methods.add("at::put:", bb(SSMap::atPut, List.of("key", "value")));
      methods.add("at::put::andGetPreviousValue",
            bb(SSMap::atPutAndReturnPreviousValue, List.of("key", "value")));
      methods.add("forEach:", bb(SSMap::forEach, List.of("block")));
      methods.add("keys", bb(SSMap::keys));
      methods.add("nature", bb((s, a) -> nature));
      methods.add("removeAt:", bb(SSMap::removeAt, List.of("key")));
      methods.add("removeAt::andGetRemovedValue",
            bb(SSMap::removeAtAndReturnRemovedValue, List.of("key")));
      methods.add("size", bb(SSMap::size));

      return methods;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   @Override
   protected void addMethod(final String name, final SSObject block) {

      if(this.methods == SSMap.sharedMethods) {
         this.methods = new MethodMap(SSMap.sharedMethods);
      }
      this.methods.add(name, block);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject at(final Stack stack, final List<SSObject> args) {

      final var subject = (SSMap) args.get(0);
      final var key = args.get(1).evaluate(stack);

      return subject.elements.getOrDefault(key, stack.getNull());
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject atPut(final Stack stack, final List<SSObject> args) {

      atPutAndReturnPreviousValue(stack, args);
      return args.get(0);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject atPutAndReturnPreviousValue(final Stack stack,
         final List<SSObject> args) {

      final var subject = (SSMap) args.get(0);
      final var key = args.get(1).evaluate(stack);
      final var value = args.get(2).evaluate(stack);

      final var previous = subject.elements.put(key, value);
      return previous != null ? previous : stack.getNull();
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject removeAt(final Stack stack, final List<SSObject> args) {

      removeAtAndReturnRemovedValue(stack, args);
      return args.get(0);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject removeAtAndReturnRemovedValue(final Stack stack,
         final List<SSObject> args) {

      final var subject = (SSMap) args.get(0);
      final var key = args.get(1).evaluate(stack);

      final var previous = subject.elements.remove(key);
      return previous != null ? previous : stack.getNull();
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject forEach(final Stack stack, final List<SSObject> args) {

      final var subject = (SSMap) args.get(0);
      for (var item : subject.elements.entrySet()) {
         args.get(1).invoke(stack, "executeWith::and:", List
               .of(item.getKey().evaluate(stack), item.getValue().evaluate(stack)));
      }
      return subject;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject keys(final Stack stack, final List<SSObject> args) {

      final var subject = (SSMap) args.get(0);
      return new SSSet(subject.elements.keySet());
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject size(final Stack stack, final List<SSObject> args) {

      final var subject = (SSMap) args.get(0);
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

      return o instanceof SSMap s && this.elements.equals(s.elements);
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private final HashMap<SSObject, SSObject> elements;
   
   final static Methods sharedMethods = putMethods(
         new MethodMap(SSDynamicObject.sharedMethods));
   private final static SSString nature = new SSString("map");
   /****************************************************************************
    * 
    ***************************************************************************/
   public final static class Factory extends SSDynamicObject {
      /*************************************************************************
       * 
      *************************************************************************/
      public Factory() {

         addBinaryMethod("new", (s,a) -> new SSMap());
         addBinaryMethod("at::put:", SSMap.Factory::atPut, List.of("key", "value"));
      }
      /*************************************************************************
       * 
      *************************************************************************/
      private static SSObject atPut(final Stack stack, final List<SSObject> args) {

         final var result = new SSMap();
         final var key = args.get(1).evaluate(stack);
         final var value = args.get(2).evaluate(stack);

         result.elements.put(key, value);
         return result;
      }
   }
}
