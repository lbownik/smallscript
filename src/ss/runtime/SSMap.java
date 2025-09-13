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

import static java.util.Collections.emptyMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/*******************************************************************************
 * @author lukasz.bownik@gmail.com {
 ******************************************************************************/
public final class SSMap extends SSDynamicObject {
   /****************************************************************************
    * 
   ****************************************************************************/
   SSMap(final Heap heap, final MethodMap methods, final Map<SSObject, SSObject> elements) {

      super(heap, methods);
      this.elements = new HashMap<>(elements);
   }

   /****************************************************************************
    * 
   ****************************************************************************/
   static MethodMap putMethods(final Heap heap, final MethodMap methods) {

      final var listOfKey = List.of("key");
      final var listOfKeyValue = List.of("key", "value");
      
      methods.add("at:", heap.newBinaryBlock(SSMap::at, listOfKey));
      methods.add("at::put:", heap.newBinaryBlock(SSMap::atPut, listOfKeyValue));
      methods.add("at::put::andGetPreviousValue",
            heap.newBinaryBlock(SSMap::atPutAndReturnPreviousValue, listOfKeyValue));
      methods.add("forEach:", heap.newBinaryBlock(SSMap::forEach, List.of("block")));
      methods.add("keys", heap.newBinaryBlock(SSMap::keys));
      methods.add("nature", heap.newBinaryBlock((s, h, a) -> h.newString("map")));
      methods.add("removeAt:", heap.newBinaryBlock(SSMap::removeAt, listOfKey));
      methods.add("removeAt::andGetRemovedValue",
            heap.newBinaryBlock(SSMap::removeAtAndReturnRemovedValue, listOfKey));
      methods.add("size", heap.newBinaryBlock(SSMap::size));

      return methods;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static SSObject newFactory(final Heap heap) {

      final var result = heap.newObject();
      
      result.addMethod("new", heap.newBinaryBlock((s, h ,a) -> h.newMap(emptyMap())));
      result.addMethod("at::put:", heap.newBinaryBlock(SSMap::atPutToNew, List.of("key", "value")));
      result.addMethod("addMethod::using:", heap
            .newBinaryBlock(SSMap::addMethodToFactory, List.of("name", "block")));

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
               "Method 'Map new' cannot be overriden.");
      } else if (name.equals("at::put:")) {
         return SSNativeObject.throwException(stack, heap, subject,
               "Method 'Map at::put:' cannot be overriden.");
      } else {
         return SSNativeObject.addMethod(stack, heap, args);
      }
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static SSObject at(final Stack stack, final Heap heap, final SSObject[] args) {

      final var subject = (SSMap) args[0];
      final var key = args[1].evaluate(stack);

      return subject.elements.getOrDefault(key, stack.getNull());
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject atPut(final Stack stack, final Heap heap, final SSObject[] args) {

      atPutAndReturnPreviousValue(stack, heap, args);
      return args[0];
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject atPutToNew(final Stack stack, final Heap heap, final SSObject[] args) {

      final var result =  heap.newMap(emptyMap());
      final var key = args[1].evaluate(stack);
      final var value = args[2].evaluate(stack);

      result.elements.put(key, value);
      return result;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject atPutAndReturnPreviousValue(final Stack stack, final Heap heap,
         final SSObject[] args) {

      final var subject = (SSMap) args[0];
      final var key = args[1].evaluate(stack);
      final var value = args[2].evaluate(stack);

      final var previous = subject.elements.put(key, value);
      return previous != null ? previous : stack.getNull();
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject removeAt(final Stack stack, final Heap heap, final SSObject[] args) {

      removeAtAndReturnRemovedValue(stack, heap, args);
      return args[0];
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject removeAtAndReturnRemovedValue(final Stack stack,
         final Heap heap,
         final SSObject[] args) {

      final var subject = (SSMap) args[0];
      final var key = args[1].evaluate(stack);

      final var previous = subject.elements.remove(key);
      return previous != null ? previous : stack.getNull();
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   static SSObject forEach(final Stack stack, final Heap heap, final SSObject[] args) {

      final var subject = (SSMap) args[0];
      for (var item : subject.elements.entrySet()) {
         args[1].invoke(stack, "executeWith::and:", new SSObject[] {
               item.getKey().evaluate(stack), item.getValue().evaluate(stack)});
      }
      return subject;
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject keys(final Stack stack, final Heap heap, final SSObject[] args) {

      final var subject = (SSMap) args[0];
      return heap.newSet(subject.elements.keySet().stream());
   }
   /****************************************************************************
    * 
   ****************************************************************************/
   private static SSObject size(final Stack stack, final Heap heap, final SSObject[] args) {

      final var subject = (SSMap) args[0];
      return heap.newLong(subject.elements.size());
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
}
